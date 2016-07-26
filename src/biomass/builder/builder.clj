(ns ^{:author "shafeeq"
      :doc "Cointains helper functions to build Mechanical Turk request data-structures"}
    biomass.builder.builder
  (:require [schema.core :as s]
            [biomass.builder.schemas :refer :all]))

(declare convert-vec-params)

(defn convert-params
  ([params]
   (convert-params nil params))
  ([prefix params]
   (reduce (fn [r [k v]]
             (let [key-prefix (if prefix
                                (str prefix "." (name k))
                                (name k))]
               (cond
                 (map? v) (merge r (convert-params key-prefix v))
                 (sequential? v) (merge r (convert-vec-params key-prefix v))
                 :else (assoc r (keyword key-prefix) v))))
           {}
           params)))

(defn convert-vec-params
  [prefix values]
  (reduce (fn [r [i v]]
            (let [key-prefix (str prefix "." i)]
              (cond
                (map? v) (merge r (convert-params key-prefix v))
                (sequential? v) (merge r (convert-vec-params key-prefix v))
                :else (assoc r (keyword key-prefix) v))))
          {}
          (map-indexed vector values)))

(defn build-qualification-requirement
  [params]
  (s/validate schema-QualificationRequirement params))
