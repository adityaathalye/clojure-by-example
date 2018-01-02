(ns clojure-by-example.ex08-but-before-we-go)

;; But before we boldly go, we have to cover errors, and REPL usage
;;
;; - Understand common errors, error messages, read Clojure stacktraces
;; - Discover some handy REPL utilities for power and convenience


;; Understanding Common Errors (and Error Messages!):
;;
;; - single-pass compiler gotcha: must def/defn things before using them
;; - ns declaration bugs
;; - Unbalanced parentheses
;; - Can't find variable/binding e.g. because s-expression is
;;   outside of lexical scope of a let binding, or because var does
;;   not exist.
;; - Unchecked nils
;; - Java errors that we did not try/catch
;; - ???


;; Debugging:
;;
;; - "Inside-out"/"Bottom-up" REPL-driven debugging, particularly how
;;    Stu Halloway explains, in "Debugging With the Scientific Method".
;;    https://github.com/stuarthalloway/presentations/wiki/Debugging-with-the-Scientific-Method
;;
;; - Aphyr's post is neat!
;;   - Scroll down to the "Debugging Clojure" section:
;;   https://aphyr.com/posts/319-clojure-from-the-ground-up-debugging


;; Handy REPL utils:
;;
;; - clojure.repl/
;;   - source, doc, pp, pprint, print-table
;;   - see the bottom of the `clojure-by-example.utils.core` ns
;;
;; - *1, *2, *3
;;
;; - A sane REPL workflow (buffer/file-based, rather than inside REPL)
;;
;; - ???
