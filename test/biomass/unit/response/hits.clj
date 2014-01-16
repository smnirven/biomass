(ns biomass.unit.response.hits
  (:require [midje.sweet :refer :all]
            [clojure.java.io :as io]
            [biomass.response.hits :refer [parse]]))

(def $get-hit-response (slurp (io/resource "get_hit_example_response.xml")))

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
