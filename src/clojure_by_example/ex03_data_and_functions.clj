(ns clojure-by-example.ex03-data-and-functions)


;; Ex03: LESSON GOALS (build upon ex01, and ex02)
;; - Learn a few more nifty functions on collections (cousins of `map`)
;; - Learn to combine simple functions into more powerful functions
;; - Get a sense of how to model with data, and compute with functions
;; - Get a feel for some of the flexibility of Clojure data structures


;; Let's begin again, with our planets, our moons checker, and `map`
(def planets [{:name "Mercury" :moons 0}
              {:name "Venus"   :moons 0}
              {:name "Earth"   :moons 1}
              {:name "Mars"    :moons 2}])


;; Our little "helper" function, to check if a planet has moons.
(defn planet-has-moons?
  "Given a 'planet' (assume it's a hash-map), return true if
  it has at least one moon."
  [planet]
  (> (:moons planet)
     0))

;; These results make some sense
(map :name planets) ; ok, we can now print just the names of planets
(map :moons planets) ; ok, we can now count total number of moons

;; But what do we do with a bunch of boolean results?
(map planet-has-moons?
     planets)

;; More usefully, I would like to know _which_ planets have moons.
;; Even more usefully, given a collection of planets, I want to
;; filter all planets with moons.

;; What would we get here?
(filter planet-has-moons?
        planets)

;; Can we do the opposite?
(filter (fn [p] (not (planet-has-moons? p)))
        planets)

;; We can define new "helper" function, in terms of earlier helper
;; functions...
(defn planet-without-moon?
  "Planets without moons are exactly opposite to planets with moons."
  [planet]
  (not (planet-has-moons? planet)))

(filter planet-without-moon? planets)


;; With the above knowledge, can we write a function to separate out
;; planets into two groups...
;; - planets with moons, and
;; - planets without moons?

;; We will probably need to look up the groups separately later,
;; so we should model this as a hash-map. Something nice, like this...
;; {:planets-with-moons    [planet-X-hash-map p-Y-hash-map ...]
;;  :planets-without-moons [planet-A-hash-map p-B-hash-map ...]}


;; Why does this work?
{:planets-with-moons    (filter planet-has-moons? planets)
 :planets-without-moons (filter planet-without-moon? planets)}

;; Note: Do More With Less
;;
;; - Clojure lets us simply write down the structure of hash-maps,
;;   even if some values need to be computed.
;; - If an "un-evaluated expression" like (filter ...) exists, and if we
;;   evaluate the hash-map, Clojure will put the return value of the
;;   un-evaluated expression in the hash-map, where the expression was.
;;
;; By the way it's not just hash-maps, this works too, for
;; the same reason:
[(filter planet-has-moons? planets)
 (filter planet-without-moon? planets)]


;; So now, we can just do this to write a planet-grouping function...
(defn group-planets-by-moons
  [planets]
  {:planets-with-moons    (filter planet-has-moons? planets)
   :planets-without-moons (filter planet-without-moon? planets)})

;; And use it like this..
(group-planets-by-moons planets)

;; - When we called the function with `planets`, it did this:
;;   - evaluated each filter one by one
;;   - put the results in the respective places in the hash-map
;;   - returned the whole hash-map

;; Now we can further find...
(:planets-with-moons (group-planets-by-moons planets))

;; and further...
(map :name
     (:planets-with-moons (group-planets-by-moons planets)))


;; Lesson: Thinking with Data:
;;
;; Why would we first group, and then query?
;;
;; Suppose we wanted to do some fact-finding to compare planets that
;; have moons and those that don't. This suggests repeat use of the
;; grouped planets, instead of a single collection of all planets.
;;
;; So now, we would want to create the grouping only once, and then
;; pass that group down our data processing functions (pipeline).
;;
;; This way our "pipeline" can repeatedly use fast keyword-based
;; look-ups to select one group or the other instead of repeated use
;; of slower operations like (map planet-without-moons? planets) etc.



;; So far, we did some pretty cool things with sequence operations like
;; `map` and `filter`. Now for the big boss of sequence operations...

;; REDUCE!

;; Remember we can collect moons from planets?
(map :moons planets)
;; Now, how to "reduce" the collection into one number?
;; i.e., how to count the sum total of moons of all given planets?
;; Well...
(reduce + 0 (map :moons planets))
;; computes this: 0 + p1-moons + p2-moons + .... + pN-moons

;; `reduce` takes a function, an "accumulator" value, and an input
;; collection, and returns an "accumulated" value.

;; Imagine each step of the above computation, like this:

;; =======================================
;; Accumulator | Input collection (of number of moons)
;; =======================================
;; 0 (start)   | [0 0 1 2]   ; just before first step
;; 0           | [0 1 2]     ; at end of first step
;; 0           | [1 2]
;; 1           | [2]
;; 3           | []          ; reduce detects empty collection
;; ---------------------------------------
;; 3 (return value)          ; reduce spits out the accumulator

;; In words...
;; - To begin with, `reduce` passes the accumulator and the first item
;;   from the input collection to the function.
;; - `reduce` "manages" the accumulator for us, such that the function
;;   can update the accumulator, and `reduce` will feed the updated
;;   accumulator back into the function, along with the next item
;;   in the input collection.
;; - This continues until the input collection is exhausted.

;; `reductions` is a convenience function that helps us visualize
;; the "accumulator" at each step of the `reduce` computation:
(reductions + 0 (map :moons planets))


;; RECAP:
;; - `map`, `filter`, and `reduce` are powerful sequence-processing
;;   functions. Clojure programmers use these heavily.
;;
;; - Clojure programmers define small functions that do one task well,
;;   and then build up sophisticated solutions by combining many such
;;   small functions.
;;
;; - We can directly write down the actual structure of a datastructure,
;;   and Clojure will evaluate any un-evaluated values at run-time,
;;   and return us the same data structure, but with computed values.
;;
;; - Clojure data structures are very flexible, and allow us to
;;   model almost any real-world object. Previously we put hash-maps
;;   inside a sequence to represent a collection of planets. And, in
;;   this exercise, we put sequences inside a hash-map to group
;;   planets by moons.
