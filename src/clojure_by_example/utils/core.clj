(ns clojure-by-example.utils.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as cs]
            [clojure.inspector :as inspect]))

(def planets-to-earth-ratios-html "resources/planetary_ratios_to_earth.html")

(def small-worlds-html "resources/solar_system_small_worlds_fact_sheet.html")

(defn file->html-resource
  [file]
  (html/html-resource
   (java.io.StringReader.
    (slurp file))))


(defn transpose-matrix
  "The matrix must be a vector of vectors, where all nested vectors
  have the same number of items. "
  [matrix]
  (let [cols (count (first matrix))]
    (map (fn [col] (map #(nth % col) matrix))
         (range cols))))

(defn extract-table-data
  [html-resource]
  (let [num-planets 10
        num-stat-rows 18
        num-stats (* num-planets num-stat-rows)
        xy-labels (html/select html-resource [:table :tr :td :a])
        xy-labels (flatten (map :content xy-labels))
        xy-labels (map (comp keyword
                             #(cs/replace % #"\s+" "-")
                             cs/lower-case)
                       xy-labels)
        stats (html/select html-resource
                           [:table :tr
                            (html/attr= :align "center")])
        stats ((comp #(map flatten %)
                     #(partition num-planets %)
                     #(map :content %)
                     #(take num-stats %)
                     #(drop num-planets %))
               stats)]

    {:col-labels (take num-planets xy-labels)
     :row-labels (drop num-planets xy-labels)
     :stats-by-rows stats}))


(defn extract-labels
  [html-resource label-path num-labels
   label-select-fn cleanup-fn]
  (->> label-path
       (html/select html-resource)
       (map :content)
       (label-select-fn num-labels)
       cleanup-fn))

(defn extract-small-planets
  []
  (let [html-resource (file->html-resource small-worlds-html)
        cols (extract-labels html-resource
                             [:table :tr :td :b]
                             12
                             take
                             (comp
                              (partial map #(cs/replace % #" " ""))
                              flatten))
        massage-row-labels #(cond (#{:a} (:tag %))
                                  (:content %)
                                  (#{:sup} (:tag %))
                                  (apply str "^" (:content %))
                                  :else %)
        rows (extract-labels html-resource
                             [:table :tr :td :b]
                             12
                             (comp (partial drop-last 12) drop)
                             (partial
                              map
                              (comp #(apply str %)
                                    flatten
                                    (partial map massage-row-labels))))
        stats (html/select html-resource
                           [:table :tr
                            (html/attr= :align "center")])
        stats ((comp (partial filter #(not= " " %))
                     flatten
                     #(map :content %)
                     #(drop-last 12 %)
                     #(drop 12 %))
               stats)]
    {:row-labels rows
     :col-labels (rest cols)
p     :stats-by-rows (partition 11 stats)}))


(defn planet-properties
  [{:keys [col-labels row-labels stats-by-rows] :as table-data}]
  (let [stats-by-cols (transpose-matrix stats-by-rows)
        denormalise-row (fn [m k v]
                          (assoc m k (zipmap row-labels v)))]
    (reduce-kv denormalise-row
               {}
               (zipmap col-labels stats-by-cols))))


(defn planets-rel-earth
  [file-path]
  (-> file-path
      file->html-resource
      extract-table-data
      planet-properties))


(defn small-worlds
  []
  (planet-properties
   (extract-small-planets)))


(small-worlds)


(comment
  ;; Try stuff
  (planets-rel-earth planets-to-earth-ratios-html)

  (get (planet-properties (extract-small-planets))
       "CALLISTO"))


(comment
  ;; Some repl-inspection

  ;; repl-print out the table like it appears in the HTML page
  (let [{:keys [col-labels row-labels stats-by-rows]}
        (-> planets-rel-earth-html
            file->html-resource
            extract-table-data)
        col-labels+ (into [:prop] col-labels)
        table (map (partial zipmap col-labels+)
                   (map (fn [x xs] (into [x] xs)) row-labels stats-by-rows))]
    (clojure.pprint/print-table col-labels+ table))

  ;; repl-print the inverted table (relative to input table)
  (let [table-data (planets-rel-earth planets-to-earth-ratios-html)
        massage-for-pprint (fn [xs k v] (conj xs (assoc v :planet k)))
        table-data (reduce-kv massage-for-pprint [] table-data)]
    (clojure.pprint/print-table [:planet :mass :number-of-moons :distance-from-sun] table-data))

  ;; walk the tree, in a swing UI
  (inspect/inspect-tree (planets-rel-earth planets-to-earth-ratios-html)))
