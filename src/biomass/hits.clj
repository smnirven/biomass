(ns ^{:author "smnirven"
      :doc "Contains methods for making HITs API requests to MTurk"}
  biomass.hits
  (:require [biomass.request :refer :all]
            [biomass.response.hits :refer :all]
            [biomass.util :as util]))

(defn- send-and-parse
  [operation params]
  (let [resp (send-request operation params)]
    (when (= (:status resp) 200)
      (parse operation (:body resp)))))

(defn create-hit-with-type-id
  [{:keys [hit-type-id hit-layout-id assignment-duration
           lifetime layout-params] :as params}]
  {:pre [(string? hit-type-id)
         (string? hit-layout-id)
         (integer? assignment-duration)
         (integer? lifetime)
         (map? layout-params)]}
  (send-and-parse "CreateHIT" (merge {:HITTypeId hit-type-id
                                      :HITLayoutId hit-layout-id
                                      :AssignmentDurationInSeconds assignment-duration
                                      :LifetimeInSeconds lifetime}
                                     (util/restify-layout-params layout-params))))

(defn- create-hit-with-property-values
  [params]
  (throw (java.lang.UnsupportedOperationException. "Creating a HIT without hit-type-id is not supported yet")))

(defn get-hit
  [hit-id]
  (send-and-parse "GetHIT" {:HITId hit-id}))

(defn get-reviewable-hits
  []
  (send-and-parse "GetReviewableHITs" {}))

(defn search-hits
  []
  (send-and-parse "SearchHITs" {}))

(defn get-hits-for-qualification-type
  [{:keys [qualification-type-id page-size page-number] :or {page-size 10 page-number 0}}]
  (send-request {:Operation "GetHITsForQualificationType"
                 :PageSize page-size
                 :PageNumber page-number}))

(defn register-hit-type
  [{:keys [title description reward-amount reward-currency assignment-duration]} qualification-requirements]
  {:pre [(string? title) (not (empty? title))
         (string? description) (not (empty? description))
         (float? reward-amount)
         (string? reward-currency)
         (integer? assignment-duration)]}
  (let [params (merge {:Title title
                       :Description description
                       :Reward.1.Amount reward-amount
                       :Reward.1.CurrencyCode reward-currency
                       :AssignmentDurationInSeconds assignment-duration} qualification-requirements)]
    (send-and-parse "RegisterHITType" params)))

(defn create-hit
  [{:keys [hit-type-id] :as params}]
  (if-not (empty? hit-type-id)
    (create-hit-with-type-id params)
    (create-hit-with-property-values params)))

(defn disable-hit
  [hit-id]
  (send-and-parse "DisableHIT" {:HITId hit-id}))

(defn dispose-hit
  [hit-id]
  (send-and-parse "DisposeHIT" {:HITId hit-id}))
