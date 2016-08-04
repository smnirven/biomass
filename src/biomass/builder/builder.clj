(ns ^{:author "shafeeq"
      :doc "Cointains helper functions to build Mechanical Turk request data-structures"}
    biomass.builder.builder
  (:require [schema.core :as schema]
            [clojure.string :as s]
            [biomass.util :as u]
            [biomass.builder.schemas :as builder-schemas]))

(defn vector->map
  [element-list]
  (->> element-list
       (map-indexed (fn [i v] [(keyword (str i)) v]))
       (into {})))

(defn ->amazon-keywords [ks]
  (s/join "." (map name ks)))

(defn ->amazon-format* [current-path params]
  (letfn [(with-path [prefix [k v]]
            (let [path (conj prefix k)]
              (cond (sequential? v)
                    (->amazon-format* path (vector->map v))

                    (map? v)
                    (->amazon-format* path v)

                    :else
                    {path v})))]
    (->> params
         (map #(with-path current-path %))
         (into {}))))

(defn ->amazon-format [params]
  (->> (->amazon-format* [] params)
       (u/map-keys ->amazon-keywords)))
