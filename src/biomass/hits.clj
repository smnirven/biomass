(ns biomass.hits
  (:require [biomass.schemas.hits :as hits-schemas]
            [biomass.qualifications :as qualifications]))

(defn validate-qualification-if-exists
  [params]
  (when (some? (:QualificationRequirement params))
    (qualifications/validate-qualification-requirement (:QualificationRequirement params)))
  params)

(def hit-operations
  {:GetHIT {:op-string "GetHIT"
            :schema hits-schemas/HITIdOnly}

   :GetReviewableHITs {:op-string "GetReviewableHITs"
                       :schema hits-schemas/GetReviewableHITs}

   :SearchHITs {:op-string "SearchHITs"
                :schema hits-schemas/SearchHITs}

   :GetHITsForQualificationType {:op-string "GetHITsForQualificationType"
                                 :schema hits-schemas/GetHITsForQualificationType}

   :RegisterHITType {:op-string "RegisterHITType"
                     :schema hits-schemas/RegisterHITType
                     :validator validate-qualification-if-exists}

   :CreateHIT {:op-string "CreateHIT"
               :schema hits-schemas/CreateHIT
               :validator validate-qualification-if-exists}

   :DisableHIT {:op-string "DisableHIT"
                :schema hits-schemas/HITIdOnly}

   :DisposeHIT {:op-string "DisposeHIT"
                :schema hits-schemas/HITIdOnly}

   :ChangeHITTypeOfHIT {:op-string "ChangeHITTypeOfHIT"
                        :schema hits-schemas/ChangeHITTypeOfHIT}

   :ExtendHIT {:op-string "ExtendHIT"
               :schema hits-schemas/ExtendHIT}

   :ForceExpireHIT {:op-string "ForceExpireHIT"
                    :schema hits-schemas/HITIdOnly}

   :GetReviewResultsForHIT {:op-string "GetReviewResultsForHIT"
                            :schema hits-schemas/GetReviewResultsForHIT}

   :SetHITAsReviewing {:op-string "SetHITAsReviewing"
                       :schema hits-schemas/SetHITAsReviewing}

   :SetHITTypeNotification {:op-string "SetHITTypeNotification"
                            :schema hits-schemas/SetHITTypeNotification}})
