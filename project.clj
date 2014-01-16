(defproject com.smnirven/biomass "0.5.0"
  :description "Boss people around. No suit required."
  :url "https://github.com/smnirven/biomass"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-codec "1.0.0"]
                 [clj-http "0.7.8"]
                 [clj-time "0.6.0"]
                 [com.github.kyleburton/clj-xpath "1.4.3"]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]]
                   :resource-paths ["test-resources"]}})
