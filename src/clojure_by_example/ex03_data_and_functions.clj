(ns clojure-by-example.ex03-data-and-functions ; current namespace (ns)
  ;; "require" and alias another ns as `p`:
  (:require [clojure-by-example.data.planets :as p]))


;; Ex03: LESSON GOALS
;; - Explore various bits and bobs of the solution interactively
;;   using the live environment at your disposal
;; - Get some ideas of how to take just a handful of pieces,
;;   and build sophisticated logic with them
;; - Debug any issues that might arise
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
  [complement atmosphere-present?
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

;; Something's not right...The Office of Interstellar Affairs tells us we're not assigning vessels correctly?!
;; We've only deployed probes and orbiters, and no other vessels?!

;; We spent all this time and 300 lines of code to direct these vessels, and the orders aren't even correct!
;; We don't even see an error message! Clearly Clojure is the worst language ever made!

;; ...OR IS IT?

(comment
  ;; It's time to learn how Clojure allows us to debug and understand our programs, using nothing more
  ;; than the REPL and our wits.

  ;; Let's look at our results again, shall we? Are we really only deploying probes and orbiters?

  (map assign-vessels p/target-planets)

  ;; That's a bit hard to visually parse, how about this:

  (map :mission-vessels (map assign-vessels p/target-planets))

  ;; The OIA is right! But why is this happening?
  ;; Either our directive to fleet mapping is wrong, or our issued directives are wrong.

  starfleet-mission-configurations

  ;; The configurations look fine. What about the directives?

  (map :mission-directive (map assign-vessels p/target-planets))

  ;; We're only probing and observing! Clearly issue-mission-directive is at fault.
  ;; Let's take a look at its source code again.

  ;; Does this mean that there are no planets which our code considers habitable or colonisable?

  ;; EXERCISE:
  ;; Check whether we have any habitable or colonisable planets according to the habitable? and colonisable? predicates.

  ;; Apparently we don't!
  ;; At the very least, the planet Earth should be both habitable and colonisable.
  ;; At least now we know that habitable? and colonisable? are problematic. But why? Let's look at their implementation.
  ;; We'll narrow in on habitable? for the time being, and worry about colonisable? later.

  ;; A planet is habitable iff:
  ;; 1. It has an atmosphere
  ;; 2. The air is not too poisonous
  ;; 3. The carbon dioxide, gravity and temperature levels are all tolerable

  ;; The following issues are possible:
  ;; 1. planet-meets-any-one-condition? is broken.
  ;; 2. planet-meets-no-condition? is broken.
  ;; 3. minimal-good-conditions is broken.
  ;; 4. fatal-conditions is broken.
  ;; 5. Any or all of the above.

  ;; EXERCISE: Check if planet-meets-any-one-condition? works correctly.
  ;; planet-meets-any-one-condition? accepts predicates as a parameter, and doesn't care about the predicates
  ;; themselves. Because of this, we can simplify our debugging by using simple and obvious predicates,
  ;; rather than using the predicates in the production code.

  ;; EXERCISE: Check if planet-meets-no-condition? works correctly.

  ;; If none of those work, clearly there's something wrong with our conditions themselves.

  ;; EXERCISE: Diagnose and fix the broken conditions.

  ;; Does everything work now?

  (map assign-vessels p/target-planets)
  )

;;
;; RECAP
;; - Hopefully, you now have a better handle on the various aspects
;;   of working with Clojure, listed in the exercise goals; viz.
;;   - Reading: How to explore an unfamiliar Clojure code-base _interactively_?
;;   - "Primitives": How to get a lot done with just 20-odd core functions?
;;   - Concepts: What helps us model our domains and compose functional logic?
;;   - Workflow: How to apply the scientific method to development and debugging?
;;
;; - REPL all the things!
;;   Especially understand how the Clojure REPL is a powerful debugging tool
;;   that supersedes more traditional step-through debuggers in many ways.
;;
;; You can:
;; 1. Test individual functions or constants to check if they're correct.
;; 2. Redefine a function to add tracing such as print statements, or other forms of instrumentation.
;; 3. Capture intermediate values such as function arguments or let bindings, and inspect them in the REPL
;;    after the fact.
;; 4. Fix the problem and verify that it works immediately.
;; 5. Do all of the above either locally, or while connected to a remote server running in a staging or
;;    production environment.
;;
;; We strongly recommend going through https://clojure.org/guides/repl/enhancing_your_repl_workflow#debugging-tools-and-techniques
;; for more tips, tricks and resources related to debugging. The entire REPL guide is useful, but the section about debugging
;; is particularly pertinent.

;;
;; 4clojure Drills: Problems you could try now.
;;
;; - #protip: Write the solutions as proper named functions in your code base,
;;   without code-golfing or hacks. Then translate to anonymous function form
;;   that 4clojure requires.
(comment
  (map (fn [problem-no] (str "https://4clojure.oxal.org/#/problem/"
                             problem-no))
       [37, 64, 72, 21, 24, 25,
        38, 29, 42, 31, 81, 107,
        88, 157, 50, 46, 65]))
