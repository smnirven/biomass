(defproject biomass "0.1.0-SNAPSHOT"
  :description "Boss people around. No suit required."
  :url "https://github.com/rboyd/biomass"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["etc"]
  :min-lein-version "2.0.0"
  :plugins [[lein-expectations "0.0.7"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-codec "1.0.0"]
                 [clj-http "0.7.8"]
                 [clj-time "0.6.0"]
                 [expectations "1.4.56"]])
