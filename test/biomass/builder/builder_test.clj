(ns biomass.builder.builder-test
  (:require [clojure.test :refer :all]
            [biomass.builder.builder :refer :all]))

(deftest test-->amazon-format
  (is (= {} (->amazon-format nil)))
  (is (= {} (->amazon-format {})))
  (is (= {"a" 1} (->amazon-format {:a 1})))
  (is (= {"a.b" 1 "a.c" 2} (->amazon-format {:a {:b 1 :c 2}})))
  (is (= {"a.b.c.d.e.f" 1 "a.b.c.x.y.z" "abc"} (->amazon-format {:a {:b {:c {:d {:e {:f 1}}
                                                                             :x {:y {:z "abc"}}}}}})))
  (is (= {"a.0" "1" "a.1" "2" "a.2" "3"} (->amazon-format {:a ["1" "2" "3"]})))
  (is (= {"a.0.b.0" 2 "a.0.b.1" 3 "a.0.b.2.c.0" 4 "a.0.b.2.c.1" 5 "a.1" 1}
         (->amazon-format {:a [{:b [2 3 {:c [4 5]}]} 1]}))))
