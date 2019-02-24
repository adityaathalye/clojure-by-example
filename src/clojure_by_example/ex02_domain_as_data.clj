(ns clojure-by-example.ex02-domain-as-data)

;; Ex02: LESSON GOAL:
;; - Model and query things using pure data
;; - Realize the flexibility and power of collections


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

;; EXERCISE
;; `get` and `get-in` work as expected
;; - Use `get` to extract :traces from `earth-alt`'s atmosphere
;; - The use `get-in` to do the same

#_(get 'FIX
       'FIX)

#_(get-in 'FIX 'FIX)


;; BUT, unlike plain old strings, keywords also behave as
;; _functions_ of hash-maps, and can look themselves up
;; in any given hash-map.

;; ("pname" earth)  ; Will FAIL!

(:pname earth-alt) ; Works!


;; EXERCISE
;; Extract `:argon` from the `:atmosphere` of `earth-alt`

('FIX ('FIX earth-alt))


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


;; EXERCISE
;; `filter` out planets with less `:mass` than the Earth

(defn less-mass-than-earth?
  [planet]
  (< ('FIX planet) 1))

('FIX 'FIX 'FIX)


;; EXERCISE
;; Recall how to use `filter`, `map`, and `reduce`:
(filter even? [1 2 3 4])
(map    inc   [1 2 3 4])
(reduce + 0   [1 2 3 4])
;; Use these to compute the total `:mass` of planets
;; having less mass than the Earth.



;; Maps, Vectors, and Sets also behave like functions!
;; - We don't normally use maps and vector in the function
;;   position to perform lookups (there are a few problems
;;   with doing so), but we often use _well-defined_ sets as
;;   predicate functions, to test for set membership.

;; Maps can "self-look-up" keys

({:a "a", :b "b"} :a)

;; Vectors can "self-look-up" by index position

(["a" "b" "c"] 0)

;; Sets can self-test set membership

(#{"a" "b" "c"} "b")   ; truthy: return set member if it exists
(#{"a" "b" "c"} "boo") ; falsey: return `nil` if it doesn't

;; Lists do NOT behave like functions

#_('("a" "b" "c") 0)   ; FAIL


;; EXERCISE
;; Define a predicate `poison-gas?` which returns the
;; poison gas if it belongs to a set of known poison gases,
;; or `nil` (falsey) otherwise. These are some known poison gases:
:carbon-monoxide, :chlorine, :helium
:sulphur-dioxide, :hydrogen-chloride


(def poison-gas?
  "Does the given gas belong to a set of known poison gases?"
  'FIX)

(poison-gas? :chlorine)                 ; truthy
(poison-gas? :oxygen)                   ; falsey


;; Collections are "open", i.e. very flexible
;; - We can make collections out of almost anything

;; Recall:
(def a-bunch-of-values
  [nil, false,                       ; falsey
   42, :a, "foo", true,              ; truthy
   {:a 1, :b 2}, [1 2 3 4],          ; truthy
   '(), {}, [], ""])                 ; truthy

(map boolean a-bunch-of-values)


;; And since functions are values too, we can potentially use
;; collections of functions like this:
(map (fn [f] (f 42))
     [str identity inc dec (fn [x] x)])


;; Domain Modeling in Clojure
;; - We use the flexibility of collections, to model
;;   real-world objects and logic as we please


;; Predicates and operations
{:number-checks [even? pos? integer? (fn [x] (> x 42))]
 :number-ops    [str identity inc dec (fn [x] x)]}


;; A data table:
[[:name :age :country]
 ["Foo" 10   "India"]
 ["Bar" 21   "Australia"]
 ["Baz" 18   "Turkey"]
 ["Qux" 42   "Chile"]]


;; HTML (ref: Hiccup templates)
[:div {:class "wow-list"}
 [:ul (map (fn [x] [:li x])
           [1 2 3 4])]]


;; Musical patterns (ref: github.com/ssrihari/ragavardhini)
{:arohanam [:s :r3 :g3 :m1 :p :d1 :n2 :s.],
 :avarohanam [:s. :n2 :d1 :p :m1 :g3 :r3 :s]}


;; DB queries (ref: Datomic)
#_[:find ?name ?duration
   :where [?e :artist/name "The Beatles"]
   [?track :track/artists ?e]
   [?track :track/name ?name]
   [?track :track/duration ?duration]]


;; Starfleet mission configurations
{:inhabit {:starships 5, :battle-cruisers 5,
           :orbiters 5,  :cargo-ships 5,
           :probes 30}
 :colonise {:starships 1, :probes 50}
 :probe {:orbiters 1, :probes 100}
 :observe {:orbiters 1, :probes 10}}


;; Only limited by your imagination!
