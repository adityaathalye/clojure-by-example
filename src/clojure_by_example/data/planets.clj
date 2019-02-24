(ns clojure-by-example.data.planets)

(def target-planets
  [{:pname "Mercury"
    :mass 0.055
    :radius 0.383
    :moons 0
    :gravity 0.378
    :surface-pressure 0
    :surface-temp-deg-c {:low -170 :high 449}
    :rocky? true
    :atmosphere {}} ; empty hash map means no atmosphere

   {:pname "Venus"
    :mass 0.815
    :radius 0.949
    :moons 0
    :gravity 0.907
    :surface-pressure 92
    :surface-temp-deg-c {:low 465 :high 465}
    :rocky? true
    :atmosphere {:carbon-dioxide 96.45 :nitrogen 3.45
                 :sulphur-dioxide 0.015 :traces 0.095}}

   {:pname "Earth"
    :mass 1
    :radius 1
    :moons 1
    :gravity 1
    :surface-pressure 1
    :surface-temp-deg-c {:low -89 :high 58}
    :rocky? true
    :atmosphere {:nitrogen 78.08 :oxygen 20.95 :carbon-dioxide 0.4
                 :water-vapour 0.10 :argon 0.33 :traces 0.14}}

   {:pname "Mars"
    :mass 0.107
    :radius 0.532
    :moons 2
    :gravity 0.377
    :surface-pressure 0.01
    :surface-temp-deg-c {:low -125 :high 20}
    :rocky? true
    :atmosphere {:carbon-dioxide 95.97 :argon 1.93 :nitrogen 1.89
                 :oxygen 0.146 :carbon-monoxide 0.056 :traces 0.008}}

   {:pname "Chlorine Planet"
    :mass 2.5
    :radius 1.3
    :moons 4
    :gravity 1.5
    :surface-pressure 1
    :surface-temp-deg-c {:low -42 :high 24}
    :rocky? true
    :atmosphere {:chlorine 100.0}}

   {:pname "Insane Planet"
    :mass 42
    :radius 4.2
    :moons 42
    :gravity 10
    :surface-pressure 420
    :surface-temp-deg-c {:low 750 :high 750}
    :rocky? false
    :atmosphere {:sulphur-dioxide 80.0 :carbon-monoxide 10.0
                 :chlorine 5.0 :nitrogen 5.0}}])
