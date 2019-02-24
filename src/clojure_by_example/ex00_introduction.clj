(ns clojure-by-example.ex00-introduction)


;; IMPORTANT:
;; - The README file explains why this project exists.
;; - Begin in this "ex00..." file, and work through it step by step.
;; - Once you are done with "ex00...", open the next file and repeat.
;; - Keep going this way, until you have worked through all the files.


;; EX00: LESSON GOAL:
;; - Drill some Clojure basics, required for the sections
;;   that follow (and generally, to help follow code
;;   in the wild)
;; - Familiarize one's eyes with Clojure syntax
;; - Understand Clojure's evaluation model
;; - Start using an interactive development workflow
;;   right away --- this is what it means to be a
;;   "Dynamic" programming language (not to be confused
;;   with dynamically typed languages.)


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
;; - All Clojure code is defined and evaluated within namespaces


;; EXERCISE:
;; Evaluate the following expressions and see what you get.
;; - First, type the expression in the REPL
;; - Next, evaluate them straight from your codebase

map   ; is defined in the `clojure.core` ns (namespace)

same  ; is defined in the current ns

#_(ns-name *ns*) ; What's the current ns?

(comment
  ;; PROTIP:
  ;;
  ;; Your IDE or text editor would have a convenient shortcut to
  ;; evaluate any Clojure expression right from your codebase.
  ;;
  ;; Some editors allow you to "evaluate in-line", some would
  ;; tell you to "send to the REPL". Consult the documentation
  ;; that accompanies your editor's Clojure plugin, to learn
  ;; how to do this.
  ;;
  ;; Make a habit of interacting "dynamically" with Clojure
  ;; this way, right from inside your codebase; i.e. prefer
  ;;_not_ to type things directly into the REPL.
  )



;; Clojure expression syntax rules:

;; - Literals:
;;   - Just write them down

;; - Collection literals and s-expressions:
;;   - ABC - Always. Be. Closing. :-D
;;   - The Clojure "Reader" (the 'R' part of the R.E.P.L)
;;     expects each open bracket to be accompanied by a
;;     corresponding closing bracket. i.e. all parentheses
;;     must be "balanced".

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

;; - Wrap in parentheses to cause evaluation.
;;   The first position is special, and must be
;;   occupied by a function

(+ 1 2)    ; OK
;; (1 2)   ; FAIL, because 1 is not a function

;; - Mentally evaluate nested expressions "inside-out".
;;   Usually, all s-expressions--however deeply nested--evaluate
;;   to a return value; a literal, or a collection, or a function,
;;   or some legal object.

(+ (+ (+ 1 2) (+ 1 2))
   (+ (+ 1 2) (+ 1 2)))

(+ (+  3       3     )
   (+  3       3     ))

(+ 6
   6)

12

;; - _Prevent_ evaluation of s-expr by "quoting" it,
;;   i.e. explicitly marking a list, by prefixing it
;;   with a single quote `'`:

'(+ 1 2) ; BUT the list will still remain in the evaluation path

;; - Leave an s-expression in-line, but remove it from
;;   the evaluation path, by prefixing it with `#_`:

(+ 1 2 #_(+ 1 2))  ; will evaluate to 3

;; - Comment out entirely, by prefixing code with one or more
;;   semicolons, just like in-line comments.

;; (+ (+ 1 2)
;;    (+ 1 2)) ; fully commented out


;; EXERCISE:
;; - Now, why will the following expression fail (throw an exception)?
;;   Make an educated guess, then try it.

;; (+ 1 2 '(+ 1 2)) ; un-comment and evaluate; then comment it back


(comment
  ;; PROTIP:
  ;;
  ;; The special "#_" syntax is called a "reader macro".
  ;;
  ;; For now, ignore what that means, just know the effect of
  ;; using it. You will see #_ often in code to follow.
  ;;
  ;; Incidentally, the single quote we used '(to mark a list)
  ;; is also a reader macro. Many more specialized reader macros
  ;; are available, but don't go there just yet.
  )



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

;;[1] [2] [3]              [4]
(defn hie [person message] (str "Hie, " person " : " message)) ; [5]

(comment
  ;; Here:
  ;; - [1] `defn` is a Clojure built-in primitive
  ;;   - Notice, it's at the 1st position, and
  ;;   - 2-4 are all arguments to defn
  ;; Further:
  ;; - [2] is a Clojure symbol, `hie`, which will name the function
  ;; - [3] is a Clojure vector of two named arguments
  ;; - [4] is a Clojure s-expression, and is treated as the body of
  ;;       the function definition
  ;; - [5] the whole thing itself is a Clojure s-expression!
  )


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
