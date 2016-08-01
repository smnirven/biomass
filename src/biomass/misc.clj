(ns biomass.misc
  (:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn get-account-balance
  []
  (send-and-parse "GetAccountBalance" {}))

(defn get-bonus-payments
  [params]
  (send-and-parse "GetBonusPayments" (s/validate schemas/GetBonusPayments params)))

(defn get-file-upload-url
  [params]
  (send-and-parse "GetFileUploadURL" (s/validate schemas/GetFileUploadURL params)))

(defn get-requester-statistic
  [params]
  (send-and-parse "GetRequesterStatistic" (s/validate schemas/GetRequesterStatistic params)))

(defn get-requester-worker-statistic
  [params]
  (send-and-parse "GetRequesterWorkerStatistic" (s/validate schemas/GetRequesterWorkerStatistic params)))

(defn grant-bonus
  [params]
  (send-and-parse "GrantBonus" (s/validate schemas/GrantBonus params)))

(defn send-test-event-notification
  [params]
  (send-and-parse "SendTestEventNotification" (s/validate schemas/SendTestEventNotification params)))
