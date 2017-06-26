(ns biomass.schemas.hits
  (:require [schema.core :as s]
            [biomass.schemas.schemas :as schemas]
            [biomass.schemas.qualifications]))

(defonce HITLayoutParameter
  {:Name s/Str
   :Value s/Str})

(defonce MapEntry
  {:Key s/Str
   :Value (schemas/maybe-sequential s/Str)})

(defonce Parameter
  {:Key s/Str
   (s/optional-key :Value) s/Any
   (s/optional-key :MapEntry) (schemas/maybe-sequential MapEntry)})

(defonce HITReviewPolicy
  {:PolicyName s/Str
   :Parameter (schemas/maybe-sequential Parameter)})

(defonce GetReviewableHITs
  {(s/optional-key :HITTypeId) s/Str
   (s/optional-key :Status) s/Str
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce SearchHITs
  {(s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce RegisterHITType
  {:Title s/Str
   :Description s/Str
   :Reward schemas/Price
   :AssignmentDurationInSeconds s/Int
   (s/optional-key :Keywords) s/Str
   (s/optional-key :AutoApprovalDelayInSeconds) s/Int
   (s/optional-key :QualificationRequirement) (schemas/maybe-sequential biomass.schemas.qualifications/QualificationRequirement)})

(defonce CreateHIT
  {:HITTypeId s/Str
   (s/optional-key :Question) s/Str
   (s/optional-key :HITLayoutId) s/Str
   (s/optional-key :HITLayoutParameter) (schemas/maybe-sequential HITLayoutParameter)
   :LifetimeInSeconds s/Int
   (s/optional-key :MaxAssignments) s/Int
   (s/optional-key :AssignmentReviewPolicy) HITReviewPolicy
   (s/optional-key :HITReviewPolicy) HITReviewPolicy
   (s/optional-key :RequesterAnnotation) s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce GetHITsForQualificationType
  {:QualificationTypeId s/Str
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce ChangeHITTypeOfHIT
  {:HITId s/Str
   :HITTypeId s/Str})

(defonce ExtendHIT
  {:HITId s/Str
   (s/optional-key :MaxAssignmentsIncrement) s/Int
   (s/optional-key :ExpirationIncrementInSeconds) s/Int
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce GetReviewResultsForHIT
  {:HITId s/Str
   (s/optional-key :PolicyLevel) s/Str
   (s/optional-key :AssignmentId) s/Str
   (s/optional-key :RetrieveActions) (s/enum "T" "F")
   (s/optional-key :RetrieveResults) (s/enum "T" "F")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce SetHITAsReviewing
  {:HITId s/Str
   (s/optional-key :Revert) s/Bool})

(defonce SetHITTypeNotification
  {:HITTypeId s/Str
   :Notification schemas/Notification
   (s/optional-key :Active) s/Bool})

(defonce HITIdOnly
  {:HITId s/Str})
