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

(defonce type-ids
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
              (:i-value request-params) s/Int
              (:preview request-params) s/Bool}]

    {:country (assoc base
                (:country request-params) s/Str
                (:comparator request-params) (s/enum (:eql comparators) (:not comparators) (:in comparators) (:not-in comparators)))

     :adult (assoc base
              (:i-value request-params) (s/enum 1 0)
              (:comparator request-params) (s/enum (:eql comparators)))

     :masters (assoc base (:i-value request-params) s/Int)

     :default base}))

(defn- request-params-convert
  [qr-no params]
  (let [pre-text "QualificationRequirement"
        convert (fn [t] (keyword (str pre-text "." qr-no "." (name t))))]
    (reduce (fn [r [k v]] (assoc r (convert k) v))
            {}
            params)))

(defn- request-params-validate
  [{:keys [qualification-type-id] :as params}]
  (let [type (or (get request-params-schema qualification-type-id) (get request-params-schema :default))]
    (s/validate type params)))

(defn- request-params-populate
  [{:keys [qualification-type-id comparator value required-to-preview]
    :or   {required-to-preview true} :as params}]
  (let [base-request {(:type request-params) (get type-ids qualification-type-id)
                      (:preview request-params) required-to-preview
                      (:comparator request-params) (get comparators comparator)}]
    (if (= type :country)
      (assoc base-request (:country request-params) value)
      (assoc base-request (:i-value request-params) value))))

(defn- request-params-build
  [qr-no params]
  (->> params
       request-params-populate
       request-params-validate
       (request-params-convert qr-no)))

(defn build
  [& qualifications]
  (reduce merge {} (map-indexed request-params-build qualifications)))
