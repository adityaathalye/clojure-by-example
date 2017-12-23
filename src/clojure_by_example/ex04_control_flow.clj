(ns clojure-by-example.ex04-control-flow)


;; WORK IN PROGRESS...


;; Ex04: LESSON GOALS
;;
;; - Introduce different ways to do data-processing logic in Clojure
;;   - with branching control structures (if, when, case, cond)
;;   - without branching structures (we have already sneakily done this)
;;   - predicates and boolean expressions
;;
;; - Have some more fun with much more sophisticated planets,
;;   using control structures, and the stuff we learned so far


;; The logical base for logic:

;; Boolean

(true? true)  ; `true` is boolean true

(true? false) ; `false` is boolean false


;; Falsey

;; `nil` is the only non-boolean "falsey" value


;; Truthy
;; - basically any non-nil value is truthy

42    ; truthy
:a    ; truthy
"foo" ; truthy
[7 3] ; truthy


;; Truthy/Falsey are NOT Boolean

(true? 42)   ; Is Truthy 42 a boolean true?

(false? nil) ; Is Falsey nil a boolean false?


;; Truthy/Falsey can be cast to boolean true/false

(boolean nil) ; coerce nil to `false`

(map boolean
     [42 :a "foo" [1 2 3 4]]) ; coerce non-nils to `true`


;; However, we normally don't need to coerce booleans, to do branching logic,
;; as Clojure control structures understand truthy and falsey values too:

;; false is, well, false

(if false   ; if     condition
  :hello    ; "then" expression
  :bye-bye) ; "else" expression


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


;; MENTAL EXERCISES
;;
;; Mental exercises to develop your intuition for how we use
;; "proper" booleans as well as truthy/falsey-ness.


;; EXERCISE:
;;
;; Predict what will happen...

(map (fn [x] (if x :hi :bye))
     [1 2 nil 4 5 nil 7 8])



;; EXERCISE:
;;
;; Predict what will happen...

(reduce (fn [acc x] (if x (inc acc) acc))
        0 ; initial accumulator
        [1 2 nil 4 5 nil 7 8])


;; EXERCISE:
;;
;; Predict and compare the result of these two...

(filter nil?     [1 2 nil 4 5 nil 7 8])

(filter false?   [1 2 nil 4 5 nil 7 8])


;; EXERCISE:
;;
;; Predict and compare these three...

(map    identity
        [1 2 nil 4 5 nil 7 8])

(filter (comp not nil?) ; recall: `comp` composes functions into a pipeline
        [1 2 nil 4 5 nil 7 8])

(filter identity
        [1 2 nil 4 5 nil 7 8]) ;; Ha! What happened here?!


;; INTERLUDE...
;;
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
;; But don't fear it.
;;
;; `nil` isn't the Enemy.
;; Fear is.
;;
;; Wield `nil` as
;; a double-edged sword.
;; For it cuts both ways.
;;
;; Ignore this,
;; and you will know
;; true suffering.


;; Good - `filter` knows `nil` is falsey

(filter identity
        [1 2 nil 4 5 nil 7 8])

;; Evil - `even?` cannot handle nothing... so, this fails:

#_(filter even?
        [1 2 nil 4 5 nil 7 8])

;; So... Guard functions like `even?` against the evil of nil

(filter (fn [x] (when x (even? x)))
        [1 2 nil 4 5 nil 7 8])

;; `fnil` is also handy, to "patch" nil input to a function

;; We might use it as a guard for `even?` like this:
((fnil even? 1) nil) ; pass 1 to `even?`, instead of nil
((fnil even? 2) nil) ; pass 2 to `even?`, instead of nil

;; Since we want `nil` to be non-even, we can nil-patch `even?` as:
(filter (fnil even? 1)
        [1 2 nil 4 5 nil 7 8])



;; Lesson:
;; - Keep `nil` handling in mind, when you write your own functions.


;; Demonstration:
;;
;; - It's possible to use `nil` for good, and make life easier.
;;
;; - How might someone use `nil` to advantage?

(def planets [{:name "Venus" :moons 0}
              {:name "Mars" :moons 2}
              {:name "Jupiter" :moons 69}])

;; Using `when` ... we might design a function:

(defn moon-or-nothing
  [planet]
  ;; Recall: we can "let-bind" local variables
  (let [num-moons (:moons planet)]
    (when (> num-moons 0)
      {:sent-rockets num-moons
       :to-moons-of (:name planet)})))

(moon-or-nothing {:name "Venus" :moons 0})


;; Later, someone may ask us...
(defn good-heavens-what-did-you-do?
  [rocket-info]
  (if rocket-info ; we will treat rocket-info as truthy/falsey
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
      {:sent-rockets num-moons
       :to-moons-of (:name planet)}
      "Bust!")))


;; And later, somebody wants to know from us...

#_(defn good-heavens-what-did-you-do-again???
     [rocket-info]
   ;; Fix to ensure the same output as we produced earlier.
   (if 'FIX
     'FIX
     'FIX))


;; We should be able to provide the same answers as before...

#_(map good-heavens-what-did-you-do-again???
     (map moon-or-bust planets))



;; `case` and `cond`
;; - are also available to do branching logic:

(map (fn [num-moons]
       ;; Use `cond` when you have to decide what to do based on
       ;; testing the value of a thing.
       (cond
         (nil? num-moons) "Do nothing!"
         (zero? num-moons)   "Send zero rockets."
         (= num-moons 1)   "Send a rocket."
         :else (str "Send " num-moons " rockets!")))

     [nil 0 1 42])


(map (fn [num-moons]
       ;; Use case when you can decide what to do based on the
       ;; actual value of a thing.
       (case num-moons
         nil "Do nothing!"
         0   "Send zero rockets."
         1   "Send a rocket."
         (str "Send " num-moons " rockets!"))) ; default expression

     [nil 0 1 42])


;; EXERCISE:
;;
;; Try to reason from first principles:
;;
;; Why does cond require `:else` to mark the last / default condition,
;; but case simply treats the last expression as default?
;;
;; (Hint: is `:else` an expression or a value?)


;; Clojure hash-sets
;; - can be used as predicates (and often are used this way)

(= #{:a :b :c}  ; A hash-set of three things, :a, :b, and :c.

   (hash-set :a :b :b :c :a :c :c :c))


;; A hash-set behaves like a function to test for set membership.

(#{:a :b :c} :a) ; Does the set contain :a? Truthy.

(#{:a :b :c} :z) ; Does the set contain :z? Falsey.


;; How do Clojure programmers use sets as predicates?

(def colonize-it? #{"Earth" "Mars"})


((comp colonize-it? :name) {:name "Earth"})


((comp colonize-it? :name) {:name "Venus"})


(filter (comp colonize-it? :name)
        [{:name "Mercury"} {:name "Venus"} {:name "Earth"}
         {:name "Mars"} {:name "Jupiter"}])



;; Lesson-end exercise


;; LET'S COLONIZE PLANETS!!!!
;; \\//_


(def target-planets
  [{:name "Mercury"
    :mass 0.055 :radius 0.383 :moons 0
    :atmosphere {}} ; empty hash map means no atmosphere

   {:name "Venus"
    :mass 0.815 :radius 0.949 :moons 0
    :atmosphere {:carbon-dioxide 96.45 :nitrogen 3.45
                 :sulphur-dioxide 0.015 :traces 0.095}}

   {:name "Earth" :mass 1 :radius 1 :moons 1
    :atmosphere {:nitrogen 78.08 :oxygen 20.95 :carbon-dioxide 0.4}
    :water-vapour 0.10 :argon 0.33 :traces 0.14}

   {:name "Mars" :mass 0.107 :radius 0.532 :moons 2
    :atmosphere {:carbon-dioxide 95.97 :argon 1.93 :nitrogen 1.89
                 :oxygen 0.146 :carbon-monoxide 0.056 :traces 0.008}}

   {:name "Chlorine Planet"
    :mass 2.5 :radius 1.3 :moons 4
    :atmosphere {:chlorine 100.0}}

   {:name "Insane Planet"
    :mass 4.2 :radius 1.42 :moons 42
    :atmosphere {:sulphur-dioxide 80.0 :carbon-monoxide 10.0
                 :chlorine 5.0 :nitrogen 5.0}}])


;; EXERCISE:
;;
;; Define a set of `poison-gases`
;; - Let's say :chlorine, :sulphur-dioxide, :carbon-monoxide are poisons


;; EXERCISE:
;;
;; Write a "predicate" function to check if a given planet is "Earth".
;;

(defn earth?
  [planet]
  'FIX)


;; EXERCISE:
;;
;; Write a predicate function to check if a planet has
;; at least 0.1% :carbon-dioxide in its atmosphere.

(defn carbon-dioxide?
  [planet]
  'FIX)


;; EXERCISE:
;;
;; Having no atmosphere is a bad thing, you know.
;;
;; Write a "predicate" function that returns true (or truthy)
;; if a planet has no atmosphere. It should return false/falsey
;; if the planet _has_ an atmosphere.
;;
;; Call it `no-atmosphere?`
;;
;; Use `empty?` to check if the value of :atmosphere is empty.
(empty? {}) ; is true. It's an empty hash-map.
;;
;; Type your solution below


;; Quick-n-dirty test
#_(filter no-atmosphere? target-planets)



;; EXERCISE:
;;
;; Let's say the air is too poisonous if the atmosphere contains
;; over 1% of _any_ poison gas.
;;
;; Write a "predicate" function that checks this, given a planet.
;;
;; Call it `air-too-poisonous?`.



;; Quick-n-dirty test
#_(filter air-too-poisonous? planets)



;; EXERCISE:
;;
;; Understand the next few functions.


(defn planet-has-some-good-conds?
  "Given a collection of functions that check a planet for 'good conditions',
  return true if a given planet satisfies at least one 'good condition'."
  [good-condition-fns planet]
  (some (fn [good?] (good? planet))
        good-condition-fns))

;; Quick-n-dirty test:
;; - Let's say it's good to be Earth (yay), and
;; - It's good to have carbon dioxide in the atmosphere.

#_(filter (fn [planet]
            (planet-has-some-good-conds?
              [earth? carbon-dioxide?] planet))
          target-planets)

;; OR we could use `partial`:

#_(filter (partial planet-has-some-good-conds?
                   [earth? carbon-dioxide?])
          target-planets)

;; What does `partial` do?
;; - Retrieve documentation for `partial` using `clojure.repl/doc`.
;;   (More on "REPL" utilities later.)

(clojure.repl/doc partial) ; evaluate and check the console/repl window

;; Then fix these to make them work:

#_((partial (fn [a b c] (+ a b c))
            1)
   'FIX 'FIX)

#_((partial (fn [a b c] (+ a b c))
            1 2)
   'FIX)


;; EXERCISE:
;;
;; Given a collection of bad conditions, and a planet, return true if
;; the planet has _no_ bad conditions.

(defn planet-has-no-bad-conds?
  [bad-condition-fns planet]
  'FIX)

;; Quick-n-dirty test:
#_(filter (partial planet-has-no-bad-conds?
                   [air-too-poisonous? no-atmosphere?])
          target-planets)

#_(filter (complement
            (partial planet-has-no-bad-conds?
                     [air-too-poisonous? no-atmosphere?]))
          target-planets)



;; EXERCISE:
;;
;; Let's say that a habitable planet has some good conditions,
;; and NO bad conditions.
;;
;; Define a function that checks true/truthy for this, given
;; good-condition-fns, bad-condition-fns, and a planet.

(defn habitable-planet?
  [FIX]
  'FIX)


;; EXERCISE:
;;
;; And finally, write a function that groups a given collection of
;; planets into :habitable, and :inhospitable.
;;
;; The function must internally know what functions will check for
;; for good conditions, what functions check for bad conditions.
;;
;; - Assume it's good to be earth, OR to have carbon-dioxide in the air.
;; - Assume it's bad to have no atmosphere, or to have poison gases.
;;
;; Hint: here's your chance to make good use of `let`, `partial`,
;; and `complement`.

(defn group-by-habitable
  [FIX]
  'FIX)


;; Quick-n-dirty test:
(map (comp (fn [p-name] (str "Send rockets to " p-name " now!"))
           :name)
     ((comp :habitable group-by-habitable)
      target-planets))



;; RECAP:
;; FIXME
