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
