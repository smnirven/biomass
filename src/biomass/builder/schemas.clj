(ns ^{:author "shafeeq"
      :doc "Contains schemas for params of various operatoins"}
    biomass.builder.schemas
  (:require [schema.core :as s]))


(defonce schema-Price
  {(s/optional-key :Amount) s/Num
   :CurrencyCode s/Str
   (s/optional-key :FormattedPrice) s/Str})

(defonce schema-Locale
  ;; TODO Subdivisions
  {:Country (s/if sequential? [s/Str] s/Str)})

(defonce schema-QualificationRequirement
  {:QualificationTypeId s/Str
   :Comparator s/Str
   (s/optional-key :IntegerValue) (s/if sequential? [s/Int] s/Int)
   (s/optional-key :LocaleValue) (s/if sequential? [schema-Locale] schema-Locale)
   (s/optional-key :RequiredToPreview) s/Bool})

(defonce schema-HITLayoutParameter
  {:Name s/Str
   :Value s/Str})

(defonce schema-RegisterHITType
  {:Title s/Str
   :Description s/Str
   :Reward schema-Price
   :AssignmentDurationInSeconds s/Int
   (s/optional-key :Keywords) s/Str
   (s/optional-key :AutoApprovalDelayInSeconds) s/Int
   (s/optional-key :QualificationRequirement) (s/if sequential? [schema-QualificationRequirement] schema-QualificationRequirement)})


(defonce schema-CreateHIT
  {:HITTypeId s/Str
   (s/optional-key :Question) s/Str
   (s/optional-key :HITLayoutId) s/Str
   (s/optional-key :HITLayoutParameter) (s/if sequential? [schema-HITLayoutParameter] schema-HITLayoutParameter)
   :LifetimeInSeconds s/Int
   (s/optional-key :MaxAssignments) s/Int
   (s/optional-key :AssignmentReviewPolicy) s/Str
   (s/optional-key :HITReviewPolicy) s/Str
   (s/optional-key :RequesterAnnotation) s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce schema-notification-event-type
  (s/enum "AssignmentAccepted" "AssignmentAbandoned" "AssignmentReturned" "AssignmentSubmitted" "AssignmentRejected" "AssignmentApproved" "HITCreated" "HITExtended" "HITDisposed" "HITReviewable" "HITExpired" "Ping"))

(defonce schema-Notification
  {:Destination s/Str
   :Transport (s/enum "Email" "SQS")
   :EventType (s/if sequential? [schema-notification-event-type] schema-notification-event-type)})

(defonce schema-GetReviewableHITs
  {(s/optional-key :HITTypeId) s/Str
   (s/optional-key :Status) s/Str
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce schema-SearchHITs
  {(s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce schema-ExtendHIT
  {:HITId s/Str
   (s/optional-key :MaxAssignmentsIncrement) s/Int
   (s/optional-key :ExpirationIncrementInSeconds) s/Int
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce schema-GetAssignmentsForHIT
  {:HITId s/Str
   (s/optional-key :AssignmentStatus) s/Str
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce schema-GetReviewResultsForHIT
  {:HITId s/Str
   (s/optional-key :PolicyLevel) s/Str
   (s/optional-key :AssignmentId) s/Str
   (s/optional-key :RetrieveActions) (s/enum "T" "F")
   (s/optional-key :RetrieveResults) (s/enum "T" "F")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce schema-SetHITAsReviewing
  {:HITId s/Str
   (s/optional-key :Revert) s/Bool})

(defonce schema-SetHITTypeNotification
  {:HITTypeId s/Str
   :Notification schema-Notification
   (s/optional-key :Active) s/Bool})

(defonce schema-GetAssignmentsForHIT
  {:HITId s/Str
   (s/optional-key :AssignmentStatus) (s/enum "Submitted" "Approved" "Rejected")
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})
