(defproject com.smnirven/biomass "0.5.1"
  :description "Boss people around. No suit required."
  :url "https://github.com/smnirven/biomass"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-codec "1.0.1"]
                 [clj-http "3.1.0" :exclusions [cheshire
                                                crouton
                                                org.clojure/tools.reader
                                                commons-codec]] ;; only this because ring-codec is behind
                 [clj-time "0.12.0"]
                 [prismatic/schema "1.1.3"]
                 [com.github.kyleburton/clj-xpath "1.4.5"]]
  :plugins [[cider/cider-nrepl "0.12.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :resource-paths ["test-resources"]
                   :plugins [[lein-midje "3.0.0"]]}})
