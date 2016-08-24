(ns biomass.test.helpers
  (:require [biomass.util :refer [find-in-response-with-path]]))

(defn hit-type-id-from-response
  [response]
  (->> [response]
       (find-in-response-with-path [:response :RegisterHITTypeResponse  :RegisterHITTypeResult :HITTypeId])
       first
       :HITTypeId
       first))

(defn hit-ids-from-search-hit-response
  [response]
  (->> [response]
       (find-in-response-with-path [:response :SearchHITsResponse :SearchHITsResult :HIT :HITId])
       (map :HITId)
       (map first)))

(defn hit-ids-from-hits-for-qualification-type-response
  [response]
  (->> [response]
       (find-in-response-with-path [:response :GetHITsForQualificationTypeResponse :GetHITsForQualificationTypeResult :HIT :HITId])
       (map :HITId)
       (map first)))

(defn hit-id-from-create-hit-response
  [response]
  (->> [response]
       (find-in-response-with-path [:response :CreateHITResponse :HIT :HITId])
       first
       :HITId
       first))

(defn worker-ids-from-get-blocked-workers-response
  [response]
  (->> [response]
       (find-in-response-with-path [:response :GetBlockedWorkersResponse :GetBlockedWorkersResult :WorkerBlock :WorkerId])
       (map :WorkerId)
       (map first)))

(defn valid?
  [response path]
  (->> [response]
       (find-in-response-with-path path)
       first
       :IsValid
       first))
