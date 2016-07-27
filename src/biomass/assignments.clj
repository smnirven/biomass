(ns biomass.assignments
  (:require [biomass.request :refer [send-and-parse]]
            [biomass.builder.schemas :as schemas]
            [schema.core :as s]))

(defn get-assignments-for-hit
  [params]
  (send-and-parse "GetAssignmentsForHIT" (s/validate schemas/GetAssignmentsForHIT params)))

(defn get-assignment
  [params]
  (send-and-parse "GetAssignment" (s/validate schemas/GetAssignment params)))

(defn approve-assignment
  [params]
  (send-and-parse "ApproveAssignment" (s/validate schemas/ApproveAssignment params)))

(defn approve-rejected-assignment
  [params]
  (send-and-parse "ApproveRejectedAssignment" (s/validate schemas/ApproveRejectedAssignment params)))

(defn reject-assignment
  [params]
  (send-and-parse "RejectAssignment" (s/validate schemas/RejectAssignment params)))
