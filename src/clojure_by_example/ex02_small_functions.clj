(ns clojure-by-example.ex02-small-functions ; current namespace (ns)
  ;; "require" and alias another ns as `p`:
  (:require [clojure-by-example.data.planets :as p]))


;; Ex02: LESSON GOALS
;; - Use stuff we've seen so far to build purely functional logic
;;   to process a bunch of planets


;; Here are some target planets:
clojure-by-example.data.planets/target-planets

;; Which we can access more conveniently as:
p/target-planets

(map :pname p/target-planets)

;; Now, let's colonize planets!


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Some constants and utilities
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


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


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Functions to test a planet for particular conditions
;; - We make them return truthy/falsey values
;; - We call such functions "predicates"
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn atmosphere-present?
  [{:keys [atmosphere] :as planet}]
  (not-empty atmosphere))

#_(map :pname
       (filter atmosphere-present? p/target-planets))


(defn co2-tolerable?
  [{{:keys [carbon-dioxide]
     :or {carbon-dioxide 0.0}} :atmosphere
    :as planet}]
  (<= (lower-bound :co2)
      carbon-dioxide
      (upper-bound :co2)))

#_(map :pname
       (filter co2-tolerable? p/target-planets))


(defn gravity-tolerable?
  [{:keys [gravity] :as planet}]
  (when gravity
    (<= (lower-bound :gravity)
        gravity
        (upper-bound :gravity))))

#_(map :pname
       (filter gravity-tolerable? p/target-planets))


(defn surface-temp-tolerable?
  [{{:keys [low high]} :surface-temp-deg-c
    :as planet}]
  (when (and low high)
    (<= (lower-bound :surface-temp-deg-c)
        low
        high
        (upper-bound :surface-temp-deg-c))))

#_(map :pname
       (filter surface-temp-tolerable? p/target-planets))


(def poison-gas?
  "A set of poison gases."
  #{:chlorine, :sulphur-dioxide, :carbon-monoxide})



;; We can use sets as predicates. Sets behave as functions
;; that test for set membership.

(poison-gas? :oxygen)   ; falsey

(poison-gas? :chlorine) ; truthy


(defn air-too-poisonus?
  [{:keys [atmosphere] :as planet}]
  (some (fn [[gas-name gas-pct]]
          (and (poison-gas? gas-name)
               (<= 1.0 gas-pct)))
        atmosphere))

(map :pname
     (filter air-too-poisonus? p/target-planets))

;; a hash-map is a collection of key-value pairs
(map identity
     {:nitrogen 78.08, :oxygen 20.95, :carbon-dioxide 0.4,
      :water-vapour 0.1, :argon 0.33, :traces 0.14})

(map (fn [[k v]] (str v "% " k))
      {:nitrogen 78.08, :oxygen 20.95, :carbon-dioxide 0.4,
       :water-vapour 0.1, :argon 0.33, :traces 0.14})



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Composite checks to
;; - test whether a given planet meets a variety of conditions.
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn planetary-conditions-meet-constraint?
  "Return the given planet as-is, when it meets the condition fns,
  as per the constraint fn (like every?, some, not-every).

  The constraint fn must return a Boolean true/false value."
  [constraint]
  (fn [conditions planet]
    (when (true? (constraint (fn [f] (f planet))
                             conditions))
      planet)))


(def planet-meets-any-one-condition?
  (planetary-conditions-meet-constraint?
   some))


(def planet-meets-all-conditions?
  (planetary-conditions-meet-constraint?
   every?))


(def planet-meets-no-condition?
  (planetary-conditions-meet-constraint?
   not-any?))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Conditions for colonisation
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
   air-too-poisonus?])


(defn habitable?
  "We deem a planet habitable, if it has all minimally good conditions,
  and no fatal conditions."
  [planet]
  (planet-meets-no-condition?
   fatal-conditions
   (planet-meets-all-conditions?
    minimal-good-conditions
    planet)))


#_(map :pname
       (filter habitable? p/target-planets))


(defn colonisable?
  "We deem a planet colonisable, if it has at least one
  minimally good condition, and no fatal conditions."
  [planet]
  (and (planet-meets-any-one-condition?
        minimal-good-conditions
        planet)
       (planet-meets-no-condition?
        fatal-conditions
        planet)))

#_(map :pname
       (filter colonisable? p/target-planets))


(defn observe-only?
  "We select a planet for orbital observation, if it only has harsh surface conditions."
  [planet]
  (and (planet-meets-any-one-condition?
        fatal-conditions
        planet)
       (planet-meets-no-condition?
        minimal-good-conditions
        planet)))

#_(map :pname
       (filter observe-only? p/target-planets))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Enrich planetary data with analytical results
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn analyze-planet
  [planet]
  (cond
    (habitable? planet) :inhabit
    (colonisable? planet) :colonise
    (observe-only? planet) :observe-only
    :else :send-probes))


#_(map (juxt :pname analyze-planet)
       p/target-planets)

(map analyze-planet p/target-planets)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Starfleet assigns mission vessels...
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defmulti assign-vessels ; multi-method definition
  analyze-planet)        ; dispatch function


(defmethod assign-vessels :inhabit [planet]
  (assoc planet
         :vessels {:starships 5, :orbiters 5, :probes 100,
                   :battle-cruisers 5, :cargo-ships 5}))


(defmethod assign-vessels :colonise [planet]
  (assoc planet
         :vessels {:starships 1, :probes 50}))


(defmethod assign-vessels :send-probes [planet]
  (assoc planet
         :vessels {:orbiters 1, :probes 10}))


(defmethod assign-vessels :observe-only [planet]
  (assoc planet
         :vessels {:orbiters 1}))


#_(map (juxt :pname analyze-planet :vessels)
       (map assign-vessels p/target-planets))
