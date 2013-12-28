(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer :all]))


(defn get-hit
  [hit-id]
  (send-request {:Operation "GetHIT" :HITId hit-id}))

(defn get-reviewable-hits
  []
  (send-request {:Operation "GetReviewableHITs"}))

(defn get-hits-for-qualification-type
  [{:keys [qualification-type-id page-size page-number]}]
  ;;TODO: defaults for optional page-size and page-number params
  (send-request {:Operation "GetHITsForQualificationType"
                 :PageSize page-size
                 :PageNumber page-number}))

(defn create-hit
  [{:keys [hit-type-id]}]
  (send-request {:Operation "CreateHIT"
                 :HITTypeId hit-type-id}))
