(ns clojure-by-example.ex01-small-beginnings)


;; IMPORTANT:
;; - the README file explains why I have created this project
;; - begin in this "ex01..." file, and work through it step by step
;; - once you are done with "ex01...", open the next file and repeat
;; - and so on, until you have worked through all the files


;; Ex01: LESSON GOAL:
;; - Show a way to model things with pure data, in the form of hash-maps
;; - Show how to query hash-maps
;; - Introduce the idea of a function
;; - Use the above to show how we can "do more with less"


;; Our Earth

;; "name"   "Earth"
;; "mass"   1 ; if Earth mass is 1, Jupiter's mass is 317.8 x Earth's mass
;; "radius" 1 ; if Earth radius is 1, Jupiter's radius is 11.21 x Earth's radius
;; "moons"  1
;; "atmosphere" "nitrogen"     78.08
;;              "oxygen"       20.95
;;              "CO2"           0.40
;;              "water-vapour"  0.10
;;              "other-gases"   "argon"  0.33
;;                              "traces" 0.14


;; Looks like a collection of name-value pairs. To some, it will look like JSON.

;; This intuition is correct. We can describe the Earth by its properties,
;; written as name-value pairs or "key"-value pairs.

;; If we put curly braces in the right places, it becomes a Clojure "hash-map".
{"name" "Earth"
 "mass" 1
 "radius" 1
 "moons" 1
 "atmosphere" {"nitrogen"     78.08
               "oxygen"       20.95
               "CO2"           0.40
               "water-vapour"  0.10
               "other-gases"  {"argon"  0.33
                               "traces" 0.14}}}


;; Let's query the Earth.

;; But first, let's create a global reference to our hash-map, for convenience.

;; Let's call it 'earth'. (Warning: such global 'def's are best avoided.)

(def earth {"name" "Earth"
            "mass" 1
            "radius" 1
            "moons" 1
            "atmosphere" {"nitrogen"     78.08
                          "oxygen"       20.95
                          "CO2"           0.40
                          "water-vapour"  0.10
                          "other-gases"   {"argon"  0.33
                                           "traces" 0.14}}}) ; <- evaluate this
;; Evaluate the above `def`. Place the cursor just after the closing paren `)`,
;; and evaluate it using your editor's evaluate feature.

;; Evaluation will attach (or 'bind') the hash-map to the symbol called 'earth'.


;; _Now_ let's query the 'earth' global...


;; Top-level access

;; Wait! What do you _expect_ 'get' to do?
;; Try to predict, before you evaluate.
(get earth "name") ; <- place cursor after closing paren and evaluate.


;; Predict and try another...
(get earth "moons")


;; So, given a hash-map and a "key", `get` returns the value associated with the key.

;; What do you expect 'get' will return here?
(get earth "atmosphere")


;; Now, further, predict the result of the next expression.
;; Hint: Mentally replace the inner get with its result you saw above.
(get (get earth "atmosphere") "other-gases")


;; Even more nested access. What did you get?
(get (get (get earth "atmosphere") "other-gases") "argon")


;; Note: We may choose to indent a deeply nested expression, for clarity.
(get (get (get earth "atmosphere")
          "other-gases")
     "argon")

;; Let's make our own function to access any "third" level value
(defn get-level-3 [planet level1-key level2-key level3-key]
  (get (get (get planet level1-key)
            level2-key)
       level3-key))


;; Now we can...
(get-level-3 earth "atmosphere" "other-gases" "argon")
(get-level-3 earth "atmosphere" "other-gases" "traces")



;; Hash-maps so widely-used, and such a convenient way to represent things,
;; that Clojure provides a far more convenient way to define and query them.

;; Instead of plain old strings as keys, we can use Clojure "keywords" as keys.
"moons" ; a string
:moons  ; a keyword

;; Unlike strings, keywords are designed to do special things.

;; But first, let's define an alternative hash-map, with keywords as keys...
;; - that represents the same data about the Earth,
;; - and is super-convenient to query because of the specialty of keywords
(def earth-alt {:name "Earth"
                :mass 1
                :radius 1
                :moons 1
                :atmosphere {:nitrogen 78.08
                             :oxygen 20.95
                             :CO2 0.4
                             :water-vapour 0.10
                             :other-gases {:argon 0.33
                                           :traces 0.14}}})

;; Easier top-level access

;; What will these return?
(:name earth-alt)
(:mass earth-alt)

;; What will this return?
(:atmosphere earth-alt)

;; Easier nested access
(:argon (:other-gases (:atmosphere earth-alt)))


;; By the way, the function we defined is general enough to work with keywords too!
(get-level-3 earth-alt :atmosphere :other-gases :argon)



;; Nested access is so common, we Clojure provides `get-in`, which is the cousin
;; of `get` (and the granddaddy of our get-level-3 function ;-)
(get-in earth-alt [:atmosphere])
(get-in earth-alt [:atmosphere :other-gases])
(get-in earth-alt [:atmosphere :other-gases :argon])

;; Try replacing the keywords with strings, like:
(get-in earth-alt ["atmosphere"])


;; Did that work?


;; The square bracketed things are Clojure "vectors", similar to arrays in
;; other languages. These are `indexed` collections, i.e. we can query a value
;; in a Vector, if we know what position it occupies in the vector.
["atmosphere"] ; is a vector of one string
[:atmosphere :other-gases]  ; is a vector of two keywords

;; What will this return?
(get [:foo :bar :baz] 0) ; We count position starting at `0`, not `1`, in Clojure

;; What will this return?
(get-in [:foo [:bar :baz]] [1 1])


;; `get` and `get-in` are general enough to query Vectors too, using index number.
;; BUT, we rarely use vectors this way. If we need key-value look-up, a hash-map
;; is almost always the right way to model the thing we need to query.


;; What if we model the earth as a vector, instead of a hash-map?
(def earth-as-a-vector
  "Docstring: This vector represents Earth this way: [Name, Radius, Mass, Moons]"
  ["Earth" 1 1 1])


;; Now, how do we query Earth?


(defn get-earth-name [] ; zero arguments means we don't need to pass it anything
  (get earth-as-a-vector 0))

(get-earth-name)

(defn get-earth-radius []
  (get earth-as-a-vector 1))

(get-earth-radius)

(defn get-earth-moons []
  (get earth-as-a-vector 2)) ; Uh, was it 2 or 3?

(get-earth-moons) ; did this return Mass, or Moons?

(:moons earth-alt) ; compare: how obviously we can query :moons in earth-alt


;; Further, our custom "getter" functions for Earth's properties,
;; are useless for other planets we may wish to define


;; While nobody stops us from doing this, the vector approach to modeling Earth
;; is clearly awkward.
;; - We must maintain label/name information about the values, separately
;;   (perhaps in the docstring)
;; - Our custom `get` functions are also far too "specialized", i.e. we can only
;;   sensibly use them to query the earth.
;; - And it opens us up to a whole host of errors:
;;   - we can easily lose track of what value represents what property
;;   - what if someone decides to add the number of man-made satellites between
;;     mass and moon?


;; DOING MORE WITH LESS

;; Clojure programmers rely on the power, and general-purpose flexibility of
;; hash-maps, as well as general-purpose functions, to avoid getting stuck
;; in such situations.


;; RECAP:

;; - hash-maps let us conveniently represent things we wish to model and query
;; - We can query hash-maps variously, with :keywords, `get`, and `get-in`
;; - If we use keywords as keys in hash-maps, querying is greatly simplified
;; - We can define our own functions with `defn`, using the following syntax:
;;   (defn function-name [arg1 arg2 arg3 ... argN]
;;         (body of the function))
;; - Using general-purpose data structures, and writing general-purpose
;;   functions lets us do more with less
