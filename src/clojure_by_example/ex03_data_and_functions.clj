(ns clojure-by-example.ex03-data-and-functions ; current namespace (ns)
  ;; "require" and alias another ns as `p`:
  (:require [clojure-by-example.data.planets :as p]))


;; Ex03: LESSON GOALS
;; - Primarily, a code reading exercise
;; - Explore various bits and bobs of the solution interactively
;;   using the live environment at your disposal
;; - Get some ideas of how to take just a handful of pieces,
;;   and build sophisticated logic with them
;; - We use only the concepts and standard library functions
;;   we've seen so far, to build purely functional logic
;;   in order to process a bunch of planets:
;;
;;   - Standard Library (about 20 functions):
;;     `def`, `defn`, `fn`, `let`            ; to create/name simple data and small functions
;;     `get`, `get-in`, `assoc`              ; to query and associate data
;;     `map`, `filter`, `reduce`             ; to operate on collections
;;     `if`, `when`, `cond`                  ; to decide things
;;     `not`, `and`, `empty?`, `<=`, `count` ; for logic and quantities
;;     `comp`, `complement`                  ; to glue higher-order logic
;;
;;   - Concepts:
;;     - Compute only with pure functions:
;;       - Build higher-order logic with higher order functions
;;       - Lexical scope and function closures to maximize modularity
;;     - Collections as functions:
;;       - Keywords as functions of hash-maps
;;       - Well-defined Sets as predicates --- tests of set membership
;;     - Hash-maps and collections to model domain entities:
;;       - A planet, or atmospheric tolerances, or decision tables,
;;         or collections of analysis criteria
;;     - Truthy / Falsey logic:
;;       - Instead of only Boolean true/false
;;     - Namespaces:
;;       - Making use of things defined elsewhere
;;
;;   - Workflow:
;;     - Apply the Scientific Method to design, debug, and to understand
;;     - Run small fast experiments via the REPL
;;     - Preserve your experiments in-line within your codebase itself
;;
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Let's colonize planets!
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment
  ;; BACKGROUND
  ;;
  ;; The Office of Interstellar Affairs (OIA) is pushing hard
  ;; for all-out space exploration and colonization.
  ;;
  ;; The OIA intends to issue "mission directives"...
  ;;
  ;; They wish humanity to :inhabit, or :colonise, or :probe,
  ;; or :observe a given planet based on their analysis of
  ;; available planetary data.
  ;;
  ;; For a given "mission directive", like :probe, the OIA
  ;; intends to dispatch a collection of vessels.

  ;; GOAL
  ;;
  ;; Prototype a bit of planetary analysis logic, using criteria
  ;; that interest the OIA, such that they will be able to decide
  ;; what to do about a given planet.
  ;;
  ;; Criteria include questions like:
  ;; - co2-tolerable?
  ;; - gravity-tolerable?
  ;; - surface-temp-tolerable?
  ;;
  ;; How a planet stands up to such questions will let us assess
  ;; whether it is habitable? or colonisable? or observe-only?.
  ;;
  ;; Once we deliver the OIA our assessment, they may choose to
  ;; dispatch one or more kinds of Starfleet vessels to the planet.
  )

;; Here are some target planets:
clojure-by-example.data.planets/target-planets

;; Which we can access more conveniently as:
p/target-planets

(map :pname p/target-planets)



(def starfleet-mission-configurations
  "Associate 'mission directives' like :inhabit, :colonise, :probe,
  and 'mission configurations' of Starfleet vessels. e.g. If our
  analysis of a planet says :probe, then we would send 1 'Orbiter'
  class Starship carrying a complement of 100 autonomous probes."

  {:inhabit {:starships 5, :battle-cruisers 5,
             :orbiters 5,  :cargo-ships 5,
             :probes 30}

   :colonise {:starships 1, :probes 50}

   :probe {:orbiters 1, :probes 100}

   :observe {:orbiters 1, :probes 10}})



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic Planetary Analysis
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Some basic constants, utility functions, and "predicate"
;; functions to test a given planet for particular conditions.


(def tolerances
  "Define low/high bounds of planetary characteristics we care about."
  {:co2                {:low 0.1,  :high 5.0}
   :gravity            {:low 0.1,  :high 2.0}
   :surface-temp-deg-c {:low -125, :high 60}})


(def poison-gas?
  "A set of poison gases."
  #{:chlorine, :sulphur-dioxide, :carbon-monoxide})


(defn lower-bound
  [tolerance-key]
  (get-in tolerances [tolerance-key :low]))


(defn upper-bound
  [tolerance-key]
  (get-in tolerances [tolerance-key :high]))


(defn atmosphere-present?
  [planet]
  (not (empty? (:atmosphere planet))))

#_(map :pname
       (filter atmosphere-present? p/target-planets))


(defn co2-tolerable?
  [planet]
  (let [co2 (get-in planet
                    [:atmosphere :carbon-dioxide])]
    (when co2
      (<= (lower-bound :co2)
          co2
          (upper-bound :co2)))))

#_(map :pname
       (filter co2-tolerable? p/target-planets))


(defn gravity-tolerable?
  [planet]
  (when (:gravity planet)
    (<= (lower-bound :gravity)
        (:gravity planet)
        (upper-bound :gravity))))

#_(map :pname
       (filter gravity-tolerable? p/target-planets))


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

#_(map :pname
       (filter surface-temp-tolerable? p/target-planets))


(defn air-too-poisonous?
  "The atmosphere is too poisonous, if the concentration of
  any known poison gas exceeds 1.0% of atmospheric composition."
  [planet]
  (let [gas-too-poisonous? (fn [gas-key-pct-pair]
                             (and (poison-gas? (gas-key-pct-pair 0))
                                  (>= (gas-key-pct-pair 1) 1.0)))]
    (not
     (empty?
      (filter gas-too-poisonous?
              (:atmosphere planet))))))


(map :pname
     (filter air-too-poisonous? p/target-planets))

;; Note: a hash-map is a collection of key-value pairs
(map identity
     {:nitrogen 78.08, :oxygen 20.95, :carbon-dioxide 0.4,
      :water-vapour 0.1, :argon 0.33, :traces 0.14})

(map (fn [pair]
       (str (get pair 0) " % = " (get pair 1)))
      {:nitrogen 78.08, :oxygen 20.95, :carbon-dioxide 0.4,
       :water-vapour 0.1, :argon 0.33, :traces 0.14})



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Composite checks to perform on a given planet
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def minimal-good-conditions
  "A collection of functions that tell us about the
  good-ness of planetary conditions."
  [co2-tolerable?
   gravity-tolerable?
   surface-temp-tolerable?])


(def fatal-conditions
  "A collection of functions that tell us about the
  fatality of planetary conditions."
  [(complement atmosphere-present?)
   air-too-poisonous?])


(defn conditions-met
  "Return only those condition fns that a planet meets.
  An empty collection means no conditions were met."
  [condition-fns planet]
  (filter (fn [condition-fn]
            (condition-fn planet))
          condition-fns))


(defn planet-meets-no-condition?
  [conditions planet]
  (empty? (conditions-met conditions planet)))


(def planet-meets-any-one-condition?
  (complement planet-meets-no-condition?))


(defn planet-meets-all-conditions?
  [conditions planet]
  (= (count conditions)
     (count (conditions-met conditions planet))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Composite checks to
;; - test whether a given planet meets a variety of conditions.
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn habitable?
  "We deem a planet habitable, if it has all minimally good conditions,
  and no fatal conditions."
  [planet]
  (when (and (planet-meets-no-condition?
              fatal-conditions
              planet)
             (planet-meets-all-conditions?
              minimal-good-conditions
              planet))
    planet))

#_(map :pname
       (filter habitable? p/target-planets))


(defn colonisable?
  "We deem a planet colonisable, if it has at least one
  minimally good condition, and no fatal conditions."
  [planet]
  (when (and (planet-meets-any-one-condition?
              minimal-good-conditions
              planet)
             (planet-meets-no-condition?
              fatal-conditions
              planet))
    planet))

#_(map :pname
       (filter colonisable? p/target-planets))


(defn observe-only?
  "We select a planet for orbital observation, if it only has harsh surface conditions."
  [planet]
  (when (and (planet-meets-any-one-condition?
              fatal-conditions
              planet)
             (planet-meets-no-condition?
              minimal-good-conditions
              planet))
    planet))

#_(map :pname
       (filter observe-only? p/target-planets))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Enrich planetary data with Starfleet mission information
;; from the Office of Interstellar Affairs.
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn issue-mission-directive
  [planet]
  (cond
    (habitable? planet) :inhabit
    (colonisable? planet) :colonise
    (observe-only? planet) :observe
    :else :probe))


(defn assign-vessels
  [planet]
  (let [mission-directive (issue-mission-directive planet)]
    (assoc planet
           :mission-directive mission-directive
           :mission-vessels   (mission-directive starfleet-mission-configurations))))


#_(map assign-vessels p/target-planets)
