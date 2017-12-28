(ns clojure-by-example.ex05-immutability-and-fp)


;; WORK IN PROGRESS


;; Ex05: Lesson Goals
;; - This section is more conceptual, than exercise-oriented.
;; - Set you up with some important ideas, which we will use heavily
;;   in the final section (and in all our Clojure programs)
;;   - All values are immutable by default (and we like it this way)
;;   - What `def` is
;;   - Lexical scope, and why you should avoid global defs
;;   - What are "pure functions"?
;;   - Convenient syntax for functions with multiple arities,
;;     variable arities, and hash-maps or vectors as arguments.




;; Previously, in Clojure by Example:
;;
;; - We either only queried one or more planets,
;; - Or we checked if values satisfy some "predicate"
;; - Or we calculated new values using (reduce + ...) etc.
;; - Or we defined globals (with def), and locals (with let)
;;
;; - But we did not really try to modify or change any quantity
;;   or sequence.


;; What if we try to "change" things?


(def pi 3.141)

(+ pi 1) ; add one to pi

;; EXERCISE:
;;
;; Predict the value of pi:

pi ; evaluate to confirm


;; Hm, let's try vectors and maps.
;;
;; - We can "associate" new key-vals into an existing map
(assoc {:a 1}
  :b 2
  :c 3)
;;
;; - With assoc, we can also update existing key-value pairs
(assoc {:a 1 :b 2} :b 99)
;;
;; - And, finally, we can "dissociate" existing key-vals
(dissoc {:a 1 :b 2 :c 3} :b :c)


;; - So suppose we define...

(def habitable-planets [{:pname "Earth" :moons 1}
                        {:pname "Mars" :moons 2}])

;; - Then, maybe, we can `assoc` a new k-v pair into all
;;   habitable-planets:
;; - And while we're at it, also dissoc an existing one:
;;
(map (fn [planet]
       (assoc (dissoc planet :moons)
              :habitable? true))
     habitable-planets)


;; EXERCISE:
;;
;; Predict the result of filtering by the value of `:habitable?`:

(filter :habitable? habitable-planets)

habitable-planets ; confirm by checking the value of this



;; WHY IS Clojure DOING THIS TO US???!!!
;;
;; Why are we not allowed to mutate these things?
;;
;; How will we get _anything_ done in the real world?
;;
;; Well, actually, you've _already_ been programming with such
;; "immutable" values, and it hasn't stopped you from being awesome!
;;
;; Now, we just need to learn "The art of fighting, without fighting",
;; or, how to change the world, _without_ using _things that change_.
;;
;; Immutability v/s mutability is a deeply unsettling topic, so
;; we will park the discussion for now and come back to it in the
;; next "chapter".
;;
;; But first, a few important practicalities.


;; On `def`:

;; DO NOT use `def` to _emulate_ mutation like this:

(def am-i-mutable? 3.141)

#_(def am-i-mutable? 42)

#_(def am-i-mutable? "LoL, No!")

;; This is _not_ actually mutation.
;;
;; It's repeated top-level _re-definition_ of `am-i-mutable?`.
;;
;; At each re-definition, `am-i-mutable` effectively "becomes"
;; the new _immutable_ value.
;;
;; "Immutable" value is actually an oxymoron. Recall, we said that
;; ALL values are by definition immutable. 3.141 will always be 3.141
;; for ever and ever, till the end of time.
;;
;; So, we use `def` to give globally-usable names to _values_ ---
;; immutable things that we can easily refer to again and again,
;; throughout our program.
;;
;; What's the point?
;;
;; Well, remember functions are values?

(fn [x] x) ; is a value (which shall remain anonymous)


(defn same
  [x]
  x) ; `same` is the name of a function. Therefore `same` names a value.


;; So what if we...
(def same-same
  (fn [x] x)) ; hah!

;; That is to say:
;; - (fn [x] x) means "return the given value, unchanged", and...
;; - It _must_ mean _exactly_ this for ever and ever,
;;   until the end of time. It _must_ be immutable.
;; - And oh, we also need to reuse this idea, so would you please
;;   give it the name `same-same`?

;; And actually, `defn` is just a convenience wrapper over `def`,
;; because we can't live without defining functions, in Clojure-land.

(macroexpand '(defn same [x] x)) ; yes, it is



;; Lexical Scope, and Global Vars


(def x 42) ; `x` is a global "var"

(defn x+
  [y]
  (+ x 1)) ; this `x` refers to the global `x`

(x+ 1) ; will return 43
(x+ 9) ; will still return 43


(defn x++
  [x] ; this `x` is local to the scope of x++,
      ; and will "shadow" the global `x`
  (+ x 1))

(x++ 1) ; will return 2
(x++ 9) ; will return 10


(defn x+++
  [x] ; this `x` will shadow the global `x`
  (let [x 10] ; but this `x` is local to the let,
              ; and will shadow all "outer" x-es
    (+ x 1)))

(x+++ 1) ; will return 11
(x+++ 9)  ; will still return 11


;; Lexical scope guarantees that the reference to a value will be
;; "enclosed" in the scope in which it is being used.

;; This makes it very easy to reason about where a value originated.
;; - Start at the place of reference of the value.
;; - Then "walk" outwards, until you meet the very first let binding,
;;   or arg-list, or def, where the value was bound.
;; - Now you know where the value came from.


;; EXERCISE:
;;
;; Reason about what is happening here:

(let [x 3.141]
  ((fn [x] x)  x))


;; How about this? What will happen here?

((fn [x] x)  x)


;; How about this?

(let [x x]
  ((fn [x] x)  x))


;; This?

((fn [x] x) (let [x x]
              x))

;; And finally, this?

((let [x 3.141]
   (fn [x] x))   x)


;; Lesson:
;;
;; - Clojure programmers use `def` _only_ to attach values to globally
;;   referenced names, for re-use. For example:
;;   - The value of `pi` would be good global.
;;   - The value of a function is often a good global.
;;     (Recall: defn just wraps over def).
;;
;; - We rely on lexical scope to bind values as close as possible
;;   to the place in code where the value is used. This makes code
;;   much easier to reason about.
;;
;; - "Lexical scope" is super-important. Understand it and use it well,
;;   for great good.



;; "Pure Functions"


;; This function is "pure"
;; - It is a mapping of input data -> output data and nothing more.
(defn add-one
  [x]
  (+ x 1))


;; This function is "impure"
;; - Although it adds one to the input, it also changes the world on
;;   the side, by sending out a value to some other place
;; - printing is a "side effect" who's outcome we cannot always predict
(defn add-one!
  [x]
  (println x)
  (+ x 1))
;; - other examples of side-effects include:
;;   - writing to a db (what if the db gets slow or unavailable?)
;;   - or logging to console (what if the log file gets corrupted?)


(add-one 1) ; adds one, but never changes the outside world

(add-one! 1) ; adds one, and also changes the outside world


;; Pure functions are drop-in replacements for each other.

(= 2
   ((fn [x] (+ x 1)) 1)
   (add-one 1)
   (inc 1))

;; Impure functions cannot be used as drop-in replacements for pure
;; functions, or for other impure functions for that matter.




;; Convenient Syntax for functions
;;
;; - We use these conveniences for good effect in API design.


;; Multiple arities
;; - When we know for sure we have to handle some known numbers
;;   of arguments.

(defn add-upto-three-nums
  ([] 0) ; identity of addition
  ([x] (+ x 0 0))
  ([x y] (+ x y 0))
  ([x y z] (+ x y z)))

(add-upto-three-nums)
(add-upto-three-nums 1)
(add-upto-three-nums 1 2)
(add-upto-three-nums 1 2 3)
#_(add-upto-three-nums 1 2 3 4) ; will fail


;; Variable aritiy
;; - When we don't know in advance how many arguments we
;;   will have to handle, but we want to handle them all.

(defn add-any-numbers
  [& nums]
  (reduce + 0 nums))

(add-any-numbers)
(add-any-numbers 1)
(add-any-numbers 1 2)
(add-any-numbers 1 2 3 4 5)


;; Multiple _and_ Variable airites, combined
;; - Guess what + actually is inside?
;;
(clojure.repl/source +) ; evaluate, check the REPL/LightTable console
;;
;; We can implement each arity as a special case, to compute results
;; as optimally as possible.

(+)
(+ 1)
(+ 1 2 3 4 5 6 7 8 9 0)



;; "De-structuring"
;; - For convenient access to items in collections.


;; Suppose a function expects a two-item sequence, we can...

(defn print-tuple-in-strange-ways
  [[a b]] ; expects a two-item vector
  (println "-----------------")
  (println b)
  (println a)
  (println "abba " [a b b a])
  (println "hash b a " {:b b :a a})
  (println b a a b a a " black sheep"))

(print-tuple-in-strange-ways [1 2])

;; It's like visually matching shapes to shapes.
;; - [1 2] ; structure 1 and 2 in a vector
;;    | |
;; - [a b] ; name by position, and use each named value however we wish
;;
;; Said another way:
;; - When we put data into a data structure, we... structure the data.
;; - When we follow the structure of the data structure, but
;;   reference each item by name, and "unpack" it for use, we have
;;   just "de-structured" the data.


;; De-structuring works in `let` and functions:

(let [[k v] [:a 42]]
  {:a 42})

((fn [[k v]] {k v})  [:a 42])


;; We can mix-and match de-structuring, for great good.
;; Compare:

(reduce (fn [acc-map kv-pair]
          (assoc acc-map
                 (first kv-pair) (second kv-pair)))
        {:a 42}
        {:b 0 :c 7 :d 10})

(reduce (fn [acc-map [k v]]
          (assoc acc-map k v))
        {:a 42}
        {:b 0 :c 7 :d 10})


;; Vectors are ordered collections, which we de-structure by _position_.

;; Hash-maps are _unordered_ collections.
;; - BUT, they are keyed by named keys.
;; - We can exploit this "pattern" as follows:

;; Compare this:
(let [make-message (fn [planet]
                     (str "Planet " (:pname planet) " has "
                          (:moons planet) " moons."))]
  (make-message
   {:pname "Mars" :moons 2}))


;; With this...
(let [make-message (fn [{:keys [pname moons]}]
                     (str "Planet " pname " has "
                          moons " moons."))]
  (make-message
   {:pname "Mars" :moons 2}))


;; And we can further...
(let [make-message (fn [{:keys [pname moons]}]
                     (str "Planet " pname " has "
                          (or moons 0) " moons."))]
  (map make-message [{:pname "Earth" :moons 1}
                     {:pname "Mars"  :moons 2}
                     {:pname "Moonless"}]))


;; We can also alias the whole hash-map:

(let [add-message (fn [{:keys [pname moons]
                        :as planet}] ; alias the hash-map as `planet`
                    (assoc planet
                           :message (str "Planet " pname " has "
                                         (or moons 0) " moons.")))]
  (map add-message [{:pname "Earth" :moons 1}
                    {:pname "Mars"  :moons 2}
                    {:pname "Moonless"}]))


;; Finally, we can specify default values directly in the destructuring:
(let [add-message (fn [{:keys [pname moons]
                        :or {moons 0} ; use 0, if :moons is absent
                        :as planet}]
                    (assoc planet
                           :message (str "Planet " pname " has "
                                         moons " moons.")))]
  (map add-message [{:pname "Earth" :moons 1}
                    {:pname "Mars"  :moons 2}
                    {:pname "Moonless"}]))


;; Further, we can exploit combinations of de-structuring
;;
;; - Suppose we have a hash map, keyed by planet names:
;;
{"Earth"    {:moons 1}
 "Mars"     {:moons 2}
 "Moonless" {}}
;;
;; - Recall: a hash-map is like a collection of key-value pairs/tuples
;;
;; - Now, we can exploit vector and map de-structuring, in combination:
;;
(let [add-message-fn
      ;; (fn [acc-map [k v]] (body... )) for use in reduce
      ;;  [acc-map [k     v is further destructured]]
      (fn [acc-map [pname {:keys [moons]
                           :or {moons 0}
                           :as pdata}]]
        (let [msg (str "Planet " pname " has " moons " moons.")]
          (assoc acc-map
                 pname (assoc pdata :message msg))))]
  (reduce add-message-fn
          {} ; acc-map
          {"Earth" {:moons 1}
           "Mars"  {:moons 2}
           "Moonless" {}
           "Nomoon" nil}))


;; There are _many_ many ways of de-structuring.
;; - Here's a really nice post detailing it:
;;   cf. http://blog.jayfields.com/2010/07/clojure-destructuring.html



;; RECAP:
;; - Clojure values are immutable by default, and we prefer it that way
;;
;; - `def` is best used only to define names for truly global values.
;;
;; - `defn` is just a wrapper over `def`, designed specifically to
;;    define functions
;;
;; - We exploit lexical scope to bind values as close as possible to the
;;   point of use in code. This greatly improves our ability to reason
;;   about our code. And it prevents an explosion of global `def`s.
;;
;; - Write pure functions as far as possible, write pure
;;
;; - Conveniences like multi-arity and variable-arity functions, with
;;   argument de-structuring, help us design better functional APIs.
;;
;; - We can mix-and match these facilities, for even more convenience.
;;
;; - Next, we will see how to "keep state at the boundary", and
;;   keep the majority of our core logic purely functional.
