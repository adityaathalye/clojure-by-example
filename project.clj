(defproject clojure_by_example "0.1.0-SNAPSHOT"
  :description "A workshop to introduce Clojure, to programmers new to Clojure."
  :url "https://github.com/inclojure-org/clojure-by-example"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  ;; LightTable dependencies
  ;; https://github.com/LightTable/Clojure/#connect-to-remote-nrepl
  :profiles {:dev {:dependencies [[lein-light-nrepl "0.3.3"]
                                  [enlive "1.1.6"]
                                  [cheshire "5.8.0"]
                                  [criterium "0.4.4"]]}}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
