(ns clojure-by-example.ex04-api-design
  (:require [clojure-by-example.data.planets :as p]))

;; EX04: Lesson Goals:
;; - See how to "de-structure" data (it's a powerful, flexible lookup mechanism)
;; - Leverage de-structuring to design a self-documenting function API


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; A tiny bit of "De-structuring"
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; If we _shape_ our domain information as a data "structure",
;; can we use our knowledge of the shape to pull it apart;
;; i.e. "destructure" it?

;; Yes.

;; We use destructuring:
;; - In `let` bindings, to cleanly reach into data
;; - In function arguments, to make the API clean and expressive


;; Here are a couple of commonly-used ways to do it.


;; "Positional" De-structuring
;;
;; - Pull apart sequential, ordered data structures like
;;   lists, vectors, and any other sequence with linear access
;;
;; - Follow the structure of the collection, and mechanically
;;   bind values to symbols by position.


;; Evaluate and see what happens. Then form a theory of what might be going on.
(def planet-names
  (map :pname p/target-planets))


(let [[pname1 pname2] planet-names]
  (str pname1 " is the 1st planet, and "
       pname2 " is the 2nd planet."))


(let [[:as pnames]  planet-names]
  pnames)


(let [[m v e :as pnames] (map :pname p/target-planets)]
  {:useless-trivia (str e " is the third rock from the Sun.")
   :planets-names pnames})


;; "Associative" De-structuring
;;
;; - Syntax to reach into associative data structures
;;   (having key-value semantics), in arbitrary ways.
;;
;; -  Note: Clojure "Records" and vectors are associative too
;;
;; - Follow the structure of the collection, and mechanically
;;   bind values to symbols by key name.

;; Evaluate one by one and see what happens.
;; Then form a theory of what might be going on.


(let [[mercury] p/target-planets]
  (str (:pname mercury) " has mass " (:mass mercury)))


(let [[{:keys [pname mass]}] p/target-planets]
  (str pname " has mass " mass))


(let [[{:keys [pname mass] :as mercury}] p/target-planets]
  (assoc mercury
         :useless-trivia (str pname " has mass " mass)))



;; And, putting it all together, a function with a
;; more self-documented API:

(defn add-useless-trivia
  "Be Captain Obvious. Given a planet, add some self-evident trivia to it."
  [{:keys [pname mass] :as planet}]
  (assoc planet
         :useless-trivia (str pname " has mass " mass)))


(map add-useless-trivia
     p/target-planets)

#_(clojure.repl/doc add-useless-trivia)


;; EXERCISE:
;; Review the de-structured argument list in `add-useless-trivia`;
;; then try to recall and reinforce a key concept.
;; Hints:
;; - Relate it to our preferred way to model the world.
;; - What is the "world" here?
;; - What are we using to model/describe what property of what?


;; EXERCISE:
;;
;; - Use de-structuring to refactor the following functions
;; that we have copied over from ex03.
;;
;; - Develop a preliminary opinion about where and when
;; it might makes sense to de-structure, and where and when
;; it might not.


(def tolerances
  "Define low/high bounds of planetary characteristics we care about."
  {:co2                {:low 0.1,  :high 5.0}
   :gravity            {:low 0.1,  :high 2.0}
   :surface-temp-deg-c {:low -125, :high 60}})


(defn lower-bound
  [tolerance-key]
  (get-in tolerances [tolerance-key :low]))


(defn upper-bound
  [tolerance-key]
  (get-in tolerances [tolerance-key :high]))


(defn atmosphere-present?
  [planet]
  (not (empty? (:atmosphere planet))))


(defn atmosphere-present?-refactored
  [FIXME]
  FIXME)

#_(= (map :pname
          (filter atmosphere-present? p/target-planets))
     (map :pname
          (filter atmosphere-present?-refactored
                  p/target-planets)))

(defn co2-tolerable?
  [planet]
  (let [co2 (get-in planet
                    [:atmosphere :carbon-dioxide])]
    (when co2
      (<= (lower-bound :co2)
          co2
          (upper-bound :co2)))))


(defn co2-tolerable?-refactored
  [FIXME]
  FIXME)

#_(= (map :pname
          (filter co2-tolerable? p/target-planets))
     (map :pname
          (filter co2-tolerable?-refactored
                  p/target-planets)))


;; EXERCISE:
;; - Fix the body of the refactored function
;; - Carefully review the function APIs, and develop a preliminary
;; opinion whether the refactored version is better than the original.


(defn surface-temp-tolerable?
  [planet]
  (let [temp (:surface-temp-deg-c planet)
        low  (:low temp)
        high (:high temp)]
    (when (and low high)
      (<= (lower-bound :surface-temp-deg-c)
          low
          high
          (upper-bound :surface-temp-deg-c)))))


(defn surface-temp-tolerable?-refactored
  [{:keys [FIXME] :as planet}]
  FIXME)


(defn surface-temp-tolerable?-refactored-v2
  [{{:keys [low high]} :surface-temp-deg-c
    :as planet}]
  'FIXME)


#_(= (map :pname
          (filter surface-temp-tolerable?
                  p/target-planets))
     (map :pname
          (filter surface-temp-tolerable?-refactored
                  p/target-planets))
     (map :pname
          (filter surface-temp-tolerable?-refactored-v2
                  p/target-planets)))


;; RECAP:
;;
;; - We model the world by composing data structures and then use
;; "de-structuring" to conveniently reach into those structures.
;; - When, where, and how much to de-structure is a matter of
;; taste; a design choice. There is no One True Way.
