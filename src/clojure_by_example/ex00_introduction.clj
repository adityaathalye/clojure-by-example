(ns clojure-by-example.ex00-introduction)


;; IMPORTANT:
;; - The README file explains why this project exists.
;; - Begin in this "ex00..." file, and work through it step by step.
;; - Once you are done with "ex00...", open the next file and repeat.
;; - Keep going this way, until you have worked through all the files.


;; EX00: LESSON GOAL:
;; - A very quick intro to Clojure syntax, just to familiarize your
;;   eyes with it.
;;
;; - Don't get stuck here!
;;   - Run through it once, try evaluating expressions of interest
;;     and move on to EX01.
;;   - Your eyes and brain will adjust fairly quickly, as you
;;     work through the examples to follow.



;; Clojure is a "Lisp"
;; - Lisp is short for List Processing
;; - It's just another way to design a programming language
;;   (Ignore "But, Why?" for now... Just use it as it is, and try to
;;    do something practical with it.)



;; Clojure code is composed of "expressions":

;; These literal values are Clojure "expressions"
"hello"                                 ; strings
:hello                                  ; keywords
'hello                                  ; symbols
42                                      ; numbers
22/7                                    ; fractional numbers

;; "Built-in" functions are also "expressions"
;; - We will meet all of these again, very soon.
+                                       ; addition
map                                     ; map over a collection
filter                                  ; filter from a collection
reduce                                  ; transform a collection


;; Collection "literals" are expressions too:
;; - We will extensively use such "collection" data structures.
[1 2 3 4 5]                             ; a vector
{:a 1 :b 2}                             ; a hash-map
#{1 2 3 4 5}                            ; a hash-set
'(1 2 3 4 5)                            ; a list


;; Clojure code is also composed of expressions;
;; - we refer to them as "symbolic" expressions (or "s"-expression)

(+ 1 2) ; an s-expression

(+ (+ 1 2) (+ 1 2)) ; an s-expression of nested expressions

(+ (+ (+ 1 2) (+ 1 2))
   (+ (+ 1 2) (+ 1 2))) ; an even more nested s-expression


;; In fact, ALL Clojure code is just "expressions"
;; - And, all Clojure expressions evaluate to a value.
;;
;; - All literals evaluate to themselves. They are values.
;;   (Hence "literal": a literal is what it is. :-D)
;;
;; - All collection literals also evaluate to themselves.
;;   (A literal collection is what it is, too.)
;;
;; - All functions are values.
;;   (More on this a little later)
;;
;; - All s-expressions, however deeply nested, finally evaluate
;;   to a return value. Expressions evaluate to either a literal,
;;   or a collection, or a function.


;; Clojure expression syntax rules:
;;
;; - Literals:
;;   - Just write them down
;;
;; - Collection Literals:
;;   - Just write them down too, but also
;;   - make sure opening brackets are always matched by closing brackets
;;     [1 2 3]  is a well-formed vector representation
;;     [1 2 3   is an "unbalanced" vector and will cause an error.
;;
;; - Symbolic expressions ("s-expressions"):
;;   - Make sure the round parentheses close over the intended/required
;;     sub-expressions
;;     (+ 1 2)  is a well-formed expression that will be evaluated
;;     (+ 1 2   is an "unbalanced" s-expression and will cause an error.


;; Clojure Code Evaluation Rules:
;;
;; - To instruct Clojure to evaluate a list of expressions,
;;   enclose the expressions in round parentheses.
;;   - Recall: (+ 1 2)
;;
;; - The very first expression after an opening paren MUST be
;;   a function.
;;   - So: (1 2) will fail, because 1 is not a function
;;
;; - All expressions or sub-expressions that follow the first expression
;;   will first be fully evaluated into values, and then passed to
;;   the first expression as arguments.
;;   - Recall: (+ (+ (+ 1 2) (+ 1 2))
;;                (+ (+ 1 2) (+ 1 2)))
;;
;;   - You may mentally evaluate the above form "inside-out", like this:
;;     - Evaluate the smallest and innermost expressions first,
;;     - Mentally replace them with their return values
;;     - Pass those values as arguments to the next higher expression
;;     - Continue until you are left with a literal value.
;;
;;     (+ (+ (+ 1 2) (+ 1 2))
;;        (+ (+ 1 2) (+ 1 2)))
;;
;;     (+ (+  3       3     )
;;        (+  3       3     ))
;;
;;     (+ 6
;;        6)
;;
;;     12
;;
;;   - Keep this evaluation model in mind, when you read Clojure code,
;;     to figure out how the code will evaluate.
;;
;; - To prevent evaluation, explicitly mark an expression as a list
;;   '(1 2) put a single quote in front of an expression to tell Clojure
;;   you don't want it to evaluate that expression.
;;
;; - To comment out in-line comment text, or even an expression,
;;   place one or more semi-colons before the text/expression.
;;
;; - `#_` is a clean way to comment out multi-line s-expressions
;;   Compare this:
#_(+ 1 2
     3 4
     5 6)
;;   With this:
;; (+ 1 2
;;    3 4
;;    5 6)


;; Why is Clojure a "List Processing" language?

'(+ 1 2) ; Recall: this is a Clojure list, that Clojure evaluates
         ; as literal data.

(+ 1 2) ; if we remove the single quote, Clojure treats the same list
        ; as an executable list, and tries to evaluate it as code.


;; More generally, Clojure code, like other lisps, is written
;; in terms of its own data structures. For example:
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
