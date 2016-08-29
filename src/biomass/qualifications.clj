(ns biomass.qualifications
  (:require [biomass.builder.schemas :as schemas]))

(def qualifications-operations
  {:DisposeQualificationType {:op-string "DisposeQualificationType"
                              :schema schemas/DisposeQualificationType}

   :GetQualificationRequests {:op-string "GetQualificationRequests"
                              :schema schemas/GetQualificationRequests}

   :GetQualificationsForQualificationType {:op-string "GetQualificationsForQualificationType"
                                           :schema schemas/GetQualificationsForQualificationType}

   :GetQualificationScore {:op-string "GetQualificationScore"
                           :schema schemas/GetQualificationScore}

   :CreateQualificationType {:op-string "CreateQualificationType"
                             :schema schemas/CreateQualificationType}

   :UpdateQualificationType {:op-string "UpdateQualificationType"
                             :schema schemas/UpdateQualificationType}

   :RevokeQualification {:op-string "RevokeQualification"
                         :schema schemas/RevokeQualification}

   :GrantQualification {:op-string "GrantQualification"
                        :schema schemas/GrantQualification}

   :SearchQualificationTypes {:op-string "SearchQualificationTypes"
                              :schema schemas/SearchQualificationTypes}

   :GetQualificationType {:op-string "GetQualificationType"
                          :schema schemas/GetQualificationType}

   :RejectQualificationRequest {:op-string "RejectQualificationRequest"
                                :schema schemas/RejectQualificationRequest}

   :AssignQualification {:op-string "AssignQualification"
                         :schema schemas/AssignQualification}

   :UpdateQualificationScore {:op-string "UpdateQualificationScore"
                              :schema schemas/UpdateQualificationScore}})
