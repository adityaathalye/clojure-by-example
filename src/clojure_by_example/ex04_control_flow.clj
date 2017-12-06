(ns clojure-by-example.ex04-control-flow)


;; UGLY DRAFT ... DO NOT USE YET.


;; Ex04: LESSON GOALS
;; - Introduce different ways to do data-processing logic in Clojure
;;   - with branching control structures ()
;;   - without branching structures (we have already sneakily done this)
;;   - predicates and boolean expressions
;; - Have some more fun with much more sophisticated planets,
;;   using control structures, and the stuff we learned so far


;; This is a cleaner way to do the same thing...
(filter (comp not planet-has-moons?)
        planets)

;; `comp` is a handy function lets us "compose" or chain other functions
;; such that the output of the function on the right is connected to the
;; input of the function on the left.
;;
;; So, when you see an expression like:
;;    ((comp fn1 fn2 fn3 fn4) {:some "data"})
;;
;; you can mentally evaluated it like:
;;    FINAL RESULT <- fn1 <- fn2 <- fn3 <- fn4 <- {:some "data"}
;;
;; Which looks suspiciously like a "data pipeline", (or a Unix pipeline
;; for those familiar with Unix/Linux shell programming)

;; Comp, in fact, returns a general-purpose function, which can take
;; any input and pass it to the right-most function
(fn? (comp not planet-has-moons?))

;; True, Earth has a moon.
(planet-has-moons?
 {:name "Earth" :moons 1})

;; True, Mercury has no moons.
((comp not planet-has-moons?)
 {:name "Mercury" :moons 0})


;; Back to map...
(map (comp not planet-has-moons?)
     planets)

;; What if a function's output does not match
;; Well, your function pipeline misbehaves (or could even fail):

;; When a keyword can't find anything, it returns `nil`
;; So the whole thing below, returns `nil`, which is useless.
((comp :name planet-has-moons?) {:name "Earth" :moons 1})
