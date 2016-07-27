(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.builder :as builder]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn get-hit
  [hit-id]
  (send-and-parse "GetHIT" {:HITId hit-id}))

(defn get-reviewable-hits
  ([]
   (send-and-parse "GetReviewableHITs" {}))
  ([params]
   (send-and-parse "GetReviewableHITs" (builder/->amazon-format (s/validate schemas/GetReviewableHITs params)))))

(defn search-hits
  ([]
   (send-and-parse "SearchHITs" {}))
  ([params]
   (send-and-parse "SearchHITs" (builder/->amazon-format (s/validate schemas/SearchHITs params)))))

(defn get-hits-for-qualification-type
  [params]
  (send-and-parse "GetHITsForQualificationType") (s/validate schemas/GetHITsForQualificationType params))

(defn register-hit-type
  [params]
  (send-and-parse "RegisterHITType" (builder/->amazon-format (s/validate schemas/RegisterHITType params))))

(defn create-hit
  [params]
  (send-and-parse "CreateHIT" (builder/->amazon-format (s/validate schemas/CreateHIT params))))

(defn disable-hit
  [hit-id]
  (send-and-parse "DisableHIT" {:HITId hit-id}))

(defn dispose-hit
  [hit-id]
  (send-and-parse "DisposeHIT" {:HITId hit-id}))

(defn change-hit-type-of-hit
  [hit-id hit-type-id]
  (send-and-parse "ChangeHITTypeOfHIT" {:HITId hit-id
                                        :HITTypeId hit-type-id}))

(defn extend-hit
  [params]
  (send-and-parse "ExtendHIT" (s/validate schemas/ExtendHIT params)))

(defn force-expire-hit
  [hit-id]
  (send-and-parse "ForceExpireHIT" {:HITId hit-id}))

(defn get-assignments-for-hits
  [params]
  (send-and-parse "GetAssignmentsForHIT" (s/validate schemas/GetAssignmentsForHIT params)))

(defn get-review-results-for-hit
  [params]
  (send-and-parse "GetReviewResultsForHIT" (s/validate schemas/GetReviewResultsForHIT params)))

(defn set-hit-as-reviewing
  [params]
  (send-and-parse "SetHITAsReviewing" (s/validate schemas/SetHITAsReviewing params)))

(defn set-hit-type-notification
  [params]
  (send-and-parse "SetHITTypeNotification" (builder/->amazon-format (s/validate schemas/SetHITTypeNotification params))))
