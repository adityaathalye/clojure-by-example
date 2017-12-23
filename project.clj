(defproject clojure_by_example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  ;; LightTable dependencies
  ;; https://github.com/LightTable/Clojure/#connect-to-remote-nrepl
  :profiles {:dev {:dependencies [[lein-light-nrepl "0.3.3"]
                                  [enlive "1.1.6"]
                                  [cheshire "5.8.0"]
                                  [criterium "0.4.4"]]}}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
