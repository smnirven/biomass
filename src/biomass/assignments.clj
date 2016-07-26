(ns biomass.assignments
  (:require [biomass.request :refer :all]
            [biomass.response.assignments :refer :all]
            [biomass.util :as util]
            [biomass.builder.schemas :refer :all]
            [schema.core :as s]))

(defn- send-and-parse
  [operation params]
  (let [resp (send-request operation params)]
    (when (= (:status resp) 200)
      (parse operation (:body resp)))))

(defn get-assignments-for-hit
  [params]
  (send-and-parse "GetAssignmentsForHIT" (s/validate schema-GetAssignmentsForHIT params)))

(defn get-assignment
  [assignment-id]
  {:pre [(string? assignment-id) (not (empty? assignment-id))]}
  (send-and-parse "GetAssignment" {:AssignmentId assignment-id}))

(defn approve-assignment
  [assignment-id]
  {:pre [(string? assignment-id) (not (empty? assignment-id))]}
  (send-and-parse "ApproveAssignment" {:AssignmentId assignment-id}))

(defn approve-rejected-assignment
  [assignment-id]
  {:pre [(string? assignment-id) (not (empty? assignment-id))]}
  (send-and-parse "ApproveRejectedAssignment" {:AssignmentId assignment-id}))

(defn reject-assignment
  ([assignment-id] (reject-assignment assignment-id {}))
  ([assignment-id {:keys [requester-feedback]}]
     {:pre [(string? assignment-id) (not (empty? assignment-id))]}
     (let [args {:AssignmentId assignment-id}]
       (if-not (empty? requester-feedback)
         (send-and-parse "RejectAssignment"
                         (merge args
                                {:RequesterFeedback requester-feedback}))
         (send-and-parse "RejectAssignment" args)))))
