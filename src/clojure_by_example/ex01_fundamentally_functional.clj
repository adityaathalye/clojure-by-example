(ns clojure-by-example.ex01-fundamentally-functional)

;; EX01: LESSON GOAL:
;; - Realize that pure functions, and strict lexical scope
;;   are the bedrock upon which Clojure programs are built
;; - Drill how to use functions, and how lexical scope works
;; - Get comfortable with how functions compose together


;; Basic Function Syntax
;;
;; - Named functions:
;;
(defn function-name
  "Documentation string (optional)."
  [arg1  arg2  arg3  etc up to  argN]
  'function 'body
  'goes 'here
  '...)

;; - Anonymous functions:
;;
(fn [arg1  arg2  arg3  etc up to  argN]
  'function 'body
  'goes 'here
  '...)



;; A dead-simple function:

(defn same
  "Simply return the input unchanged."
  [x]
  x)


(fn [x] x) ; just like `same`, but with no name



;; EXERCISE
;;
;; Evaluate and see:

(same  42)

(same  [1 2 3 4 5])

(same  {:pname "Earth" :moons 1})


;; How about the anonymous version of `same`?
;; - What's the evaluation model?

((fn [x] x)  42)

((fn [x] x)  [1 2 3 4 5])

((fn [x] x)  {:pname "Earth" :moons 1})


;; What should happen here?
(= 'FIX
   (same same)
   ((fn [x] x) same))


;; `identity`
;; - provided by Clojure
;; - is exactly like our`same` function
;; - is extremely general (accepts any value)
;; - is surprisingly useful, as we will discover later

;; Fix this to prove `identity` and `same` are the same:
;; - Note: Functions are values and can therefore be compared.
;;
(= identity
   (same       identity)
   ((fn [x] x) identity)
   (identity   identity))
;;
;; Now, evaluate this in the REPL to _see_ the truth:
;; (clojure.repl/source identity)



;; And to round it up... functions can accept, as well as
;; return functions.

(defn gen-identity
  [] ; zero arguments
  identity)


(= identity
   (gen-identity))


(defn selfie
  "Given a function `f`, return the result of
  applying `f` to itself."
  [f]
  (f f))


(= 42
   (identity 42)
   ((selfie identity) 42)
   ((selfie (selfie identity)) 42)
   ((selfie (selfie (selfie identity))) 42)) ; ad-infinitum


;; Compose (chain) functions with `comp`
((comp inc inc inc) 39)

;; Negate predicates with `complement`
((complement string?) "hi")

;; Use `fnil` to "nil-patch" functions that cannot sanely handle nil (null) inputs
#_(+ nil 1) ; FAIL
((fnil + 0) nil 1) ; OK

;; Juxtapose functions with `juxt` (place results "side-by-side")
((juxt inc identity dec) 42)



;; Lexical Scope in Clojure


;; EXERCISE:
;; Reason about the following expressions.
;; - Mentally evaluate and predict the results; then check.

(def x 42)      ; Bind `x` to 42, globally ("top-level" binding)

(identity x)    ; obviously returns 42

((fn [x] x)  x) ; also returns 42, but how?

(let [x 10]     ; we use `let` to bind things locally
  (+ x 1))


;; EXERCISE:
;; Read carefully, and compare these three function variants:

(defn add-one-v1
  [x]
  (+ x 1)) ; which `x` will this `x` reference?

(add-one-v1  1) ; should evaluate to what?
(add-one-v1  x) ; should evaluate to what?


(defn add-one-v2
  [z]
  (+ x 1)) ; which `x` will this `x` reference?

(add-one-v2  1) ; should evaluate to what?
(add-one-v2  x) ; should evaluate to what?


(defn add-one-v3
  [x]
  (let [x 10]
    (+ x 1))) ; which `x` will this `x` reference?

(add-one-v3  1) ; should evaluate to what?
(add-one-v3  x) ; should evaluate to what?


;; `let`
;; - Mentally evaluate these, predict the results,
;;   and try to infer the scoping rule.
;; - Start with any `x`, and mechanically work
;;   your way around.

((fn [x] x)   (let [x 10]  x))


((fn [x] x)   (let [x  x]  x))


(let [x 10]   ((fn [x] x)  x))


((let [x 10]  (fn [x] x))  x)



;; Function Closure:
;; - Read carefully and work out what gets bound to `scale-by-PI`.

(defn scale-by
  "Given a number `x`, return a function that accepts
  another number `y`, and scales `y` by `x`."
  [x]
  (fn [y] (* y x)))

(def PI 3.141592653589793)

((scale-by PI) 10)


;; Achieve the same with `partial` application
(def scale-by-PI (partial * PI))

(scale-by-PI 10)


;; Strict lexical scope allows us to mechanically work out
;; where a value originated.
;; - Start at the place of reference of the value.
;; - Then "walk" outwards, until you meet the very first let binding,
;;   or argument list, or def, where the value was bound.
;; - Now you know where the value came from.



;; Sequences (or Collections), and operations on Sequences
;; - Clojure provides _many_ sequence functions.
;;   Here are some important ones:

map
;; Basic Syntax:
;;
;; (map a-function a-collection)
;;
;; Where the function must accept exactly one argument, because
;; it must transform only one item of the input at a time.

(map inc [1 2 3 4 5 6])
;;        | | | | | |   ; declare a mapping of each item of the input coll
;;        inc inc inc   ; via `inc`
;;        | | | | | |
;;       (2 3 4 5 6 7)  ; to each item of the output coll


filter
;; Basic Syntax:
;;
;; (filter a-predicate-fn a-collection)
;;
;; Where the function must accept exactly one argument and
;; return a truthy result (hence we term it a "predicate" function).

(filter even?    [1 2 3 4 5 6])

(filter identity [1 nil 3 nil 5 nil]) ; nil is falsey, non-nils are truthy


reduce
;; Basic Syntax:
;;
;; (reduce  a-function  accumulator  a-collection)
;;
;; Where the function must accept two arguments:
;; - first one is the value of the accumulator it manages, and
;; - the second one is bound to each item of the collection

(reduce + 0 [0 0 1 2])

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



;; Truthiness
;;
;; - Only `nil` and `false` are Falsey; everything else
;;   is Truthy
;; - a "predicate" function can return Truthy/Falsey,
;;   not just boolean true/false
;; - we can make good use of this behaviour, in Clojure


(def a-bunch-of-values
  [nil, false,                       ; falsey
   42, :a, "foo", true,              ; truthy
   {:a 1, :b 2}, [1 2 3 4],          ; truthy
   '(), {}, [], ""])                 ; truthy


;; A quick proof:
(map boolean ; coerces a given value to boolean true or false
     a-bunch-of-values)

(filter boolean
        a-bunch-of-values)


;; Branching logic accepts Truthy/Falsey

(if nil    ; if condition is Truthy
  "hi!"    ; then evaluate the first expression
  "boo!")  ; else evaluate the second expression


(when false   ; only when the condition is truthy
  "boo!")     ; evaluate the body. Otherwise, always return `nil`



;; RECAP:
;;
;; - All Clojure code is a bunch of "expressions"
;;   (literals, collections, s-expressions)
;;
;; - All Clojure expressions evaluate to a return value
;;
;; - All Clojure code is written in terms of its own data structures
;;
;; - All opening braces or parentheses must be matched by closing
;;   braces or parentheses, to create legal Clojure expressions.
