(ns biomass.workers
  (:require [biomass.schemas.workers :as workers-schemas]))

(def worker-operations
  {:BlockWorker {:op-string "BlockWorker"
                 :schema workers-schemas/BlockWorker}

   :GetBlockedWorkers {:op-string "GetBlockedWorkers"
                       :schema workers-schemas/GetBlockedWorkers}

   :NotifyWorkers {:op-string "NotifyWorkers"
                   :schema workers-schemas/NotifyWorkers}

   :UnblockWorker {:op-string "UnblockWorker"
                   :schema workers-schemas/UnblockWorker}})
