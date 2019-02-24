(ns clojure-by-example.fun.inspect-nasa-planets
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as cs]
            [clojure.inspector :as inspect]))

;; Warning:
;; Code here may not be "professional" grade.
;;
;; This is just some fun messing around with some planetary data
;; published by NASA.
;;
;; Important:
;; Be kind to their servers, if you start tinkering with this code.
;;
;; First download a copy of the HTML pages to local disk,
;; and replace the URL with the absolute path to the file on disk.
;; Everything else should work the same.


(def planets-to-earth-ratios-table
  {:table-name :planet-to-earth-ratios
   :table-data-file "https://nssdc.gsfc.nasa.gov/planetary/factsheet/"
   :num-cols 10
   :num-rows 20
   :rows-label-path [:table :tr [:td html/first-child] :a]
   :cols-label-path [:table [:tr html/first-child] :td :a]
   :stats-path [:table :tr (html/attr= :align "center")]})


(def small-worlds-table
  {:table-name :small-worlds
   :table-data-file "https://nssdc.gsfc.nasa.gov/planetary/factsheet/galileanfact_table.html"
   ;; Of 12 cols, 11 have data, one is empty and must be removed
   ;; prior to processing.
   :num-cols 11
   :num-rows 16
   :rows-label-path [:table :tr [:td html/first-child] :b]
   :cols-label-path [:table [:tr html/first-child] :td :b]
   :stats-path [:table :tr (html/attr= :align "center")]})


(defn file->html-resource
  "Given a path to a file, produce an Enlive 'html resource'."
  [file-or-url]
  (html/html-resource
   (java.io.StringReader.
    (slurp file-or-url))))


(comment
  (let [checker (fn [table xy-key]
                  (map :content
                       (html/select
                        (file->html-resource (:table-data-file table))
                        (xy-key table))))
        check-pte (partial checker planets-to-earth-ratios-table)
        check-sw (partial checker small-worlds-table)
        check-all (juxt check-pte check-sw)]
    [(check-all :rows-label-path)
     (check-all :cols-label-path)]))


(defn massage-table-info
  [{:keys [table-data-file num-cols num-rows] :as table-info}]
  (assoc table-info
         :num-stats (* num-cols num-rows)
         ;; Maybe not such a good idea to inject html-resource.
         ;; It could get really big. But then again, how to design the
         ;; api so that we can avoid parsing the DOM repeatedly?
         :html-resource (file->html-resource table-data-file)))


(comment
  (massage-table-info planets-to-earth-ratios-table))


(defn extract-raw-data
  [{:keys [html-resource stats-path
           rows-label-path cols-label-path]
    :as table-info}]
  (let [extract (partial html/select html-resource)]
    (dissoc (assoc table-info
                   :col-labels    (extract cols-label-path)
                   :row-labels    (extract rows-label-path)
                   :stats-by-rows (extract stats-path))
            :html-resource)))


(comment
  (-> planets-to-earth-ratios-table
      massage-table-info
      extract-raw-data))


(def cleanup-table-data nil)

(defmulti cleanup-table-data :table-name)


(defmethod cleanup-table-data :planet-to-earth-ratios
  [{:keys [col-labels row-labels stats-by-rows
           num-cols num-stats]
    :as raw-table-info}]
  (let [keywordize (comp keyword
                         #(cs/replace % #"\s+" "-")
                         cs/lower-case)
        cleanup-labels #(->> %
                             (map :content)
                             flatten
                             (map keywordize))]
    ;; replace labels and stats with cleaned up versions
    (assoc raw-table-info
           :row-labels (cleanup-labels row-labels)
           :col-labels (cleanup-labels col-labels)
           :stats-by-rows ((comp #(map flatten %)
                                 #(partition num-cols %)
                                 #(map :content %)
                                 #(take num-stats %)
                                 #(drop num-cols %))
                           stats-by-rows))))


(defmethod cleanup-table-data :small-worlds
  [{:keys [col-labels row-labels stats-by-rows
           num-cols num-stats]
    :as raw-table-data}]
  (let [keywordize (comp keyword
                         #(cs/replace % #"\s+" "-")
                         cs/lower-case)
        cleanup-cols #(->> %
                           (map :content)
                           flatten
                           ((partial map
                                     (fn [s] (cs/replace s #"�" ""))))
                           rest ; get rid of empty column's label
                           (map keywordize))
        massage-row-label #(cond (#{:a} (:tag %))
                                 (:content %)
                                 (#{:sup} (:tag %))
                                 (apply str "^" (:content %))
                                 :else %)
        cleanup-rows (comp rest butlast ; get rid of empty row labels
                           (partial
                            map (comp keywordize
                                      #(cs/replace % #"_|\(|\)" "")
                                      #(apply str %)
                                      flatten
                                      (partial map massage-row-label)
                                      :content)))
        cleanup-stats (comp (partial partition num-cols)
                            (partial filter #(not= "�" %))
                            flatten
                            #(map :content %)
                            #(take num-stats %)
                            #(drop (inc num-cols) %))]
    (assoc raw-table-data
           :col-labels (cleanup-cols col-labels)
           :row-labels (cleanup-rows row-labels)
           :stats-by-rows (cleanup-stats stats-by-rows))))


(comment
  (-> planets-to-earth-ratios-table
      massage-table-info
      extract-raw-data
      cleanup-table-data)

  (-> small-worlds-table
      massage-table-info
      extract-raw-data
      cleanup-table-data))


(defn transpose-matrix
  "The matrix must be a vector of vectors, where all nested vectors
  have the same number of items. "
  [matrix]
  (apply map vector matrix))


(defn planet-properties
  [{:keys [col-labels row-labels stats-by-rows] :as table-data}]
  (let [stats-by-cols (transpose-matrix stats-by-rows)
        denormalise-row (fn [m k v]
                          (assoc m k (zipmap row-labels v)))]
    (reduce-kv denormalise-row
               {}
               (zipmap col-labels stats-by-cols))))


(defn planets-rel-earth
  []
  (-> planets-to-earth-ratios-table
      massage-table-info
      extract-raw-data
      cleanup-table-data
      planet-properties))


(defn small-worlds
  []
  (-> small-worlds-table
      massage-table-info
      extract-raw-data
      cleanup-table-data
      planet-properties))


(comment
  ;; Some repl-inspection

  ;; Try stuff
  (:jupiter (planets-rel-earth))

  (:callisto (small-worlds))

  ;; repl-print out the table like it appears in the HTML page
  (let [{:keys [col-labels row-labels stats-by-rows]}
        (-> planets-to-earth-ratios-table
            massage-table-info
            extract-raw-data
            cleanup-table-data)
        col-labels+ (into [:prop] col-labels)
        table (map (partial zipmap col-labels+)
                   (map (fn [x xs] (into [x] xs))
                        row-labels stats-by-rows))]
    (clojure.pprint/print-table col-labels+ table))

  ;; repl-print the inverted table (relative to input table)
  (let [table-data (planets-rel-earth)
        massage-for-pprint (fn [xs k v] (conj xs (assoc v :planet k)))
        table-data (reduce-kv massage-for-pprint [] table-data)]
    (clojure.pprint/print-table
     [:planet :mass :number-of-moons :distance-from-sun]
     table-data))

  ;; walk the tree, in a swing UI
  (inspect/inspect-tree (small-worlds)))
