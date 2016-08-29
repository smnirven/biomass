(ns biomass.assignments
  (:require [biomass.builder.schemas :as schemas]))

(def assignments-operations
  {:GetAssignmentsForHIT {:op-string "GetAssignmentsForHIT"
                          :schema schemas/GetAssignmentsForHIT}

   :GetAssignment {:op-string "GetAssignment"
                   :schema schemas/GetAssignment}

   :ApproveAssignment {:op-string "ApproveAssignment"
                       :schema schemas/ApproveAssignment}

   :ApproveRejectedAssignment {:op-string "ApproveRejectedAssignment"
                               :schema schemas/ApproveRejectedAssignment}

   :RejectAssignment {:op-string "RejectAssignment"
                      :schema schemas/RejectAssignment}})
