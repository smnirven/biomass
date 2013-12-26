(ns biomass.core
  (:require [biomass.request :refer :all]))

(defn get-account-balance
  []
  (send-request {:Operation "GetAccountBalance"}))
