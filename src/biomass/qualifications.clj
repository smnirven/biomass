(ns biomass.qualifications
  (:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn assign-qualification
  [params]
  (send-and-parse "AssignQualification" (s/validate schemas/AssignQualification params)))

(defn create-qualification-type
  [params]
  (send-and-parse "CreateQualificationType" (s/validate schemas/CreateQualificationType params)))

(defn dispose-qualification-type
  [params]
  (send-and-parse "DisposeQualificationType" (s/validate schemas/DisposeQualificationType params)))

(defn get-qualification-for-qualification-type
  [params]
  (send-and-parse "GetQualificationsForQualificationType" (s/validate schemas/GetQualificationsForQualificationType params)))

(defn get-qualification-requests
  [params]
  (send-and-parse "GetQualificationRequests" (s/validate schemas/GetQualificationRequests params)))

(defn get-qualification-score
  [params]
  (send-and-parse "GetQualificationScore" (s/validate schemas/GetQualificationScore params)))

(defn get-qualification-type
  [params]
  (send-and-parse "GetQualificationType" (s/validate schemas/GetQualificationType params)))

(defn grant-qualification
  [params]
  (send-and-parse "GrantQualification" (s/validate schemas/GrantQualification params)))

(defn reject-qualification-request
  [params]
  (send-and-parse "RejectQualificationRequest" (s/validate schemas/RejectQualificationRequest params)))

(defn revoke-qualification
  [params]
  (send-and-parse "RevokeQualification" (s/validate schemas/RevokeQualification params)))

(defn search-qualification-types
  [params]
  (send-and-parse "RevokeQualification" (s/validate schemas/RevokeQualification params params)))

(defn update-qualification-score
  [params]
  (send-and-parse "UpdateQualificationScore" (s/validate schemas/UpdateQualificationScore params)))

(defn update-qualification-type
  [params]
  (send-and-parse "UpdateQualificationType" (s/validate schemas/UpdateQualificationType params)))
