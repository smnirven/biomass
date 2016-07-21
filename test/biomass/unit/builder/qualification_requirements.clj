(ns biomass.unit.builder.qualification-requirements
  (:require [midje.sweet :refer :all]
            [midje.util :refer [expose-testables]]
            [biomass.builder.qualification-requirements :refer :all]))

(expose-testables biomass.builder.qualification-requirements)


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

(def hits-approved-qualification-multivalued
  {:qualification-type-id :hits_approved
   :comparator :in
   :value [10 20 30 40]})

(def locale-qualification-multivalued
  {:qualification-type-id :country
   :comparator :in
   :country ["US" "GB" "IN"]})

(fact "Generate hits-approved-qualification populated-request"
      (request-params-populate hits-approved-qualification)
      => {:qualification-type-id :hits_approved,
          :base-request {:QualificationTypeId "00000000000000000040",
                         :RequiredToPreview true,
                         :Comparator "GreaterThanOrEqualTo",
                         :IntegerValue 2000}})

(fact "Generate master-qualification populated-request (production type, since setup! is not called for test now)"
      (request-params-populate master-qualification)
      =>  {:qualification-type-id :master,
           :base-request {:QualificationTypeId "2F1QJWKUDD8XADTFD2Q0G6UTO95ALH",
                          :RequiredToPreview true,
                          :Comparator "Exists"}})

(fact "Generate locale-qualification populated-request"
      (request-params-populate locale-qualification)
      => {:qualification-type-id :country,
          :base-request {:QualificationTypeId "00000000000000000071",
                         :RequiredToPreview true,
                         :Comparator "EqualTo",
                         :LocaleValue.Country "US"}})

(fact "Build qualification request for multiple qualifications"
      (build hits-approved-qualification master-qualification locale-qualification locale-qualification-multivalued)
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
          :QualificationRequirement.2.RequiredToPreview true
          :QualificationRequirement.3.QualificationTypeId "00000000000000000071"
          :QualificationRequirement.3.RequiredToPreview true
          :QualificationRequirement.3.Comparator "In"
          :QualificationRequirement.3.LocaleValue.0.Country "US"
          :QualificationRequirement.3.LocaleValue.1.Country "GB"
          :QualificationRequirement.3.LocaleValue.2.Country "IN"})

(fact "Convert vector of IntegerValues to individual params"
      (convert-multivalued-integers (request-params-validate(request-params-populate hits-approved-qualification-multivalued)))
      => {:QualificationTypeId "00000000000000000040",
          :RequiredToPreview true,
          :Comparator "In",
          :IntegerValue.0 10
          :IntegerValue.1 20
          :IntegerValue.2 30
          :IntegerValue.3 40})

(fact "Convert vector of LocaleValues to individual params"
      (build locale-qualification-multivalued)
      =>  {:QualificationRequirement.0.QualificationTypeId "00000000000000000071"
           :QualificationRequirement.0.RequiredToPreview true
           :QualificationRequirement.0.Comparator "In"
           :QualificationRequirement.0.LocaleValue.0.Country "US"
           :QualificationRequirement.0.LocaleValue.1.Country "GB"
           :QualificationRequirement.0.LocaleValue.2.Country "IN"})
