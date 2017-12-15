(ns clojure-by-example.ex04-control-flow
  (:require [clojure-by-example.ex03-data-and-functions
             :as ex03-data-and-functions]))


;; WORK IN PROGRESS...


;; Ex04: LESSON GOALS
;; - Introduce different ways to do data-processing logic in Clojure
;;   - with branching control structures ()
;;   - without branching structures (we have already sneakily done this)
;;   - predicates and boolean expressions
;; - Have some more fun with much more sophisticated planets,
;;   using control structures, and the stuff we learned so far


;; The logical base for logic:

;; `true` and `false`
true  ; boolean true
false ; boolean false


;; Falsey
nil   ; is the only non-boolean "falsey" value

;; Truthy
42    ; truthy
:a    ; truthy
"foo" ; truthy
[7 3] ; truthy
;; basically any non-nil value is truthy


;; Of course, falsey is NOT boolean `false`, and truthy is NOT boolean `true`
(true? 42)   ; 42 is not boolean true
(false? nil) ; nil is not boolean false


;; Truthy/Falsey can be cast to boolean true/false
(boolean nil) ; coerce nil to `false`
(map boolean [42 :a "foo" 3.141 [1 2 3 4]]) ; coerce non-nils to `true`


;; However, we normally don't need to coerce booleans, to do branching logic,
;; as Clojure control structures understand truthy and falsey values too:

;; false is, well, false
(if false
  :hello
  :bye-bye)

;; `nil` is falsey
(if nil
  :hello
  :bye-bye)

;; true is true, and every non-nil thing is truthy
(if true  :hello :bye-bye)
(if "Oi"  :hello :bye-bye)
(if 42    :hello :bye-bye)
(if [1 2] :hello :bye-bye)



;; `when` piggy-backs on the falsy-ness of `nil`
;; - when a condition is true, it evaluates the body and
;;   returns its value
;; - otherwise, it does nothing, and returns `nil`, i.e. _falsey_
(when 42
  :hello)

(when false :bye-bye)

(when nil :bye-bye)

(when (nil? nil) :bye-bye)



;; Mental exercises to develop your intuition for how we use
;; "proper" booleans as well as truthy/falsey-ness.


;; Predict what will happen...
(map (fn [x] (if x :hi :bye))
     [1 2 nil 4 5 nil 7 8])


;; Predict what will happen...
(reduce (fn [acc x] (if x (inc acc) acc))
        0 ; initial accumulator
        [1 2 nil 4 5 nil 7 8])


;; Predict and compare these two...
(filter nil?     [1 2 nil 4 5 nil 7 8])

(filter false?   [1 2 nil 4 5 nil 7 8])


;; Predict and compare these three...
(map    identity [1 2 nil 4 5 nil 7 8])

(filter (fn [x] (not (nil? x)))
        [1 2 nil 4 5 nil 7 8])

(filter identity [1 2 nil 4 5 nil 7 8])
;; Hah! What happened here?!


;; Interlude...


;; The logic and ill-logic of `nil` in Clojure
;;
;; `nil`
;;
;; is Good and Evil,
;; something and nothing,
;; dead and alive.
;;
;; Love it or hate it,
;; you _will_ face `nil`.
;; Sooner than later,
;; in Clojure.
;;
;; Embrace it.
;; Guard against it.
;;
;; Wield it as
;; a double-edged sword.
;; For it cuts both ways.
;;
;; Ignore this,
;; and you will know
;; true suffering.


;; Good - `filter` knows `nil` is falsey
(filter identity [1 2 nil 4 5 nil 7 8])

;; Evil - `even?` cannot handle nothing... so, this fails:
(filter even? [1 2 nil 4 5 nil 7 8])

;; So... Guard functions like `even?` against the evil of nil
(filter (fn [x] (when x (even? x)))
        [1 2 nil 4 5 nil 7 8])


;; Lesson:
;; - Keep `nil` handling in mind, when you write your own functions.
;; - It's possible to use `nil` for good, and make life easier.

;; Demonstration:
;; How might someone use `nil` to advantage?

(def planets [{:name "Venus" :moons 0}
              {:name "Mars" :moons 2}
              {:name "Jupiter" :moons 69}])

;; Using `when` ... we might design a function:

(defn moon-or-nothing [planet]
  (let [num-moons (:moons planet)]
    ;; Recall: we use `let` to bind local variables.
    (when (> num-moons 0)
      {:sent-rockets num-moons :to-moons-of (:name planet)})))


;; Later, someone may ask us...
(defn good-heavens-what-did-you-do?
  [rocket-info]
  (if rocket-info ; now we can treat rocket-info as truthy/falsey
    ;; do/return this if true...
    (format "I sent %d rockets to the moons of %s! Traa la laaa..."
            (:sent-rockets rocket-info)
            (:to-moons-of rocket-info))
    ;; do/return this if false...
    "Nothing."))


;; And we will answer...
(map good-heavens-what-did-you-do?
     (map moon-or-nothing planets))


;; But suppose, using `if` ... we design a function:
(defn moon-or-bust [planet]
  (let [num-moons (:moons planet)]
    (if (> num-moons 0)
      ;; do/return this if true...
      {:sent-rockets num-moons
       :to-moons-of (:name planet)}
      ;; do/return this if false...
      "Bust!")))


;; And later, somebody wants to know from us...
(defn good-heavens-what-did-you-do-again???
  [rocket-info]
  ;; Fix to ensure the same output as we produced earlier.
  (if ;; FIXME - condition to check
      ;; FIXME - expression if true
      ;; FIXME - expression if false
      ))


;; We should be able to provide the same answers as before...
(map good-heavens-what-did-you-do-again???
     (map moon-or-bust planets))







;; Scratch work follows....



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
