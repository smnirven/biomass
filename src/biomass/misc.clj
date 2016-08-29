(ns biomass.misc
  (:require [biomass.schemas.misc :as misc-schemas]))

(def misc-operations
  {:GetAccountBalance {:op-string "GetAccountBalance"}

   :GetBonusPayments {:op-string "GetBonusPayments"
                      :schema misc-schemas/GetBonusPayments}

   :GetFileUploadURL {:op-string "GetFileUploadURL"
                      :schema misc-schemas/GetFileUploadURL}

   :GetRequesterStatistic {:op-string "GetRequesterStatistic"
                           :schema misc-schemas/GetRequesterStatistic}

   :GetRequesterWorkerStatistic {:op-string "GetRequesterWorkerStatistic"
                                 :schema misc-schemas/GetRequesterWorkerStatistic}

   :GrantBonus {:op-string "GrantBonus"
                :schema misc-schemas/GrantBonus}

   :SendTestEventNotification {:op-string "SendTestEventNotification"
                               :schema misc-schemas/SendTestEventNotification}})
