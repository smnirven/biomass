(ns biomass.test.qualifications
  (:require [clojure.test :refer :all]
            [biomass.qualifications :refer :all]))

(deftest test-validate-qualification-requirement
  (testing "Multiple qualification requirements"
    (let [qualifications [{:QualificationTypeId "00000000000000000072"
                           :Comparator "EqualTo"
                           :IntegerValue 10}

                          {:QualificationTypeId "00000000000000000071"
                           :Comparator "In"
                           :LocaleValue [{:Country "IN"} {:Country "US"}]}]]
      (is (= qualifications (validate-qualification-requirement qualifications)))))

  (testing "Fail for a wrong qualification in a vector"
    (let [qualifications [{:QualificationTypeId "00000000000000000072"
                           :Comparator "EqualTo"
                           :IntegerValue 10}

                          {:QualificationTypeId "00000000000000000071"
                           :LocaleValue [{:Country "IN"} {:Country "US"}]}]]
      (is (thrown? clojure.lang.ExceptionInfo (validate-qualification-requirement qualifications)))))

  (is (thrown? clojure.lang.ExceptionInfo (validate-qualification-requirement {})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Value does not match schema"(validate-qualification-requirement {:QualificationTypeId "1233456"})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Both IntegerValue and LocaleValue cannot be present at the same time"
                        (validate-qualification-requirement {:QualificationTypeId "00000000000000000071"
                                                             :Comparator "EqualTo"
                                                             :IntegerValue 10
                                                             :LocaleValue [{:Country "IN"} {:Country "US"}]
                                                             :RequiredToPreview false})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"LocaleValue can only be used with Worker_Locale QualificationTypeId"
                        (validate-qualification-requirement {:QualificationTypeId "000000000000"
                                                             :Comparator "In"
                                                             :LocaleValue {:Country "US"}
                                                             :RequiredToPreview false})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Expected a single LocaleValue for Comparator"
                        (validate-qualification-requirement {:QualificationTypeId "00000000000000000071"
                                                             :Comparator "EqualTo"
                                                             :LocaleValue [{:Country "IN"} {:Country "US"}]
                                                             :RequiredToPreview false})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Can use up to only 30 LocaleValue elements using the In or the NotIn Comparator"
                        (validate-qualification-requirement {:QualificationTypeId "00000000000000000071"
                                                             :Comparator "In"
                                                             :LocaleValue (repeat 31 {:Country "US"})
                                                             :RequiredToPreview false})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Cannot use IntegerValue with Worker_Locale QualificationTypeId"
                        (validate-qualification-requirement {:QualificationTypeId "00000000000000000071"
                                                             :Comparator "EqualTo"
                                                             :IntegerValue 10
                                                             :RequiredToPreview false})))

  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Can use up to only 15 IntegerValue elements using the In or the NotIn Comparator"
                        (validate-qualification-requirement {:QualificationTypeId "00000000000000000072"
                                                             :Comparator "In"
                                                             :IntegerValue (take 16 (range))})))

  (testing "Qualifications without any values"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Missing IntegerValue or LocaleValue for the "
                          (validate-qualification-requirement {:QualificationTypeId "00000000000000000072"
                                                               :Comparator "In"})))))
