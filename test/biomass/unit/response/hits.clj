(ns biomass.unit.response.hits
  (:require [midje.sweet :refer :all]
            [clojure.java.io :as io]
            [biomass.response.hits :refer [parse]]))

(def $get-hit-response (slurp (io/resource "get_hit_example_response.xml")))
(def $search-hits-response (slurp (io/resource "search_hits_example_response.xml")))

(fact "GetHIT response is parsed correctly"
  (parse "GetHIT" $get-hit-response)
  => (just {:request {:request-id "ff72a6c9-33f5-477d-8cd9-c9a43708c965"}
            :hits (just (contains {:assignment-duration-in-seconds 3600
                                   :auto-approval-delay-in-seconds 604800
                                   :creation-time truthy
                                   :description "Given the restaurant information, find the URL for their official website"
                                   :hit-group-id "2C4PHMVHVKCNPJ00QUVJA9LR0B835B"
                                   :hit-id "25DR14IXKO2PYKGU0J1603YOPE3ABI"
                                   :hit-layout-id "2N933FDEY5CSLIKWFMU8FRFLC8M1SF"
                                   :hit-review-status "NotReviewed"
                                   :keywords "data collection, restaurants, listings"
                                   :max-assignments 3
                                   :number-of-assignments-available nil
                                   :number-of-assignments-pending nil
                                   :requester-annotation "BatchId:62636;"
                                   :reward (just {:amount 0.02 :currency-code "USD"
                                                  :formatted-price "$0.02"})
                                   :title "Find the Website Address for Restaurants"}))}))






(fact "SearchHITs response is parsed correctly"
  (parse "SearchHITs" $search-hits-response)
  => (just {:request {:request-id "54f2f090-8e6d-46f0-9cf9-0ce38acaa211"}
            :result (contains {:request (just {:valid? true})
                               :num-results 1
                               :page-number 1
                               :total-num-results 1
                               :hits (just [(contains {:hit-id "25DR14IXKO2PYKGU0J1603YOPE3ABI"
                                                       :hit-type-id "2FYPQJQ9OPLPMU6AAEOGBKCOZ9F1ZT"
                                                       :hit-group-id "2C4PHMVHVKCNPJ00QUVJA9LR0B835B"
                                                       :hit-layout-id "2N933FDEY5CSLIKWFMU8FRFLC8M1SF"
                                                       :title "Find the Website Address for Restaurants"
                                                       :description "Given the restaurant information, find the URL for their official website"
                                                       :keywords "data collection, restaurants, listings"
                                                       :hit-status "Assignable"
                                                       :max-assignments 3
                                                       :reward (just {:amount 0.02
                                                                      :currency-code "USD"
                                                                      :formatted-price "$0.02"})
                                                       :auto-approval-delay-in-seconds 604800
                                                       :assignment-duration-in-seconds 3600
                                                       :requester-annotation "BatchId:62636;"
                                                       :hit-review-status "NotReviewed"
                                                       :number-of-assignments-pending 0
                                                       :number-of-assignments-available 2
                                                       :number-of-assignments-completed 0})])})}))
