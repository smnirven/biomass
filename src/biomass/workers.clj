(ns biomass.workers
(:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn block-worker
  [params]
  (send-and-parse "BlockWorker" (s/validate schemas/BlockWorker params)))

(defn get-blocked-workers
  [params]
  (send-and-parse "GetBlockedWorkers" (s/validate schemas/GetBlockedWorkers params)))

(defn notify-workers
  [params]
  (send-and-parse "NotifyWorkers" (s/validate schemas/NotifyWorkers params)))

(defn unblock-worker
  [params]
  (send-and-parse "UnblockWorker" (s/validate schemas/UnblockWorker params)))
