(ns biomass.assignments
  (:require [biomass.schemas.assignments :as assignment-schemas]))

(def assignments-operations
  {:GetAssignmentsForHIT {:op-string "GetAssignmentsForHIT"
                          :schema assignment-schemas/GetAssignmentsForHIT}

   :GetAssignment {:op-string "GetAssignment"
                   :schema assignment-schemas/GetAssignment}

   :ApproveAssignment {:op-string "ApproveAssignment"
                       :schema assignment-schemas/ApproveAssignment}

   :ApproveRejectedAssignment {:op-string "ApproveRejectedAssignment"
                               :schema assignment-schemas/ApproveRejectedAssignment}

   :RejectAssignment {:op-string "RejectAssignment"
                      :schema assignment-schemas/RejectAssignment}})
