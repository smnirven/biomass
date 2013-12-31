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

(defn- parse-get-hit-response
  [doc]
  {:operation-request {:request-id ($x:text? "/GetHITResponse/OperationRequest/RequestId" doc)}
   :hits (map #(parse-single-hit %) ($x:node* "/GetHITResponse/HIT" doc))})


(defn- parse-get-reviewable-hits-response
  [doc]
  {:operation-request {:request-id ($x:text? "/GetReviewableHITsResponse/OperationRequest/RequestId" doc)}
   :get-reviewable-hits-result {:request {:is-valid ($x:text? "/GetReviewableHITsResponse/GetReviewableHITsResult/Request/IsValid" doc)}
                                :num-results (util/nil-or-integer ($x:text? "/GetReviewableHITsResponse/GetReviewableHITsResult/NumResults" doc))
                                :total-num-results (util/nil-or-integer ($x:text? "/GetReviewableHITsResponse/GetReviewableHITsResult/TotalNumResults" doc))
                                :page-number (util/nil-or-integer ($x:text? "/GetReviewableHITsResponse/GetReviewableHITsResult/PageNumber" doc))
                                :hits (map #(parse-single-hit %)
                                           ($x:node* "/GetReviewableHITsResponse/GetReviewableHITsResult/HIT" doc))}})

(defn- parse-search-hits-response
  [doc]
  {:operation-request {:request-id ($x:text? "/SearchHITsResponse/OperationRequest/RequestId" doc)}
   :search-hits-result {:request {:is-valid ($x:text? "/SearchHITsResponse/SearchHITsResult/Request/IsValid" doc)}
                        :num-results (util/nil-or-integer ($x:text? "/SearchHITsResponse/SearchHITsResult/NumResults" doc))
                        :total-num-results (util/nil-or-integer ($x:text? "/SearchHITsResponse/SearchHITsResult/TotalNumResults" doc))
                        :page-number (util/nil-or-integer ($x:text? "/SearchHITsResponse/SearchHITsResult/PageNumber" doc))
                        :hits (map #(parse-single-hit %)
                                   ($x:node* "/SearchHITsResponse/SearchHITsResult/HIT"
                                             doc))}})

(defn- parse-disable-hit-response
  [doc]
  {:request {:valid (util/nil-or-boolean ($x:text? "/DisableHITResponse/DisableHITResult/Request/IsValid" doc))}})

(defn- parse-dispose-hit-response
  [doc]
  {:request {:valid (util/nil-or-boolean ($x:text? "/DisposeHITResponse/DisposeHITResult/Request/IsValid" doc))}})

(def $OP_TO_PARSER {"GetHIT" parse-get-hit-response
                    "GetReviewableHITs" parse-get-reviewable-hits-response
                    "SearchHITs" parse-search-hits-response
                    "DisableHIT" parse-disable-hit-response
                    "DisposeHIT" parse-dispose-hit-response})

(defn parse
  [operation xml-response-body]
  (let [doc (xml->doc xml-response-body)
        parser (get $OP_TO_PARSER operation)]
    (parser doc)))
