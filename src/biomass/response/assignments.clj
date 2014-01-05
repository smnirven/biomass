(ns biomass.response.assignments
  (:require [clojure.string :as str]
            [clj-xpath.core :refer :all]
            [biomass.util :as util]))

(defn- parse-single-assignment
  [doc]
  {:assignment-id ($x:text? "AssignmentId" doc)
   :worker-id ($x:text? "WorkerId" doc)
   :hit-id ($x:text? "HITId" doc)
   :assignment-status ($x:text? "AssignmentStatus" doc)
   :auto-approval-time (util/nil-or-datetime ($x:text? "AutoApprovalTime" doc))
   :accept-time (util/nil-or-datetime ($x:text? "AcceptTime" doc))
   :submit-time (util/nil-or-datetime ($x:text? "SubmitTime" doc))
   :answer ($x:text? "Answer" doc)}) ;;TODO: WTF should I do with the
;;answer XML?

(defmulti parse-by-operation #(:operation %))

(defmethod parse-by-operation "GetAssignmentsForHIT"
  [{:keys [doc xml]}]
  (let [base-xpath "/GetAssignmentsForHITResponse"
        result "/GetAssignmentsForHITResult"]
    {:request {:request-id ($x:text? (str base-xpath "/OperationRequest/RequestId") doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid") doc))}
              :num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/NumResults") doc))
              :total-num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/TotalNumResults") doc))
              :page-number (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/PageNumber") doc))
              :assignments (map #(parse-single-assignment %)
                                ($x:node* (str base-xpath
                                               result
                                               "/Assignment") doc))}}))

(defmethod parse-by-operation "GetAssignment"
  [{:keys [doc xml]}]
  (let [base-xpath "/GetAssignmentResponse"
        result "/GetAssignmentResult"]
    {:request {:request-id ($x:text? (str base-xpath "/OperationRequest/RequestId") doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid") doc))}
              :assignment (parse-single-assignment ($x:node? (str base-xpath
                                                                  result
                                                                  "/Assignment") doc))
              :hit {:hit-id ($x:text? (str base-xpath
                                           result
                                           "/HIT/HITId") doc)
                    :hit-type-id ($x:text? (str base-xpath
                                                result
                                                "/HIT/HITTypeId") doc)}}}))

(defmethod parse-by-operation "ApproveAssignment"
  [{:keys [doc xml]}]
  (let [base-xpath "/ApproveAssignmentResponse"
        result "/ApproveAssignmentResult"]
    {:request {:request-id ($x:text? (str base-xpath "/OperationRequest/RequestId") doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid") doc))}}}))

(defn parse
  [operation xml-response-body]
  (parse-by-operation {:operation operation
                       :xml xml-response-body
                       :doc (xml->doc xml-response-body)}))
