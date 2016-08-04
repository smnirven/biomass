(ns biomass.util)

;; from https://github.com/staples-sparx/kits/blob/master/src/clojure/kits/map.clj
(defn map-keys
  "Apply a function on all keys of a map and return the corresponding map (all
   values untouched)"
  [f m]
  (when m
    (persistent! (reduce-kv (fn [out-m k v]
                              (assoc! out-m (f k) v))
                            (transient (empty m))
                            m))))
