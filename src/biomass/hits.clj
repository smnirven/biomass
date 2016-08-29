(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.builder.schemas :as schemas]))

(defn validate-qualification-if-exists
  [params]
  (when (some? (:QualificationRequirement params))
    (schemas/validate-qualification-requirement (:QualificationRequirement params)))
  params)

(def hit-operations
  {:GetHIT {:op-string "GetHIT"
            :schema schemas/HITIdOnly}

   :GetReviewableHITs {:op-string "GetReviewableHITs"
                       :schema schemas/GetReviewableHITs}

   :SearchHITs {:op-string "SearchHITs"
                :schema schemas/SearchHITs}

   :GetHITsForQualificationType {:op-string "GetHITsForQualificationType"
                                 :schema schemas/GetHITsForQualificationType}

   :RegisterHITType {:op-string "RegisterHITType"
                     :schema schemas/RegisterHITType
                     :validator validate-qualification-if-exists}

   :CreateHIT {:op-string "CreateHIT"
               :schema schemas/CreateHIT
               :validator biomass.hits/validate-qualification-if-exists}

   :DisableHIT {:op-string "DisableHIT"
                :schema schemas/HITIdOnly}

   :DisposeHIT {:op-string "DisposeHIT"
                :schema schemas/HITIdOnly}

   :ChangeHITTypeOfHIT {:op-string "ChangeHITTypeOfHIT"
                        :schema schemas/ChangeHITTypeOfHIT}

   :ExtendHIT {:op-string "ExtendHIT"
               :schema "schemas/ExtendHIT"}

   :ForceExpireHIT {:op-string "ForceExpireHIT"
                    :schema schemas/HITIdOnly}

   :GetReviewResultsForHIT {:op-string "GetReviewResultsForHIT"
                            :schema schemas/GetReviewResultsForHIT}

   :SetHITAsReviewing {:op-string "SetHITAsReviewing"
                       :schema schemas/SetHITAsReviewing}

   :SetHITTypeNotification {:op-string "SetHITTypeNotification"
                            :schema schemas/SetHITTypeNotification}})
