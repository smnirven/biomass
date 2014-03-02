(ns biomass.response.hits
  (:require [clojure.string :as str]
            [clj-xpath.core :refer :all]
            [biomass.util :as util]))

(defn- parse-single-hit
  [doc]
  {:hit-id ($x:text? "HITId" doc)
   :hit-type-id ($x:text? "HITTypeId" doc)
   :hit-group-id ($x:text? "HITGroupId" doc)
   :hit-layout-id ($x:text? "HITLayoutId" doc)
   :creation-time (util/nil-or-datetime ($x:text? "CreationTime" doc))
   :title ($x:text? "Title" doc)
   :description ($x:text? "Description" doc)
   :keywords ($x:text? "Keywords" doc)
   :hit-status ($x:text? "HITStatus" doc)
   :max-assignments (util/nil-or-integer ($x:text? "MaxAssignments" doc))
   :reward {:amount (util/nil-or-double ($x:text? "Reward/Amount" doc))
            :currency-code ($x:text? "Reward/CurrencyCode" doc)
            :formatted-price ($x:text? "Reward/FormattedPrice" doc)}
   :auto-approval-delay-in-seconds (util/nil-or-integer
                                    ($x:text? "AutoApprovalDelayInSeconds" doc))
   :expiration (util/nil-or-datetime ($x:text? "Expiration" doc))
   :assignment-duration-in-seconds (util/nil-or-integer
                                    ($x:text? "AssignmentDurationInSeconds" doc))
   :requester-annotation ($x:text? "RequesterAnnotation" doc)
   :hit-review-status ($x:text? "HITReviewStatus" doc)
   :number-of-assignments-pending (util/nil-or-integer
                                   ($x:text? "NumberOfAssignmentsPending" doc))
   :number-of-assignments-available (util/nil-or-integer
                                     ($x:text? "NumberOfAssignmentsAvailable" doc))
   :number-of-assignments-completed (util/nil-or-integer
                                     ($x:text? "NumberOfAssignmentsCompleted" doc))})

(defmulti parse-by-operation :operation)

(defmethod parse-by-operation "GetHIT"
  [{:keys [doc]}]
  (let [base-xpath "/GetHITResponse"]
    {:request {:request-id ($x:text? (str base-xpath
                                          "/OperationRequest/RequestId")
                                     doc)}
     :hits (map #(parse-single-hit %) ($x:node* "/GetHITResponse/HIT" doc))}))

(defmethod parse-by-operation "GetReviewableHITs"
  [{:keys [doc]}]
  (let [base-xpath "/GetReviewableHITsResponse"
        result "/GetReviewableHITsResult"]
    {:request {:request-id ($x:text? (str base-xpath
                                          "/OperationRequest/RequestId")
                                     doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid")
                                                               doc))}
              :num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/NumResults")
                                                          doc))
              :total-num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                                     result
                                                                     "/TotalNumResults")
                                                                doc))
              :page-number (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/PageNumber")
                                                          doc))
              :hits (map #(parse-single-hit %)
                         ($x:node* (str base-xpath
                                        result
                                        "/HIT")
                                   doc))}}))

(defmethod parse-by-operation "SearchHITs"
  [{:keys [doc]}]
  (let [base-xpath "/SearchHITsResponse"
        result "/SearchHITsResult"]
    {:request {:request-id ($x:text? (str base-xpath "/OperationRequest/RequestId") doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid")
                                                               doc))}
              :num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/NumResults")
                                                          doc))
              :total-num-results (util/nil-or-integer ($x:text? (str base-xpath
                                                                     result
                                                                     "/TotalNumResults")
                                                                doc))
              :page-number (util/nil-or-integer ($x:text? (str base-xpath
                                                               result
                                                               "/PageNumber")
                                                          doc))
              :hits (map #(parse-single-hit %)
                         ($x:node* (str base-xpath result "/HIT")
                                   doc))}}))

(defmethod parse-by-operation "DisableHIT"
  [{:keys [doc]}]
  (let [base-xpath "/DisableHITResponse/DisableHITResult"]
    {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                           "/Request/IsValid")
                                                      doc))}}))

(defmethod parse-by-operation "DisposeHIT"
  [{:keys [doc]}]
  (let [base-xpath "/DisposeHITResponse/DisposeHITResult"]
    {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                           "/Request/IsValid")
                                                      doc))}}))

(defmethod parse-by-operation "RegisterHITType"
  [{:keys [doc xml]}]
  (let [base-xpath "/RegisterHITTypeResponse"
        result "/RegisterHITTypeResult"]
    {:request {:request-id ($x:text? (str base-xpath
                                          "/OperationRequest/RequestId")
                                     doc)}
     :result {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                    result
                                                                    "/Request/IsValid")
                                                               doc))}
              :hit-type-id ($x:text? (str base-xpath
                                          result
                                          "/HITTypeId")
                                     doc)}}))

(defmethod parse-by-operation "CreateHIT"
  [{:keys [doc xml]}]
  (let [base-xpath "/CreateHITResponse"]
    {:request {:request-id ($x:text? (str base-xpath "/OperationRequest/RequestId") doc)}
     :hit {:request {:valid? (util/nil-or-boolean ($x:text? (str base-xpath
                                                                 "/HIT/Request/IsValid")
                                                            doc))}
           :hit-id ($x:text? (str base-xpath "/HIT/HITId") doc)
           :hit-type-id ($x:text? (str base-xpath "/HIT/HITTypeId") doc)}}))

(defn parse
  [operation xml-response-body]
  (parse-by-operation {:operation operation
                       :doc (xml->doc xml-response-body)
                       :xml xml-response-body}))
