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

(def int-val-comparators (s/enum "LessThan" "LessThanOrEqualTo" "GreaterThan" "GreaterThanOrEqualTo"
                                 "EqualTo" "NotEqualTo" "In" "NotIn"))
(def worker-locale-id "00000000000000000071")
(def locale-val-compartors (s/enum "EqualTo" "NotEqualTo" "In" "NotIn"))
(def exists-comparators (s/enum "Exists" "NotExists"))

(defmacro throw-with-msg
  [message]
  (list 'throw (list 'clojure.lang.ExceptionInfo. (list 'str "Malformed QualificationRequirement: " message) 'params)))

(defn- validate-qualification-requirement-with-integer-value
  [params]
  (s/validate int-val-comparators (:Comparator params))

  (cond
    (= (:QualificationTypeId params) worker-locale-id)
    (throw-with-msg "Cannot use IntegerValue with Worker_Locale QualificationTypeId")

    (and (sequential? (:IntegerValue params))
         (> (count (:IntegerValue params)) 15))
    (throw-with-msg "Can use up to only 15 IntegerValue elements using the In or the NotIn Comparator")))


(defn- validate-qualification-requirement-with-locale-value
  [params]
  (s/validate locale-val-compartors (params :Comparator))

  (cond
    (not= worker-locale-id (:QualificationTypeId params))
    (throw-with-msg "LocaleValue can only be used with Worker_Locale QualificationTypeId")

    (and (some #{"EqualTo" "NotEqualTo"} [(params :Comparator)])
         (sequential? (params :LocaleValue)))
    (throw-with-msg (str "Expected a single LocaleValue for Comparator " (:Comparator params)))

    (and (some #{"In" "NotIn"} [(params :Comparator)])
         (> (count (params :LocaleValue)) 30))
    (throw-with-msg "Can use up to only 30 LocaleValue elements using the In or the NotIn Comparator")))

(defn- validate-qualification-requirement-no-values
  [params]
  (try (s/validate exists-comparators (:Comparator params))
       (catch clojure.lang.ExceptionInfo e
         (throw-with-msg (str "Missing IntegerValue or LocaleValue for the " (:Comparator params) " Comparator")))))

(defn validate-qualification-requirement
  [params]
  (if (sequential? params)
    (if (every? some? (map validate-qualification-requirement params))
      params
      (throw-with-msg "Unknown error in qualification requirement"))

    (do (s/validate QualificationRequirement params)

        (when (and (:IntegerValue params) (:LocaleValue params))
          (throw-with-msg "Both IntegerValue and LocaleValue cannot be present at the same time"))

        (case (some #{:IntegerValue :LocaleValue} (keys params))
          :IntegerValue (validate-qualification-requirement-with-integer-value params)
          :LocaleValue (validate-qualification-requirement-with-locale-value params)

          (validate-qualification-requirement-no-values params))

        params)))

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

(defonce MapEntry
  {:Key s/Str
   :Value (maybe-sequential s/Str)})

(defonce Parameter
  {:Key s/Str
   (s/optional-key :Value) s/Any
   (s/optional-key :MapEntry) (maybe-sequential MapEntry)})

(defonce HITReviewPolicy
  {:PolicyName s/Str
   :Parameter (maybe-sequential Parameter)})

(defonce CreateHIT
  {:HITTypeId s/Str
   (s/optional-key :Question) s/Str
   (s/optional-key :HITLayoutId) s/Str
   (s/optional-key :HITLayoutParameter) (maybe-sequential HITLayoutParameter)
   :LifetimeInSeconds s/Int
   (s/optional-key :MaxAssignments) s/Int
   (s/optional-key :AssignmentReviewPolicy) HITReviewPolicy
   (s/optional-key :HITReviewPolicy) HITReviewPolicy
   (s/optional-key :RequesterAnnotation) s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce notification-event-type
  (s/enum "AssignmentAccepted" "AssignmentAbandoned" "AssignmentReturned" "AssignmentSubmitted" "AssignmentRejected" "AssignmentApproved" "HITCreated" "HITExtended" "HITDisposed" "HITReviewable" "HITExpired" "Ping"))

(defonce Notification
  {:Destination s/Str
   :Transport (s/enum "Email" "SQS")
   :Version s/Str
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
   (s/optional-key :Test) s/Str
   (s/optional-key :AnswerKey) s/Str
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

(defonce GetBonusPayments
  {(s/optional-key :HITId) s/Str
   (s/optional-key :AssignmentId) s/Str
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetFileUploadURL
  {:AssignmentId s/Str
   :QuestionIdentifier s/Str})

(defonce GetRequesterStatistic
  {:Statistic s/Str
   :TimePeriod (s/enum "OneDay" "SevenDays" "ThirtyDays" "LifeToDate")
   (s/optional-key :Count) s/Int})

(defonce GetRequesterWorkerStatistic
  {:Statistic s/Str
   :WorkerId s/Str
   :TimePeriod (s/enum "OneDay" "SevenDays" "ThirtyDays" "LifeToDate")
   (s/optional-key :Count) s/Int})

(defonce GrantBonus
  {:WorkerId s/Str
   :AssignmentId s/Str
   :BonusAmount Price
   :Reason s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce SendTestEventNotification
  {:Notification Notification
   :TestEventType notification-event-type})

(defonce GetAssignment
  {:AssignmentId s/Str})

(defonce ApproveAssignment
  {:AssignmentId s/Str
   (s/optional-key :RequesterFeedback) s/Str})

(defonce ApproveRejectedAssignment
  {:ApproveAssignment s/Str
   (s/optional-key :RequesterFeedback) s/Str})

(defonce RejectAssignment
  {:ApproveAssignment s/Str
   (s/optional-key :RequesterFeedback) s/Str})

(defonce HITIdOnly
  {:HITId s/Str})

(defonce ChangeHITTypeOfHIT
  {:HITId s/Str
   :HITTypeId s/Str})
