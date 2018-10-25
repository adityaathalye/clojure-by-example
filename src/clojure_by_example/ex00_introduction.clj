(ns clojure-by-example.ex00-introduction)


;; IMPORTANT:
;; - The README file explains why this project exists.
;; - Begin in this "ex00..." file, and work through it step by step.
;; - Once you are done with "ex00...", open the next file and repeat.
;; - Keep going this way, until you have worked through all the files.


;; EX00: LESSON GOAL:
;; - Drill some Clojure basics, to set up the sections
;;   to follow (and generally, to help grok code in the wild)
;; - Familiarize one's eyes with Clojure syntax
;; - Understand Clojure's evaluation model
;; - Introduce a handful of important concepts,
;;   data structures, expressions, and functions
;;   that are used pervasively.
;; - Start using an interactive development workflow
;;   right away



;; All Clojure code is composed of "expressions":
;; - And, all Clojure expressions evaluate to a value.

;; - "Atomic" literals:
"hello"                       ; strings
:hello                        ; keywords
\h \e \l \l \o                ; characters
'hello                        ; symbols
42                            ; numbers
22/7                          ; fractional numbers
nil                           ; yes, nil is a value

;; - Collection literals:
[1 2 3 4 5]                   ; a vector
{:a 1 :b 2}                   ; a hash-map (key-value pairs)
#{1 2 3 4 5}                  ; a hash-set
'(1 2 3 4 5)                  ; a list

;; - "Built-in" functions:
+                             ; addition
map                           ; map over a collection
filter                        ; filter from a collection
reduce                        ; transform a collection

;; - "Symbolic" expressions (or "s"-expression or s-expr)

(+ 1 2)                       ; a simple s-expr

(+ (+ 1 2) (+ 1 2))           ; an s-expr of nested s-exprs

(+ (+ (+ 1 2) (+ 1 2))
   (+ (+ 1 2) (+ 1 2)))       ; an even more nested s-expr

(defn same
  [x]
  x)                          ; function definitions are also s-exprs


;; Namespaces:
;;
;; - are how we organize and/or modularise code
;; - all Clojure code is defined and evaluated within namespaces

;; Evaluate and see:

map   ; is defined in the `clojure.core` ns (namespace)

same  ; is defined in the current ns

#_(ns-name *ns*) ; What's the current ns?


;; Clojure expression syntax rules:

;; - Literals:
;;   - Just write them down

;; - Collections and S-expressions:
;;   - Always. Be. Closing.

;;   [1 2 3]         ; OK
;;   [1 2 3          ; FAIL

;;   {:a 1 :b 2}     ; OK
;;   {:a 1 :b 2      ; FAIL

;;   (+ 1 2)         ; OK
;;   (+ 1 2          ; FAIL

;; - Indentation, extra spaces, and commas are just for
;;   our reading convenience. Example: all of the following
;;   literal maps represent the same value:

{:a 1   :b 2}

{:a 1,  :b 2}

{:a 1,
 :b 2}

{:a 1
 :b 2}

{:a 1
 :b
 2}



;; Clojure Expression Evaluation Rules:

(+  1  2)

;; - Wrap in parentheses to cause evaluation

;; - First position is special, and must be occupied by a function
;;   (1 2)   ; FAIL, because 1 is not a function

;; - All s-expressions, however deeply nested, finally evaluate
;;   to a return value (a literal, or a collection, or a function.)

;; - Mentally evaluate nested expressions "inside-out":
;;
(+ (+ (+ 1 2) (+ 1 2))
   (+ (+ 1 2) (+ 1 2)))

(+ (+  3       3     )
   (+  3       3     ))

(+ 6
   6)

12

;; - _Prevent_ evaluation of s-expression by "quoting" it,
;; i.e. explicitly marking a list:

'(+ 1 2) ; but the list will still remain in the evaluation path

;; - Prevent evaluation and _elide_ any s-expression
;;   (the special "#_" syntax is called a "reader macro").

(+   (+ (+ 1 2) (+ 1 2))
   #_(+ (+ 1 2) (+ 1 2))) ; elide the nested sub-expression from execution path

;; - Entirely _ignore_ code and free-form text by commenting out
;;   with one or more semicolons:
;   ( + 1 2
;;      3 4
;;;     5 6)



;; Why is Clojure a Lisp ("LISt Processing") language?

'(+ 1 2) ; Recall: this is a Clojure list, that Clojure evaluates
         ; as literal data.

(+ 1 2) ; if we remove the single quote, Clojure treats the same list
        ; as an executable list, and tries to evaluate it as code.


;; More generally, Clojure code is written in terms of Clojure's
;; own data structures. For example:
;;
;; Here is a function definition.
(defn hie
  [person message]
  (str "Hie, " person " : " message))

;; What does it look like?
;; - Let's flatten it into one line for illustrative purposes:


;;[1] [2]   [3]              [4]
(defn hie [person message] (str "Hie, " person " : " message)) ; [5]
;; Where:
;; - [1] `defn` is a Clojure built-in primitive
;;   - Notice, it's at the 1st position, and
;;   - 2-4 are all arguments to defn
;; Further:
;; - [2] is a Clojure symbol, `hello`, which will name the function
;; - [3] is a Clojure vector of two named arguments
;; - [4] is a Clojure s-expression, and is treated as the body of
;;       the function definition
;; - [5] the whole thing itself is a Clojure s-expression!



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
