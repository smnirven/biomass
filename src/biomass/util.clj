(ns biomass.util
  (:require [clj-time.format :as tf]))

(defn nil-or-integer
  [s]
  (when-not (empty? s)
    (Integer/parseInt s)))

(defn nil-or-double
  [s]
  (when-not (empty? s)
    (Double/parseDouble s)))

;;TODO: allow passing of formatter type, with default
(defn nil-or-datetime
  [s]
  (when-not (empty? s)
    (tf/parse (tf/formatters :date-time-no-ms) s)))
