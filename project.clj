(defproject clojure_by_example "0.1.0-SNAPSHOT"
  :description "A workshop to introduce Clojure, to programmers new to Clojure."
  :url "https://github.com/inclojure-org/clojure-by-example"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  ;; Requirements: Java 8 or higher (recommended: Java 8 or Java 11)
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :profiles {:dev {:dependencies [[org.clojure/data.json "0.2.6"]
                                  [enlive "1.1.6"]
                                  [rewrite-clj "0.6.1"]]}})
