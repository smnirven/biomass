(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.builder :as builder]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn validate-qualification-if-exists
  [params]
  (when (some? (:QualificationRequirement params))
    (schemas/validate-qualification-requirement (:QualificationRequirement params)))
  params)

(defn get-hit
  [params]
  (send-and-parse "GetHIT" (s/validate schemas/HITIdOnly params)))

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
  (send-and-parse "GetHITsForQualificationType" (s/validate schemas/GetHITsForQualificationType params)))

(defn register-hit-type
  [params]
  (send-and-parse "RegisterHITType" (builder/->amazon-format (s/validate schemas/RegisterHITType
                                                                         (validate-qualification-if-exists params)))))

(defn create-hit
  [params]
  (send-and-parse "CreateHIT" (builder/->amazon-format (s/validate schemas/CreateHIT
                                                                   (validate-qualification-if-exists params)))))

(defn disable-hit
  [params]
  (send-and-parse "DisableHIT" (s/validate schemas/HITIdOnly params)))

(defn dispose-hit
  [params]
  (send-and-parse "DisposeHIT" (s/validate schemas/HITIdOnly params)))

(defn change-hit-type-of-hit
  [params]
  (send-and-parse "ChangeHITTypeOfHIT" (s/validate schemas/ChangeHITTypeOfHIT params)))

(defn extend-hit
  [params]
  (send-and-parse "ExtendHIT" (s/validate schemas/ExtendHIT params)))

(defn force-expire-hit
  [params]
  (send-and-parse "ForceExpireHIT" (s/validate schemas/HITIdOnly params)))

(defn get-review-results-for-hit
  [params]
  (send-and-parse "GetReviewResultsForHIT" (s/validate schemas/GetReviewResultsForHIT params)))

(defn set-hit-as-reviewing
  [params]
  (send-and-parse "SetHITAsReviewing" (s/validate schemas/SetHITAsReviewing params)))

(defn set-hit-type-notification
  [params]
  (send-and-parse "SetHITTypeNotification" (builder/->amazon-format (s/validate schemas/SetHITTypeNotification params))))
