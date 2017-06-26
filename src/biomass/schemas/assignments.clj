(ns biomass.schemas.assignments
  (:require [schema.core :as s]))

(defonce GetAssignmentsForHIT
  {:HITId s/Str
   (s/optional-key :AssignmentStatus) (s/enum "Submitted" "Approved" "Rejected")
   (s/optional-key :SortProperty) s/Str
   (s/optional-key :SortDirection) (s/enum "Ascending" "Descending")
   (s/optional-key :PageSize) s/Int
   (s/optional-key :PageNumber) s/Int})

(defonce GetAssignment
  {:AssignmentId s/Str})

(defonce ApproveAssignment
  {:AssignmentId s/Str
   (s/optional-key :RequesterFeedback) s/Str})

(defonce ApproveRejectedAssignment
  {:ApproveAssignment s/Str
   (s/optional-key :RequesterFeedback) s/Str})

(defonce RejectAssignment
  {:ApproveAssignment s/Str
   (s/optional-key :RequesterFeedback) s/Str})
