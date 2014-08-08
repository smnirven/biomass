(ns ^{:author "kitallis"
      :doc "Contains methods for making QualificationRequirement API requests to MTurk"}
  biomass.builder.qualification-requirement
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
  {:approval_rate "000000000000000000L0" :submission_rate "00000000000000000000"
   :abandoned_rate "00000000000000000070" :return_rate "000000000000000000E0"
   :rejection_rate  "000000000000000000S0" :hits_approved "00000000000000000040"
   :adult "00000000000000000060" :country "00000000000000000071"
   :master "2F1QJWKUDD8XADTFD2Q0G6UTO95ALH"
   :categorization_masters "2NDP2L92HECWY8NS8H3CK0CP5L9GHO"
   :photo_moderation_masters "21VZU98JHSTLZ5BPP4A9NOBJEK3DPG"})

(defonce request-schema
  (let [base {:QualificationRequirement.1.IntegerValue s/Int
              :QualificationRequirement.1.Comparator s/Str
              :QualificationRequirement.1.QualificationTypeId s/Str
              :QualificationRequirement.1.RequiredToPreview s/Bool}]

    {:country (assoc base
                :QualificationRequirement.1.LocaleValue.Country s/Str
                :QualificationRequirement.1.Comparator (s/enum (:eql comparators) (:not comparators) (:in comparators) (:not-in comparators)))

     :adult (assoc base :IntegerValue (s/enum 1 0) :Comparator (s/enum (:eql comparators)))

     :masters (assoc base :IntegerValue s/Int)
     :categorization-masters (assoc base :IntegerValue s/Int)
     :photo-moderation-masters (assoc base :IntegerValue s/Int)

     :default base}))

(defn- validate-request
  [params]
  (let [qualification-type-id (or (:qualification-type-id request-schema) (:default request-schema))]
    (s/validate qualification-type-id params)))

(defn- build
  [params]
  (let [qualification-type-id (:qualification-type-id params)
        base-request {:QualificationRequirement.1.QualificationTypeId (type-ids (:qualification-type-id params))
                      :QualificationRequirement.1.RequiredToPreview (:required-to-preview params)
                      :QualificationRequirement.1.Comparator (comparators (:comparator params))}
        value (:value params)]
    (if (= qualification-type-id :country) (assoc base-request :QualificationRequirement.1.LocaleValue.Country value) (assoc base-request :QualificationRequirement.1.IntegerValue value))))

(defn build-qualification
  [{:keys [qualification-type-id comparator value required-to-preview]
    :or   [required-to-preview true] :as params}]
  (-> params
      build
      validate-request))
