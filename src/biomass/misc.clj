(ns biomass.misc
  (:require [biomass.builder.schemas :as schemas]))

(def misc-operations
  {:GetAccountBalance {:op-string "GetAccountBalance"}

   :GetBonusPayments {:op-string "GetBonusPayments"
                      :schema schemas/GetBonusPayments}

   :GetFileUploadURL {:op-string "GetFileUploadURL"
                      :schema schemas/GetFileUploadURL}

   :GetRequesterStatistic {:op-string "GetRequesterStatistic"
                           :schema schemas/GetRequesterStatistic}

   :GetRequesterWorkerStatistic {:op-string "GetRequesterWorkerStatistic"
                                 :schema schemas/GetRequesterWorkerStatistic}

   :GrantBonus {:op-string "GrantBonus"
                :schema schemas/GrantBonus}

   :SendTestEventNotification {:op-string "SendTestEventNotification"
                               :schema schemas/SendTestEventNotification}})
