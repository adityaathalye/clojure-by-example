(ns clojure-by-example.ex05-immutability-and-fp)

;; Ex05: Lesson Goals
;; - This section is more conceptual, than exercise-oriented.
;;
;; - Set you up with some important ideas, which we will use heavily
;;   in the final section (and in all our Clojure programs)
;;   - All values are immutable by default (and we like it this way)
;;   - What `def` is
;;   - Why you should avoid global defs
;;   - What are "pure functions"?
;;
;; - Don't forget to evaluate all s-expressions that interest you,
;;   and also feel free to write and play around with your own ones.



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
(assoc {:a 1 :b 2}
       :b 99)
;;
;; - And, finally, we can "dissociate" existing key-vals
(dissoc {:a 1 :b 2 :c 3}
        :b
        :c)


;; - So suppose we define...

(def planets [{:pname "Earth" :moons 1}
              {:pname "Mars" :moons 2}])

;; - Then, maybe, we can `assoc` a new k-v pair into all planets:
;; - And while we're at it, also dissoc an existing one:
;;
(map (fn [planet]
       (assoc (dissoc planet :moons)
         :habitable? true))
     planets)


;; EXERCISE:
;;
;; Predict the result of filtering by the value of `:habitable?`:

(filter :habitable? planets)

planets ; confirm by checking the value of this



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
;;
;; `def` creates a mutable reference. Technically, it is a way
;; "to maintain a persistent reference to a changing value".
;;
;; Note the difference:
;; - We can mutate the _reference_, but not the value.
;; - Values are by definition immutable.
;;
;; Warning:
;; - DO NOT use `def` to _emulate_ mutation.

;; Firstly:
;; - Because you'll cause errors of understanding:

(def weird-pi 3.141) ; bind weird-pi to 3.141

(def other-pi weird-pi) ; bind other-pi to the value of weird-pi,
                        ; which at this point is 3.141

(def weird-pi 42) ; re-bind weird-pi to some other value

weird-pi ; changes to 42

other-pi ; what should this be?

;; See the problem?
;; - We re-bound weird-pi to a new value, but other-pi's binding
;;   remained constant.
;; - Spread enough re-definitions across your program, and you'll be
;;   in trouble; unable to reason about who's using what version
;;   of the binding.


;; Secondly:
;; - It's dangerous because re-defining a var alters it globally.
;; - Why so dangerous?
;;
;; Well, compare the following.
;; - All three are _incorrect_, because pi is wrong.
;; - But only the third one is actually dangerous.

(defn scale-by-pi-v1
  [n]
  (let [pi 42]
    (* pi n)))

(defn scale-by-pi-v2
  [n]
  (* weird-pi n))

#_(defn scale-by-pi-v3
    [n]
    (def pi 42)
    (* n pi))


;; Also, remember functions are values?

(fn [x] x) ; is a value (which shall remain anonymous)

(defn same
  [x]
  x) ; `same` is the name of a function.
     ; Therefore `same` names a value.

;; So what if we:
(def same-same
  (fn [x] x)) ; hah!

;; As it happens:
;; - `defn` is really just a convenience wrapper over `def`.
;; - Because we can't live without defining functions, in Clojure-land

(macroexpand '(defn same [x] x)) ; yes, it is

;; So, truthfully:
;; - `same` and `same-same` are mutable references to immutable
;;   function definitions.
;;
;; Now:
;; - This mutable binding is very useful during development.
;; - It lets us interactively improve-and-rebind functions bit by bit.
;; - Stay in "flow".
;;
;; But:
;; - Imagine the horror of someone or something mutating the definition
;;   of your functions from under you, while your program is running!
;;
;; Yet:
;; - In extremely rare cases, the ability to re-define things in a
;;   live production system can be incredibly useful. Read the
;;   paragraph starting with "The Remote Agent software" on this page:
;;   http://flownet.com/gat/jpl-lisp.html
;;
;; Still:
;; - Don't try this in production, unless you really really really
;;   know what your are doing.


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


;; RECAP:
;;
;; - Clojure values are immutable by default, and we prefer it that way
;;
;; - `def` is best used only to define names for truly global values.
;;   Use `let` to bind local values, to get all the benefits of strict
;;   lexical scope.
;;
;; - `defn` is just a wrapper over `def`, designed specifically to
;;    define functions.
;;
;; - Write pure functions as far as possible.
