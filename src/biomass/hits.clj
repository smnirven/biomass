(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer :all]))


(defn get-reviewable-hits
  []
  (let [op "GetReviewableHITs"]
    (send-request {:Operation op})))
