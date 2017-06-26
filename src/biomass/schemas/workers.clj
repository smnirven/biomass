(ns biomass.schemas.workers
  (:require [schema.core :as s]
            [biomass.schemas.schemas :as schemas]))

(defonce BlockWorker
  {:WorkerId s/Str
   :Reason s/Str})

(defonce GetBlockedWorkers
  {(s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce NotifyWorkers
  {:Subject s/Str
   :MessageText s/Str
   :WorkerId (schemas/maybe-sequential s/Str)})

(defonce UnblockWorker
  {:WorkerId s/Str
   :Reason s/Str})
