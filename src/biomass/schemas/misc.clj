(ns biomass.schemas.misc
  (:require [schema.core :as s]
            [biomass.schemas.schemas :as schemas]))

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
   :BonusAmount schemas/Price
   :Reason s/Str
   (s/optional-key :UniqueRequestToken) s/Str})

(defonce SendTestEventNotification
  {:Notification schemas/Notification
   :TestEventType schemas/notification-event-type})
