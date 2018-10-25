(ns clojure-by-example.ex01-domain-as-data)


;; Ex01: LESSON GOAL:
;; - Model and query things using pure data
;; - See how to "de-structure" data (it's a powerful, flexible lookup mechanism)
;; - Leverage de-structuring to design a self-documenting function API


;; Our Earth

;; "pname"  "Earth"
;; "mass"   1 ; if Earth mass is 1, Jupiter's mass is 317.8 x Earth
;; "radius" 1 ; if Earth radius is 1, Jupiter's radius is 11.21 x Earth
;; "moons"  1
;; "atmosphere" "nitrogen"     78.08
;;              "oxygen"       20.95
;;              "CO2"           0.40
;;              "water-vapour"  0.10
;;              "argon"  0.33
;;              "traces" 0.14


;; Recall: Literal syntax:
;; - If we just put curly braces in the right places,
;;   we can turn the given table into a Clojure hash-map:

(def earth
  {"pname" "Earth"
   "mass"   1
   "radius" 1
   "moons"  1
   "atmosphere" {"nitrogen"       78.08
                 "oxygen"         20.95
                 "carbon-dioxide" 0.4
                 "water-vapour"   0.10
                 "argon"          0.33
                 "traces"         0.14}})

;; Now we can look up any value using `get`, and `get-in`:

;; with `get`
(get earth "pname")

(get (get earth "atmosphere")
     "traces")


;; more conveniently, with `get-in`
(get-in earth ["pname"])

(get-in earth ["atmosphere" "traces"])
;;            '--> imagine this as a "path" to the value



;; Alternatively, we can model the earth this way,
;; using keywords as keys, to great benefit:
(def earth-alt
  {:pname "Earth"
   :mass 1
   :radius 1
   :moons 1
   :atmosphere {:nitrogen 78.08
                :oxygen 20.95
                :carbon-dioxide 0.4
                :water-vapour 0.10
                :argon 0.33
                :traces 0.14}})

;; `get` and `get-in` work as expected

(get (get earth-alt :atmosphere)
     :traces)


(get-in earth-alt [:atmosphere :traces])


;; BUT, unlike plain old strings, keywords also behave as _functions_
;; of hash-maps, and can look themselves up in hash-maps.

;; ("pname" earth)  ; Will FAIL!

(:pname earth-alt) ; Works!

(:argon (:atmosphere earth-alt))


;; Which means we can use keywords in this manner:

(def planets
  [{:pname "Mercury" :moons 0  :mass 0.0533}
   {:pname "Venus"   :moons 0  :mass 0.815}
   {:pname "Earth"   :moons 1  :mass 1}
   {:pname "Mars"    :moons 2  :mass 0.107}])


;; Instead of having to write functions to query planets:
(map (fn [p] (get p :pname))
     planets)

;; We can directly use keywords as functions:
(map :pname
     planets)


;; Find planets with less mass than the Earth

(defn less-mass-than-earth?
  [planet]
  (< (:mass planet) 1))

(filter less-mass-than-earth?
        planets)


;; Compute total mass of planets having
;; less mass than the Earth:
(reduce + 0
        (map :mass
             (filter less-mass-than-earth?
                     planets)))


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
