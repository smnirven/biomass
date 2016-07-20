(ns biomass.unit.builder.qualification-requirements
  (:require [midje.sweet :refer :all]
            [midje.util :refer [testable-privates]]
            [biomass.builder.qualification-requirements :refer :all]))

(testable-privates biomass.builder.qualification-requirements request-params-populate)

(def hits-approved-qualification
  {:qualification-type-id :hits_approved
   :comparator :gte
   :value 2000})

(def master-qualification
  {:qualification-type-id :master
   :comparator :exists})

(def locale-qualification
  {:qualification-type-id :country
   :comparator :eql
   :country "US"})

(fact "Generate hits-approved-qualification populated-request"
      (request-params-populate hits-approved-qualification) => {:qualification-type-id :hits_approved,
                                                                :base-request {:QualificationTypeId "00000000000000000040",
                                                                               :RequiredToPreview true,
                                                                               :Comparator "GreaterThanOrEqualTo",
                                                                               :IntegerValue 2000}})

(fact "Generate master-qualification populated-request (production type, since setup! is not called for test now)"
      (request-params-populate master-qualification) =>  {:qualification-type-id :master,
                                                          :base-request {:QualificationTypeId "2F1QJWKUDD8XADTFD2Q0G6UTO95ALH",
                                                                         :RequiredToPreview true,
                                                                         :Comparator "Exists"}})

(fact "Generate locale-qualification populated-request"
      (request-params-populate locale-qualification) => {:qualification-type-id :country,
                                                         :base-request {:QualificationTypeId "00000000000000000071",
                                                                        :RequiredToPreview true,
                                                                        :Comparator "EqualTo",
                                                                        :LocaleValue.Country "US"}})

(fact "Build qualification request for multiple qualifications"
      (build hits-approved-qualification master-qualification locale-qualification)
      => {:QualificationRequirement.0.QualificationTypeId "00000000000000000040"
          :QualificationRequirement.0.Comparator "GreaterThanOrEqualTo"
          :QualificationRequirement.0.IntegerValue 2000
          :QualificationRequirement.0.RequiredToPreview true
          :QualificationRequirement.1.QualificationTypeId "2F1QJWKUDD8XADTFD2Q0G6UTO95ALH"
          :QualificationRequirement.1.Comparator "Exists"
          :QualificationRequirement.1.RequiredToPreview true
          :QualificationRequirement.2.QualificationTypeId "00000000000000000071"
          :QualificationRequirement.2.Comparator "EqualTo"
          :QualificationRequirement.2.LocaleValue.Country "US"
          :QualificationRequirement.2.RequiredToPreview true})
