(ns clojure-by-example.utils.ex07-boldly-go)

;; WORK IN PROGRESS

;; Very speculative


;; EX07: Lesson Goals
;;
;; Quickly see how to start your own project:
;;
;; - Make a library for deep space exploration, using code from ex06
;; - Make a new lein project (perhaps a library utility)
;; - Copy over code from ex06
;;   - put I/O stuff under utils/io.clj
;;   - require/alias it in src/core.clj, where we put our pure logic
;;   - point out the ns forms, and :require forms
;; - Add a main function to core.clj
;; - Update project.clj (add deps? add AOT?)
;; - Clean, compile, uberjar
;;   - Show jar in target/ dir
;; - Run jar
;; - Get "consolidated planets" JSON, to use for colonization!!!


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
