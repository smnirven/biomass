(ns ^{:author "kitallis"
      :doc "Contains methods for building a QualificationRequirement"}
  biomass.builder.qualification-requirements
  (:require [biomass.request :refer :all]
            [biomass.response.hits :refer :all]
            [biomass.util :as util]
            [schema.core :as s]))

(defonce comparators
  {:gt "GreaterThan" :lt "LessThan"
   :gte "GreaterThanOrEqualTo" :lte "LessThanOrEqualTo"
   :eql "EqualTo" :not "NotEqualTo"
   :exists "Exists" :does-not-exist "DoesNotExist"
   :in "In" :not-in "NotIn"})

(defn type-ids []
  {:approval_rate "000000000000000000L0" :hits_approved "00000000000000000040"
   :adult "00000000000000000060" :country "00000000000000000071"
   :master (if @sandbox-mode "2ARFPLSP75KLA8M8DH1HTEQVJT3SY6" "2F1QJWKUDD8XADTFD2Q0G6UTO95ALH")})

(defonce request-params
  {:type :QualificationTypeId
   :comparator :Comparator
   :i-value :IntegerValue
   :country :LocaleValue.Country
   :preview :RequiredToPreview})

(defonce request-params-schema
  (let [base {(:type request-params) s/Str
              (:comparator request-params) s/Str
              (s/optional-key (:preview request-params)) s/Bool}]

    {:country (assoc base
                (:country request-params) (s/if sequential? [s/Str] s/Str)
                (:comparator request-params) (s/enum (:eql comparators) (:not comparators) (:in comparators) (:not-in comparators)))

     :adult (assoc base
              (:i-value request-params) (s/enum 1 0)
              (:comparator request-params) (s/enum (:eql comparators)))

     :master base

     :default (assoc base (:i-value request-params) (s/if sequential? [s/Int] s/Int))}))

(defn- request-params-convert
  [qr-no params]
  (let [pre-text "QualificationRequirement"
        convert (fn [t] (keyword (str pre-text "." qr-no "." (name t))))]
    (reduce (fn [r [k v]] (assoc r (convert k) v))
            {}
            params)))

(defn- ^{:testable true} request-params-validate
  [{:keys [qualification-type-id base-request]}]
  (let [type (or (get request-params-schema qualification-type-id) (get request-params-schema :default))]
    (s/validate type base-request)))

(defn- ^{:testable true} request-params-populate
  [{:keys [qualification-type-id comparator value required-to-preview country]
    :or   {required-to-preview true} :as params}]
  (let [base-request {(:type request-params) (get (type-ids) qualification-type-id)
                      (:preview request-params) required-to-preview
                      (:comparator request-params) (get comparators comparator)}
        updated-request (cond
                          (= qualification-type-id :country) (assoc base-request (:country request-params) country)
                          (not= qualification-type-id :master) (assoc base-request (:i-value request-params) value)
                          :else base-request)]
    {:qualification-type-id qualification-type-id :base-request updated-request}))

(defn- ^{:testable true} convert-multivalued-integers
  [base-request]
  (let [updated-request (if (sequential? (:IntegerValue base-request))
                          (merge (dissoc base-request :IntegerValue)
                                 (reduce (fn [vals [i v]]
                                           (assoc vals (keyword (str "IntegerValue" "." i)) v))
                                         {}
                                         (map-indexed vector (:IntegerValue base-request))))
                          base-request)]
    updated-request))


(defn- ^{:testable true} convert-multivalued-locales
  [base-request]
  (let [updated-request (if (sequential? (:LocaleValue.Country base-request))
                          (merge (dissoc base-request :LocaleValue.Country)
                                 (reduce (fn [vals [i v]]
                                           (assoc vals (keyword (str "LocaleValue" "." i ".Country")) v))
                                         {}
                                         (map-indexed vector (:LocaleValue.Country base-request))))
                          base-request)]
    updated-request))


(defn- request-params-build
  [qr-no params]
  (->> params
       request-params-populate
       request-params-validate
       convert-multivalued-integers
       convert-multivalued-locales
       (request-params-convert qr-no)))

(defn build
  [& qualifications]
  (reduce merge {} (map-indexed request-params-build qualifications)))
