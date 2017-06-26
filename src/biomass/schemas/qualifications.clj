(ns biomass.schemas.qualifications
  (:require [schema.core :as s]
            [biomass.schemas.schemas :as schemas]))

(defonce Locale
  ;; TODO Subdivisions
  {:Country (schemas/maybe-sequential s/Str)})

(defonce QualificationRequirement
  {:QualificationTypeId s/Str
   :Comparator s/Str
   (s/optional-key :IntegerValue) (schemas/maybe-sequential s/Int)
   (s/optional-key :LocaleValue) (schemas/maybe-sequential Locale)
   (s/optional-key :RequiredToPreview) s/Bool})

(defonce DisposeQualificationType
  {:QualificationTypeId s/Str})

(defonce GetQualificationRequests
  {(s/optional-key :QualificationTypeId) s/Str
   (s/optional-key :SortProperty) (s/enum "QualificationTypeId" "SubmitTime")
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetQualificationsForQualificationType
  {:QualificationTypeId s/Str
   (s/optional-key :Status) (s/enum "Granted" "Revoked")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetQualificationScore
  {:QualificationTypeId s/Str
   :SubjectId s/Str})

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

(defonce GetQualificationType
  {:QualificationTypeId s/Str})

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

(defonce RevokeQualification
  {:QualificationTypeId s/Str
   :SubjectId s/Str
   (s/optional-key :Reason) s/Str})

(defonce GrantQualification
  {:QualificationRequestId s/Str
   (s/optional-key :IntegerValue) s/Int})

(defonce SearchQualificationTypes
  {(s/optional-key :Query) s/Str
   (s/optional-key :SortProperty) (s/enum "QualificationTypeId" "SubmitTime")
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int
   :MustBeRequestable s/Bool
   (s/optional-key :MustBeOwnedByCaller) s/Bool})

(defonce RejectQualificationRequest
  {:QualificationRequestId s/Str
   (s/optional-key :Reason) s/Str})

(defonce AssignQualification
  {:QualificationTypeId s/Str
   :WorkerId s/Str
   :IntegerValue s/Int
   :SendNotification s/Bool})

(defonce UpdateQualificationScore
  {:QualificationTypeId s/Str
   :SubjectId s/Str
   :IntegerValue s/Int})
