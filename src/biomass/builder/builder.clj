(ns biomass.builder.builder)

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
