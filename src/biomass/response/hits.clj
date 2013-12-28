(ns biomass.response.hits
  (:require [clj-xpath.core :refer :all]
            [biomass.util :as util]))

(defn- parse-hit
  [doc]
  {:hit-id ($x:text? "/HIT/HITId" doc)
   :hit-type-id ($x:text? "/HIT/HITTypeId" doc)
   :creation-time (util/nil-or-datetime ($x:text? "/HIT/CreationTime" doc))
   :title ($x:text? "/HIT/Title" doc)
   :description ($x:text? "/HIT/Description" doc)
   :hit-status ($x:text? "/HIT/HITStatus" doc)
   :max-assignments ($x:text? "/HIT/MaxAssignments" doc)
   :reward {:amount (util/nil-or-double ($x:text? "/HIT/Reward/Amount" doc))
            :currency-code ($x:text? "/HIT/Reward/CurrencyCode" doc)
            :formatted-price ($x:text? "/HIT/Reward/FormattedPrice" doc)}
   :auto-approval-delay-in-seconds (util/nil-or-integer ($x:text? "/HIT/AutoApprovalDelayInSeconds" doc))
   :expiration (util/nil-or-datetime ($x:text? "/HIT/Expiration" doc))
   :assignment-duration-in-seconds (util/nil-or-integer ($x:text? "/HIT/AssignmentDurationInSeconds" doc))
   :number-of-similar-hits (util/nil-or-integer ($x:text? "/HIT/NumberOfSimilarHITs" doc))
   :hit-review-status ($x:text? "/HIT/HITReviewStatus" doc)})

(defn- parse-get-reviewable-hits-response
  [doc]
  {:request {:is-valid ($x:text? "/GetReviewableHITsResult/Request/IsValid" doc)}
   :num-results (util/nil-or-integer ($x:text? "/GetReviewableHITsResult/NumResults" doc))
   :total-num-results (util/nil-or-integer ($x:text? "/GetReviewableHITsResult/TotalNumResults" doc))
   :page-number (util/nil-or-integer ($x:text? "/GetReviewableHITsResult/PageNumber" doc))
   :hits (map (fn [n] {:HITId ($x:text? "HITId" n)}) ($x:node* "/GetReviewableHITsResult/HIT" doc))})

(defn parse
  [xml-response-body operation]
  (let [doc (xml->doc xml-response-body)]
    (cond
     (= operation "GetHIT") (parse-hit doc)
     (= operation "GetReviewableHITs") (parse-get-reviewable-hits-response doc))))
