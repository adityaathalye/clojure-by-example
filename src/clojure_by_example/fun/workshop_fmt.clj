(ns clojure-by-example.fun.workshop-fmt
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [rewrite-clj.zip :as z])
  (:import java.io.PushbackReader))

(def relative-src-path "src/clojure_by_example/")
(def relative-dest-path (str relative-src-path "workshop/"))
(def workshop-file-pattern (re-pattern "ex.*.clj"))


(defn elide-if-comment
  [zloc]
  (if (= (z/value zloc) 'comment)
    (-> zloc z/up z/remove)
    zloc))


(defn elide-comment-forms
  [zloc]
  (if (z/end? zloc)
    zloc
    (recur (-> zloc elide-if-comment z/next))))


(defn workshop-files!
  [sourcedir file-pattern]
  (->> (io/file sourcedir)
       .listFiles
       (filter #(not (.isDirectory %)))
       (map #(.getName %))
       (filterv (fnil #(re-find file-pattern %) ""))))


(defn spit-root!
  [zloc outfile]
  (with-open [w (io/writer outfile :encoding "UTF-8")]
    (z/print-root zloc w)))


(defn prep-workshop-code!
  ([infile outfile]
   (-> (z/of-file infile)
       elide-comment-forms
       (spit-root! outfile)))
  ([infile indir outdir]
   (prep-workshop-code!
    (str indir infile)
    (str outdir infile))))


(comment
  ;; Try one...
  (prep-workshop-code! "ex06_full_functional_firepower.clj"
                       relative-src-path
                       relative-dest-path)

  ;; Review output using:
  ;; ls -1 | grep ex | xargs -I {} diff -s {} "workshop/"{}
  (doseq [f (sort (workshop-files! relative-src-path
                                   workshop-file-pattern))]
    (prep-workshop-code! f
                         relative-src-path
                         relative-dest-path))
  )


(comment
  ;; Experiment with file as EDN
  (defn read-forms [file]
    ;; https://stackoverflow.com/questions/39976680/reading-another-clojure-program-as-a-list-of-s-expressions
    (let [rdr (-> file io/file io/reader PushbackReader.)
          sentinel (Object.)]
      (loop [forms []]
        (let [form (edn/read {:eof sentinel} rdr)]
          (if (= sentinel form)
            forms
            (recur (conj forms form)))))))
  )

(comment
  ;; Experiments with zippers

  ((comp z/child-sexprs z/down) (z/of-file "foobar.clj"))

  (spit-root!
   (z/prewalk (z/of-file "foobar.clj")
              #(= (z/value %) 'comment)
              z/remove)
   "foobar-cleaned.clj")


  (let [zloc (z/of-file (str sourcedir "foobar.clj"))
        outfile (str sourcedir "foobar-cleaned.clj")]
    (with-open [w (clojure.java.io/writer outfile :encoding "UTF-8")]
      (loop [znodes zloc]
        (if (z/end? znodes)
          (z/print-root znodes w)
          (recur (if (= (z/value znodes) 'comment)  ; for fanciness, this could be a comment macro by another name, like `extra-explanation'
                   ((comp z/next z/remove z/up) znodes)
                   (z/next znodes)))))))
  )
