(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer :all]
            [biomass.response.hits :refer :all]
            [biomass.util :as util]
            [biomass.builder.builder :refer [convert-params]]
            [biomass.builder.schemas :refer :all]
            [schema.core :as s]))

(defn- send-and-parse
  [operation params]
  (let [resp (send-request operation params)]
    (prn "RESP: " resp)
    (when (= (:status resp) 200)
      (parse operation (:body resp)))))

(defn get-hit
  [hit-id]
  (send-and-parse "GetHIT" {:HITId hit-id}))

(defn get-reviewable-hits
  ([]
   (send-and-parse "GetReviewableHITs" {}))
  ([params]
   (send-and-parse "GetReviewableHITs" (convert-params (s/validate schema-GetReviewableHITs params)))))

(defn search-hits
  ([]
   (send-and-parse "SearchHITs" {}))
  ([params]
   (send-and-parse "SearchHITs" (convert-params (s/validate schema-SearchHITs params)))))

(defn get-hits-for-qualification-type
  [{:keys [qualification-type-id page-size page-number]}]
  ;;TODO: defaults for optional page-size and page-number params
  (send-request {:Operation "GetHITsForQualificationType"
                 :PageSize page-size
                 :PageNumber page-number}))

(defn register-hit-type
  [params]
  (send-and-parse "RegisterHITType" (convert-params (s/validate schema-RegisterHITType params))))

(defn create-hit
  [params]
  (send-and-parse "CreateHIT" (convert-params (s/validate schema-CreateHIT params))))

(defn disable-hit
  [hit-id]
  (send-and-parse "DisableHIT" {:HITId hit-id}))

(defn dispose-hit
  [hit-id]
  (send-and-parse "DisposeHIT" {:HITId hit-id}))

(defn change-hit-type-of-hit
  [hit-id hit-type-id]
  (send-and-parse "ChangeHITTypeOfHIT" {:HITId hit-id
                                        :HITTypeId hit-type-id}))

(defn extend-hit
  [params]
  (send-and-parse "ExtendHIT" (s/validate schema-ExtendHIT params)))

(defn force-expire-hit
  [hit-id]
  (send-and-parse "ForceExpireHIT" {:HITId hit-id}))

(defn get-assignments-for-hits
  [params]
  (send-and-parse "GetAssignmentsForHIT" (s/validate schema-GetAssignmentsForHIT params)))

(defn get-review-results-for-hit
  [params]
  (send-and-parse "GetReviewResultsForHIT" (s/validate schema-GetReviewResultsForHIT params)))

(defn set-hit-as-reviewing
  [params]
  (send-and-parse "SetHITAsReviewing" (s/validate schema-SetHITAsReviewing params)))

(defn set-hit-type-notification
  [params]
  (send-and-parse "SetHITTypeNotification" (s/validate schema-SetHITTypeNotification params)))
