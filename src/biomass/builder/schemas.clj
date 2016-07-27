(ns ^{:author "shafeeq"
      :doc "Contains schemas for params of various operatoins"}
    biomass.builder.schemas
  (:require [schema.core :as s]))

(defn maybe-sequential [schema]
  (s/if sequential? [schema] schema))

(defonce Price
  {(s/optional-key :Amount) s/Num
   :CurrencyCode s/Str
   (s/optional-key :FormattedPrice) s/Str})

(defonce Locale
  ;; TODO Subdivisions
  {:Country (maybe-sequential s/Str)})

(defonce QualificationRequirement
  {:QualificationTypeId s/Str
   :Comparator s/Str
   (s/optional-key :IntegerValue) (maybe-sequential s/Int)
   (s/optional-key :LocaleValue) (maybe-sequential Locale)
   (s/optional-key :RequiredToPreview) s/Bool})

(defonce HITLayoutParameter
  {:Name s/Str
   :Value s/Str})

(defonce RegisterHITType
  {:Title s/Str
   :Description s/Str
   :Reward Price
   :AssignmentDurationInSeconds s/Int
   (s/optional-key :Keywords) s/Str
   (s/optional-key :AutoApprovalDelayInSeconds) s/Int
   (s/optional-key :QualificationRequirement) (maybe-sequential QualificationRequirement)})


(defonce CreateHIT
  {:HITTypeId s/Str
   (s/optional-key :Question) s/Str
   (s/optional-key :HITLayoutId) s/Str
   (s/optional-key :HITLayoutParameter) (maybe-sequential HITLayoutParameter)
   :LifetimeInSeconds s/Int
   (s/optional-key :MaxAssignments) s/Int
   (s/optional-key :AssignmentReviewPolicy) s/Str
   (s/optional-key :HITReviewPolicy) s/Str
   (s/optional-key :RequesterAnnotation) s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce notification-event-type
  (s/enum "AssignmentAccepted" "AssignmentAbandoned" "AssignmentReturned" "AssignmentSubmitted" "AssignmentRejected" "AssignmentApproved" "HITCreated" "HITExtended" "HITDisposed" "HITReviewable" "HITExpired" "Ping"))

(defonce Notification
  {:Destination s/Str
   :Transport (s/enum "Email" "SQS")
   :EventType (maybe-sequential notification-event-type)})

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

(defonce ExtendHIT
  {:HITId s/Str
   (s/optional-key :MaxAssignmentsIncrement) s/Int
   (s/optional-key :ExpirationIncrementInSeconds) s/Int
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce GetAssignmentsForHIT
  {:HITId s/Str
   (s/optional-key :AssignmentStatus) s/Str
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

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
   :Notification Notification
   (s/optional-key :Active) s/Bool})

(defonce GetAssignmentsForHIT
  {:HITId s/Str
   (s/optional-key :AssignmentStatus) (s/enum "Submitted" "Approved" "Rejected")
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce AssignQualification
  {:QualificationTypeId s/Str
   :WorkerId s/Str
   :IntegerValue s/Int
   :SendNotification s/Bool})

(defonce CreateQualificationType
  {:Name s/Str
   :Description s/Str
   (s/optional-key :Keywords) s/Str
   (s/optional-key :RetryDelayInSeconds) s/Int
   :QualificationTypeStatus (s/enum "Active" "Inactive")
   (s/optional-key :Test) s/Str ;; Change to qurstionform
   (s/optional-key :AnswerKey) s/Str ;; Change to answerkey
   (s/optional-key :TestDurationInSeconds) s/Int
   (s/optional-key :AutoGranted) s/Bool
   (s/optional-key :AutoGrantedValue) s/Int})

(defonce GetHITsForQualificationType
  {:QualificationTypeId s/Str
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce DisposeQualificationType
  {:QualificationTypeId s/Str})

(defonce GetQualificationsForQualificationType
  {:QualificationTypeId s/Str
   (s/optional-key :Status) (s/enum "Granted" "Revoked")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetQualificationRequests
  {(s/optional-key :QualificationTypeId) s/Str
   (s/optional-key :SortProperty) (s/enum "QualificationTypeId" "SubmitTime")
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetQualificationScore
  {:QualificationTypeId s/Str
   :SubjectId s/Str})

(defonce GetQualificationType
  {:QualificationTypeId s/Str})

(defonce GrantQualification
  {:QualificationRequestId s/Str
   (s/optional-key :IntegerValue) s/Int})

(defonce RejectQualificationRequest
  {:QualificationRequestId s/Str
   (s/optional-key :Reason) s/Str})

(defonce RevokeQualification
  {:QualificationTypeId s/Str
   :SubjectId s/Str
   (s/optional-key :Reason) s/Str})

(defonce SearchQualificationTypes
  {(s/optional-key :Query) s/Str
   (s/optional-key :SortProperty) (s/enum "QualificationTypeId" "SubmitTime")
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int
   :MustBeRequestable s/Bool
   (s/optional-key :MustBeOwnedByCaller) s/Bool})

(defonce UpdateQualificationScore
  {:QualificationTypeId s/Str
   :SubjectId s/Str
   :IntegerValue s/Int})

(defonce UpdateQualificationType
  {:QualificationTypeId s/Str
   (s/optional-key :RetryDelayInSeconds) s/Int
   (s/optional-key :QualificationTypeStatus) (s/enum "Active" "Inactive")
   (s/optional-key :Description) s/Str
   (s/optional-key :Test) s/Str ;; Change to qurstionform
   (s/optional-key :AnswerKey) s/Str ;; Change to answerkey
   (s/optional-key :TestDurationInSeconds) s/Int
   (s/optional-key :AutoGranted) s/Bool
   (s/optional-key :AutoGrantedValue) s/Int})

(defonce BlockWorker
  {:WorkerId s/Str
   :Reason s/Str})

(defonce GetBlockedWorkers
  {(s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce NotifyWorkers
  {:Subject s/Str
   :MessageText s/Str
   :WorkerId (maybe-sequential s/Str)})

(defonce UnblockWorker
  {:WorkerId s/Str
   :Reason s/Str})
