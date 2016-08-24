(ns biomass.test.request
  (:require [biomass.request :refer :all]
            [clojure.test :refer :all]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn setup-creds
  [f]
  (let [original-access-key @aws-access-key
        original-secret-key @aws-secret-access-key
        original-sandbox @sandbox-mode]
    (setup {:AWSAccessKey nil :AWSSecretAccessKey nil :sandbox nil})
    (f)
    (setup {:AWSAccessKey original-access-key :AWSSecretAccessKey original-secret-key :sandbox original-sandbox})))

(use-fixtures :once setup-creds)


(defn process-xml-string
  [xml-string]
  (let [stream (->> xml-string .getBytes java.io.ByteArrayInputStream.)]
    (->> stream
         xml/parse
         zip/xml-zip
         first)))

(deftest test-parse-zipped-xml
  (is (= (parse-zipped-xml (process-xml-string "<?xml version=\"1.0\"?><a></a>"))
         {:a nil}))
  (is (= (parse-zipped-xml (process-xml-string "<?xml version=\"1.0\"?><a><b></b></a>"))
         {:a [{:b nil}]}))
  (is (= (parse-zipped-xml (process-xml-string "<?xml version=\"1.0\"?>
                                                 <a>
                                                  <b>1</b>
                                                  <b>2</b>
                                                  <c>3</c>
                                                 </a>"))
         {:a [{:b ["1"]}
              {:b ["2"]}
              {:c ["3"]}]}))
  (is (= (parse-zipped-xml (process-xml-string "<?xml version=\"1.0\"?>
                                                 <BlockWorkerResponse>
                                                  <OperationRequest>
                                                   <RequestId>a7191361-9bfc-47ef-a49d-2ea30f9c6f97</RequestId>
                                                  </OperationRequest>
                                                  <BlockWorkerResult>
                                                   <Request>
                                                    <IsValid>True</IsValid>
                                                   </Request>
                                                  </BlockWorkerResult>
                                                 </BlockWorkerResponse>"))
         {:BlockWorkerResponse
          [{:OperationRequest
            [{:RequestId ["a7191361-9bfc-47ef-a49d-2ea30f9c6f97"]}]}
           {:BlockWorkerResult [{:Request [{:IsValid ["True"]}]}]}]}))

  (is (= (parse-zipped-xml (process-xml-string (slurp "test/fixtures/sample-xml")))
         (read-string (slurp "test/fixtures/sample-parsed-xml")))))

(deftest test-send-and-parse
  (testing "Throw exception if not setup"
    (is (thrown? RuntimeException (send-and-parse "SomeOperation" {:some-key :some-value})))))
