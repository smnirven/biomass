(ns ^{:author "smnirven"
      :doc "Contains methods needed for all manner of requests to MTurk"}
    biomass.request
  (:require [ring.util.codec :as codec]
            [clj-time.local :refer [local-now format-local-time]]
            [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [biomass.assignments]
            [biomass.hits]
            [biomass.misc]
            [biomass.qualifications]
            [biomass.workers]
            [biomass.builder.builder])
  (:import (javax.crypto Mac)
           (javax.crypto.spec SecretKeySpec)))

(defonce $HMAC_ALGORITHM "HmacSHA1")
(defonce $API_VERSION "2014-08-15")
(defonce $SERVICE "AWSMechanicalTurkRequester")
(defonce $SANDBOX_BASE_URL "https://mechanicalturk.sandbox.amazonaws.com/")
(defonce $PRODUCTION_BASE_URL "https://mechanicalturk.amazonaws.com/")

(def aws-access-key (ref nil))
(def aws-secret-access-key (ref nil))
(def base-url (ref nil))
(def sandbox-mode (ref nil))

(defn setup
  [{:keys [AWSAccessKey AWSSecretAccessKey sandbox] :or {sandbox false}}]
  (dosync
   (ref-set aws-access-key AWSAccessKey)
   (ref-set aws-secret-access-key AWSSecretAccessKey)
   (ref-set sandbox-mode sandbox)
   (ref-set base-url (if @sandbox-mode $SANDBOX_BASE_URL $PRODUCTION_BASE_URL))))

(defn gen-aws-signature
  "Generates an RFC 2104 compliant HMAC for AWS authentication as
  outlined in the following article:
  http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMechanicalTurkRequester/MakingRequests_RequestAuthenticationArticle.html"
  [operation timestamp]
  (let [data (str $SERVICE operation timestamp)
        signing-key (SecretKeySpec. (.getBytes @aws-secret-access-key) $HMAC_ALGORITHM)
        mac (doto (Mac/getInstance $HMAC_ALGORITHM) (.init signing-key))]
    (codec/base64-encode (.doFinal mac (.getBytes data)))))

(defn- gen-timestamp
  []
  (format-local-time (local-now) :date-time-no-ms))

(defn get-default-params
  [operation]
  (let [ts (gen-timestamp)]
    {:AWSAccessKeyId @aws-access-key
     :Version $API_VERSION
     :Service $SERVICE
     :Timestamp ts
     :Signature (gen-aws-signature operation ts)
     :Operation operation}))

(defn send-request
  [operation params]
  (let [final-params (merge params (get-default-params operation))]
    (client/get @base-url {:query-params final-params})))

(defn parse-zipped-xml
  [struct]
  (cond
    (map? struct)
    {(:tag struct) (parse-zipped-xml (:content struct))}

    (sequential? struct)
    (map parse-zipped-xml struct)

    :else
    struct))

(defn send-and-parse
  [operation params]
  (if (some nil? [@aws-access-key @aws-secret-access-key])
    (throw (RuntimeException. "One or more access keys not set"))

    (let [response (send-request operation params)]
      (if (= (:status response) 200)
        {:status :success
         :response (let [response-stream (->> response :body .getBytes java.io.ByteArrayInputStream.)]
                     (->> response-stream
                          xml/parse
                          zip/xml-zip
                          first
                          parse-zipped-xml
                          vector))}
        {:status :error
         :status-code (:status response)
         :response [(:body response)]}))))

(def operations-actions-map
  (merge biomass.assignments/assignments-operations
         biomass.hits/hits-operations
         biomass.misc/misc-operations
         biomass.qualifications/qualifications-operations
         biomass.workers/worker-operations))

(defn get-or-throw-exception
  [map key]
  (if (contains? map key)
    (get map key)
    (throw (RuntimeException. (str key " : No such operation defined")))))

(defn requester
  [operation params]
  (let [{:keys [op-string schema validator]} (get-or-throw-exception operations-actions-map operation)
        validated-params (if validator
                           (validator params)
                           params)
        schema-validated-params (if schema
                                  (schema.core/validate schema validated-params)
                                  validated-params)]
    (send-and-parse op-string (biomass.builder.builder/->amazon-format schema-validated-params))))
