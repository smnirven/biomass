(ns biomass.test.integration
  (:require  [clojure.test :refer :all]
             [biomass.request :as r]
             [biomass.hits :as hits]
             [biomass.workers :as workers]
             [clj-time.core :as time]
             [biomass.test.helpers :as h]
             [nomad :refer [defconfig]]
             [clojure.java.io :as io]))

(defn setup-creds [f]
  (let [original-access-key @biomass.request/aws-access-key
        original-secret-key @biomass.request/aws-secret-access-key
        original-sandbox-mode @biomass.request/sandbox-mode]

    (defconfig test-config (io/file "test/config/test-config.edn"))
    (def aws-access-key (get (test-config) :aws-access-key))
    (def aws-secret-key (get (test-config) :aws-secret-key))
    (def worker-id (get (test-config) :worker-id))

    (if (some nil? [aws-access-key aws-secret-key worker-id])
      (throw (RuntimeException. "One or more env variables not set for test"))
      (do
        (r/setup {:AWSAccessKey aws-access-key :AWSSecretAccessKey aws-secret-key :sandbox true})
        (f)))

    (r/setup {:AWSAccessKey original-access-key :AWSSecretAccessKey original-secret-key :sandbox original-sandbox-mode})))

(use-fixtures :once setup-creds)

(deftest test-creds
  (testing "aws-access-key"
    (is (not (nil? aws-access-key)))
    (testing "aws-secret-key"
      (is (not (nil? aws-secret-key)))))

  (testing "check-sandboxed"
    (is (not (nil? @biomass.request/sandbox-mode)))))

(deftest hits
  (def hit-type-id)
  (def hit-id)
  (testing "register hit-type"
    (let [qualification {:QualificationTypeId "00000000000000000071"
                         :Comparator "In"
                         :LocaleValue [{:Country "US"}
                                       {:Country "IN"}
                                       {:Country "GB"}]
                         :RequiredToPreview false}

          register-hit-params {:Title (str "TestHITType" (time/now))
                               :Description "Test generated hittype"
                               :Reward {:Amount 0.50 :CurrencyCode "USD"}
                               :AssignmentDurationInSeconds 600
                               :Keywords "test"
                               :QualificationRequirement qualification}

          hittype-response (r/requester :RegisterHITType register-hit-params)]

      (def hit-type-id (h/hit-type-id-from-response hittype-response))
      (is (= :success (:status hittype-response)))
      (is (= "True" (h/valid? hittype-response [:response :RegisterHITTypeResponse  :RegisterHITTypeResult :Request :IsValid])))))

  (testing "create hit with new hittype"
    (is (not (nil? hit-type-id)))
    (let [question (slurp "test/fixtures/sample-question")

          create-hit-response (r/requester :CreateHIT {:HITTypeId hit-type-id
                                                       :Question question
                                                       :LifetimeInSeconds 6000})]
      (def hit-id h/hit-id-from-create-hit-response)
      (is (= :success (:status create-hit-response)))
      (is (= "True" (h/valid? create-hit-response [:response :CreateHITResponse :HIT :Request :IsValid])))
      (def hit-id (h/hit-id-from-create-hit-response create-hit-response))))

  (testing "search hits for the new hit"
    (is (not (nil? hit-id)))
    (let [search-response (r/requester :SearchHITs {})]
      (is (= hit-id (some #{hit-id} (h/hit-ids-from-search-hit-response search-response))))))

  (testing "search for hit with qualification id"
    (let [search-qual-id-response (r/requester :GetHITsForQualificationType {:QualificationTypeId "00000000000000000071"})]
      (is (= hit-id (some #{hit-id} (h/hit-ids-from-hits-for-qualification-type-response search-qual-id-response))))))

  (testing "disable created hit"
    (let [disable-hit-response (r/requester :DisableHIT {:HITId hit-id})]
      (is (= "True" (h/valid? disable-hit-response [:response :DisableHITResponse :DisableHITResult :Request :IsValid]))))))

(deftest blocking-workers
  (testing "block worker"
    (let [block-worker-response (r/requester :BlockWorker {:WorkerId worker-id
                                                           :Reason (str "test block worker" (time/now))})]
      (is (= "True" (h/valid? block-worker-response [:response :BlockWorkerResponse :BlockWorkerResult :Request :IsValid])))))

  (testing "search for blocked worker"
    (let[get-blocked-response (r/requester :GetBlockedWorkers {})]
      (is (= worker-id (some #{worker-id} (h/worker-ids-from-get-blocked-workers-response get-blocked-response))))))

  (testing "unblock worker"
    (let [unblock-worker-response (r/requester :UnblockWorker {:WorkerId worker-id
                                                               :Reason (str "test unblock worker" (time/now))})]
      (is (= "True" (h/valid? unblock-worker-response [:response :UnblockWorkerResponse :UnblockWorkerResult :Request :IsValid]))))))
