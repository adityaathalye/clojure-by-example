(ns clojure-by-example.ex01-fundamentally-functional)

;; EX01: LESSON GOAL:
;; - Realize that pure functions, and strict lexical scope
;;   are the bedrock upon which Clojure programs are built
;; - Drill how to use functions, and how lexical scope works
;; - Get comfortable with how functions compose together
;; - At every step, further drill the interactive REPL workflow.
;;   Figure out how to take advantage of the immediate feedback
;;   that the live REPL gives you. Treat each exercise as a
;;   tiny experimental setup. Run small experiments that will
;;   help you discover answers...
;;   -> Read the exercise
;;   -> Make a testable guess (hypothesis)
;;   -> Evaluate your solution (test your hypothesis)
;;   -> Compare your guess with the solution
;;   -> If it differs, update your guess (or fix the solution) and redo
;;   That is, try to use the Scientific Method to solve exercises.


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

;; EXERCISE
;; How about the anonymous version of `same`?
;; - What's the evaluation model? Think before you tinker.
;; - Form your hypothesis -> Test it -> Learn from the feedback

((fn [x] x)  42)

((fn [x] x)  [1 2 3 4 5])

((fn [x] x)  {:pname "Earth" :moons 1})


;; EXERCISE
;; Fix the the following s-expr, so that it evaluates to true.
;; - First predict the solution in your head.
;; - Then replace 'FIX with your solution and evaluate to confirm.
;; - Think about the little experiment you just performed, and
;;   form a theory about why the solution worked
(= 'FIX
   (same same)
   ((fn [x] x) same))


;; `identity`
;; - provided by Clojure
;; - is exactly like our`same` function
;; - is extremely general (accepts any value)
;; - is surprisingly useful, as we will discover later

;; EXERCISE
;; Fix this to prove `identity`, `same`, and the anonymous
;; version of `same`, all do the exact same thing:
;; - Note: Functions are values and can therefore be compared.
;;
(= identity
   ('FIX identity)
   ('FIX identity)
   ('FIX identity))
;;
;; Now, evaluate this in the REPL to _see_ the truth:
;;
#_(clojure.repl/source identity)
;;
(comment
  ;; This is another example of what "dynamic" means.
  ;; We can not only can we interact live with small bits of
  ;; our Clojure programs, we can also examine many aspects
  ;; of our programs at run time. The clojure.repl namespace
  ;; is one tool at our disposal. Try these in the REPL:
  #_(clojure.repl/dir clojure.repl)
  #_(clojure.repl/doc clojure.repl)
  )


;; "Higher order" functions (HoFs):

;; Functions that:
;; - *accept* functions as arguments
;;   and/or
;; - *return* functions as results
;; are called "higher order" functions.


;; EXERCISE
;; Have we seen HoFs so far? If yes, list them out below.


;; EXERCISE
;; Write a zero-argument function that returns the `identity` function

(defn gen-identity
  [] ; zero arguments
  'FIX)

;; EXERCISE
;; Fix this function so that it returns a function that _behaves_
;; like the identity function (don't return `same`, or `identity`).

(defn gen-identity-v2
  []
  'FIX)

;; EXERCISE
;; Replace 'FIX1 with a call to the `gen-identity` function,
;; and 'FIX2 with a call to the `gen-identity-v2` function,
;; such that the following evaluates to true.

(= identity
   'FIX1
   'FIX2)


;; Composing Logic with Higher-order Functions (HoFs):
(comment
  ;; Clojure programmers often write simple functions that
  ;; each do one task well, and use higher order functions
  ;; to "compose" these in creative ways, to produce more
  ;; useful pieces of logic.
  ;;
  ;; We treat "simple" functions as building blocks, and
  ;; HoFs as versatile mini-blueprints that help us organize
  ;; and glue together the simple functions.
  )

;; EXERCISE
;; Reason about why this is working:

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


;; Let's play with a couple of nifty HoFs built into Clojure
;; - `comp`
;; - `complement`


;; EXERCISE
;; Use `(comp vec str inc)` to make the following true
;; - `comp` accepts any number of functions as arguments,
;;   and returns a function that behaves as a pipeline
;;   (or chain) of the given functions

(= [\4 \2]
   (vec (str (inc 41)))
   ('FIX 'FIX))

(comment
  ;; Reason about the order of evaluation and how inputs
  ;; and outputs should connect, for `comp` chains to
  ;; work correctly.
  ;;
  ;; To see if you reasoned correctly, try each of
  ;; seq, str, inc independently:
  (inc 41)                ; increment a number
  (str 42)                ; turn the input into a string
  (seq "42")              ; turns a string into a character sequence
  )


;; EXERCISE
;; Use `(complement string?)` to make the following true
;; - `complement` accepts a "predicate" function, and returns a
;;   function that does the opposite of the given "predicate"
(= (not (string? "hi"))
   ('FIX 'FIX))

(comment
  ;; "Predicate" is just a term we use to conveniently describe
  ;; any function that returns a truthy/falsey value, i.e.
  ;; any function that is used to test for some condition.
  ;; These so-called "predicates" are not inherently special.
)



;; "Lexical Scope" in Clojure
;; - Lexical scope guarantees that the reference to a value will be
;;   "enclosed" in the scope in which it is being used.

(comment
  ;; Strict lexical scope greatly simplifies our life, because
  ;; it allows us to mechanically follow code, and determine
  ;; where a value originated.
  ;; - Start at the place of reference of the value.
  ;; - Then "walk" outwards, until you meet the very first let binding,
  ;;   or arg-list, or def, where the value was bound.
  ;; - Now you know where the value came from.
  ;;
  ;; This also helps reduce our mental burden of inventing
  ;; new names to refer to things, because we can re-use
  ;; a name within a limited scope, and be certain that
  ;; it will not destroy anything with the same name outside
  ;; the given scope.
  )

;; EXERCISE:
;; - Develop an intuition for what "Lexical scope" might mean
;;   by reasoning about the following exercises.
;;
;; - Mentally evaluate and predict the results; then check.

(def x 42)      ; Bind `x` to 42, globally ("top-level" binding)

(identity x)    ; obviously returns 42

((fn [x] x)  x) ; also returns 42, but how?

(let [x 10]     ; We use `let` to bind things locally.
  x)            ; This evaluates to the value of the "let-bound" `x`.

(+ (let [x 10]
     x)
   x)           ; So, this whole thing should evaluate to what?


;; EXERCISE
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


;; EXERCISE
;; - Mentally evaluate the following, predict the results,
;;   and try to infer the scoping rule.
;; - Then evaluate each expression to see if your
;;   mental model agrees with the result you see.
;; - Start with any `x`, and mechanically work
;;   your way around.

((fn [x] x)   (let [x 10]  x))


((fn [x] x)   (let [x  x]  x))


(let [x 10]   ((fn [x] x)  x))


((let [x 10]  (fn [x] x))  x)


;; Function "Closure"
;; - This is a way for a function to capture and "close over"
;;   any value available at the time the function is defined

(def PI 3.141592653589793)

(defn scale-by-PI
  [n]
  (* n PI)) ; PI is captured within the body of `scale-by-PI`

(scale-by-PI 10)


;; A more general way to "scale by":
;; - Thanks to lexical scope + the function closure property

(defn scale-by
  "Given a number `x`, return a function that accepts
  another number `y`, and scales `y` by `x`."
  [x]
  (fn [y] (* y x))) ; whatever is passed as `x` is captured
                    ; within the body of the returned function


;; EXERCISE
;;
#_(= (scale-by-PI 10)
     ('FIX 10)
     (* PI 10))

(comment
  ;; BONUS EXERCISES
  ;; Define a few scaling functions, in terms of `scale-by`
  ;;
  (def scale-by-PI-v2
    'FIX)

  (def quadruple
    "4x the given number."
    'FIX)

  (def halve
    'FIX))


;; Sequences (or Collections)
;;
;; - and operations on Sequences
;; - Clojure provides _many_ sequence functions.
;;   Here are some important ones: `map`, `filter`, and `reduce`
;; - Observe that all these functions are HoFs!

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



;; RECAP
;; - Acquire a "scientific experimentation" mindset when
;;   interactively developing and debugging Clojure code
;;   ... The REPL is your friend.
;; - Learn to use lexical scope and function closures effectively.
;; - Learn to define small "single purpose" functions, such that
;;   you can compose them together to produce higher order logic.
