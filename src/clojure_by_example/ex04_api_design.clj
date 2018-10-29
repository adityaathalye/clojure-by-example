(ns clojure-by-example.ex04-api-design)

;; EX04: Lesson Goals:
;; - See how to "de-structure" data (it's a powerful, flexible lookup mechanism)
;; - Leverage de-structuring to design a self-documenting function API


;; "De-structuring"

;; - Clojure lets us reach into data structures in arbitrary ways,
;;   and extract multiple values in one go
;;
;; - We use this for clean lookups in `let` bindings, and
;;   in function signatures, to design expressive APIs.


;; Positional De-structuring
;;
;; - Pull apart sequential, ordered data structures like
;;   lists, vectors, and any other sequence with linear access
;;
;; - Follow the structure of the collection, and mechanically
;;   bind values to symbols by position.


;; Instead of looking up values by index position:
(str "The first two planets: "
     (:pname (get planets 0)) " and "
     (:pname (get planets 1)) ".")


;; We can bind symbols by position (match structure to structure):
(let [[m, v] planets]
  (str "The first two planets: "
       (:pname m) " and "
       (:pname v) "."))


;; Use underscores `_` to mark values we don't care for.
(let [[_, _, e] planets]
  (str (:pname e)
       " is the third rock from the Sun."))


;; Use `:as` to also alias the whole structure.
(let [[_, _, e :as planet-names] (map :pname planets)]
  {:useless-trivia (str e " is the third rock from the Sun.")
   :planet-names   planet-names})


;; "Associative" De-structuring
;;
;; - Syntax to reach into associative data structures
;;   (having key-value semantics), in arbitrary ways.
;;
;; -  Note: Clojure "Records" and vectors are associative too
;;
;; - Follow the structure of the collection, and mechanically
;;   bind values to symbols by key name.


;; Instead of looking up values like this:
(:pname earth-alt)

;; We can follow the map's structure like this:
(let [{p :pname} earth-alt]
  p)

;; Compose it with positional destructuring:
(let [[_, _, {e :pname}] planets]
  (str e " is the third rock from the Sun."))


;; And instead of doing lookups one at a time:
(str (:pname earth-alt) " has "
     (:moons earth-alt) " moon, "
     (:oxygen (:atmosphere earth-alt))  "% Oxygen, "
     (:argon (:atmosphere earth-alt))  "% Argon, and "
     (:traces (:atmosphere earth-alt))  "% trace gases.")


;; We can arbitrarily de-structure maps, directly:
(let [{p :pname
       m :moons
       {traces :traces
        Ar :argon
        O2 :oxygen} :atmosphere} earth-alt]
  (str p " has "
       m " moon, "
       O2 "% Oxygen, "
       Ar "% Argon, and "
       traces "% trace gases."))


;; The `:keys` form lets us de-structure more concisely:
;; - Note: in this style, we must exactly match spellings of
;;   symbol names, with spellings of the keys we wish to bind.
(let [{:keys [pname moons]
       {:keys [oxygen argon traces]} :atmosphere}
      earth-alt]
  (str pname " has "
       moons " moon, "
       oxygen "% Oxygen, "
       argon "% Argon, and "
       traces "% trace gases."))


;; More powerfully, the `:keys` form lets us:
;; - extract multiple values,
;; - define fallbacks for missing values, _and_
;; - alias the original input.
;; All in one shot:

(defn summarise-planet
  [{:keys [pname moons]
    {:keys [oxygen argon traces]
     :or {oxygen "unknown"
          argon "unknown"
          traces "unknown"}} :atmosphere
    :as planet}]
  {:summary (str pname " has "
                 moons " moon(s), "
                 oxygen " % O2, "
                 argon " % Argon, and "
                 traces" % of trace gases.")
   :planet planet})

(summarise-planet earth-alt)

(summarise-planet {:pname "Mercury", :moons 0, :mass 0.0533})

(map summarise-planet
     planets)


;; Self-documenting function API:
;;
;; Last, but not least, de-structuring function arguments
;; automatically gives us self-documenting function signatures.

#_(clojure.repl/doc summarise-planet) ; Evaluate this in the REPL

#_(meta #'summarise-planet)

;; RECAP:
;;
;; - hash-maps let us conveniently represent objects we wish to
;;   model and query
;; - We can query hash-maps variously with keywords, `get`, and `get-in`
;; - If we use keywords as keys in hash-maps, querying is dead-simple
;; - We can define our own functions with `defn`, using this syntax:
;;
;;   (defn function-name
;;         [arg1 arg2 arg3 ... argN]
;;         (body of the function))
;;
;; - Using general-purpose data structures, and writing general-purpose
;;   functions lets us do more with less
