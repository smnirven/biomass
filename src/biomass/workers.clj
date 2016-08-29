(ns biomass.workers
  (:require [biomass.builder.schemas :as schemas]))

(def worker-operations
  {:BlockWorker {:op-string "BlockWorker"
                 :schema schemas/BlockWorker}

   :GetBlockedWorkers {:op-string "GetBlockedWorkers"
                       :schema schemas/GetBlockedWorkers}

   :NotifyWorkers {:op-string "NotifyWorkers"
                   :schema schemas/NotifyWorkers}

   :UnblockWorker {:op-string "UnblockWorker"
                   :schema schemas/UnblockWorker}})
