(ns clojure-by-example.ex01-small-beginnings)


;; IMPORTANT:
;; - the README file explains why I have created this project
;; - begin in this "ex01..." file, and work through it step by step
;; - once you are done with "ex01...", open the next file and repeat
;; - and so on, until you have worked through all the files


;; Ex01: LESSON GOAL:
;;
;; - Show a way to model things with pure data, in the form of hash-maps
;; - Show how to query hash-maps
;; - Introduce the idea of a function
;; - Use the above to show how we can "do more with less"


;; Our Earth

;; "name"   "Earth"
;; "mass"   1 ; if Earth mass is 1, Jupiter's mass is 317.8 x Earth
;; "radius" 1 ; if Earth radius is 1, Jupiter's radius is 11.21 x Earth
;; "moons"  1
;; "atmosphere" "nitrogen"     78.08
;;              "oxygen"       20.95
;;              "CO2"           0.40
;;              "water-vapour"  0.10
;;              "other-gases"   "argon"  0.33
;;                              "traces" 0.14


;; Looks like a collection of name-value pairs. To some, it will
;; look like JSON.
;;
;; This intuition is correct. We can describe the Earth by its
;; properties, written as name-value pairs or "key"-value pairs.

;; If we put curly braces in the right places, it becomes a
;; Clojure "hash-map":

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

;; But first, let's create a global reference to our hash-map,
;; for convenience.

;; Let's call it `earth`.


(def earth
  {"name" "Earth"
   "mass" 1
   "radius" 1
   "moons" 1
   "atmosphere" {"nitrogen"     78.08
                 "oxygen"       20.95
                 "CO2"           0.40
                 "water-vapour"  0.10
                 "other-gases"   {"argon"  0.33
                                  "traces" 0.14}}}) ; <- evaluate this
;; To evaluate the above `def`:
;; - Place the cursor just after the closing paren `)`, and
;; - evaluate it using your editor's evaluate feature
;;   (in LightTable, hit ctrl+Enter on Win/Linux, and cmd+Enter on Mac)

;; Evaluation will attach (or 'bind') the hash-map to the symbol
;; we have called `earth`.


;; _Now_ let's query the 'earth' global...


;; Top-level access:

;; Wait!
;;
;; What do you _expect_ 'get' to do, in the expression below?
;;
;; Try to predict, before you evaluate.

(get earth "name") ; <- place cursor after closing paren and evaluate.



;; EXERCISE:
;;
;; How to get number of moons?
;; - Fix the expression below:

(get earth 'FIX)


;; EXERCISE:
;;
;; What does the atmosphere contain?
;; - Type your expression below:


;; Lesson:
;; - Given a hash-map and a "key", `get` returns the value
;;   associated with the key.
;; - A value can be a string, or a number, or even another hash-map.



;; Nested access:

;; EXERCISE:
;;
;; Now, how to find what the other gases are, in the atmosphere?
;; - Hint: Mentally replace FIX with the value of "atmosphere".
;; - Now ask yourself, what expression will return that value?

(get 'FIX "other-gases")


;; EXERCISE:
;;
;; Now, try to go even deeper, to find how much argon we have?

(get 'FIX "argon")



;; Lesson:
;; - You can put expressions inside expressions and evaluate the
;;   whole thing as one expression.

;; Note:
;; - We may choose to indent a deeply nested expression, for clarity.
;;   (Ask your instructor to demonstrate.)



;; A Simple "Function"

;; Let's make our own function to access any "third" level value...

(defn get-level-3 ; function name
  [planet level1-key level2-key level3-key] ; arguments list
  ;; function "body":
  (get (get (get planet level1-key)
            level2-key)
       level3-key)) ; What does the function's "body" look like?



;; Now we can...
(get-level-3 earth "atmosphere" "other-gases" "argon")
(get-level-3 earth "atmosphere" "other-gases" "traces")



;; Keywords as Keys of Hash-maps

;; Hash-maps so widely-used, and can so conveniently represent things,
;; that Clojure provides a far more convenient way to define hash-maps
;; and query them.

;; Instead of plain old strings as keys, we can use
;; Clojure "keywords" as keys.

"moons" ; a string

:moons  ; a keyword


;; Like strings, keywords directly "represent" themselves.
;; (A keyword is what it is.)
;; _Unlike_ strings, keywords are designed to do special things.

;; To find out, we must first define an alternative hash-map,
;; - that represents the same data about the Earth,
;; - but with keywords as keys, instead of strings
;; - to let us super-easily query the hash-map, using just keywords
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

;; EXERCISE:
;;
;; What will these return?

(:name earth-alt)

(:mass earth-alt)


;; EXERCISE:
;;
;; How to find the atmosphere? Uncomment,fix, and evaluate:

('FIX earth-alt)


;; EXERCISE:
;;
;; What are the other gases, in the atmosphere?
;; Hint: Remember, we can nest expressions inside expressions.

('FIX 'FIX)


;; EXERCISE:
;;
;; How much argon is present in the atmosphere?
;; Hint: once again, more nested expressions.

('FIX 'FIX)


;; Clojure provides `get-in`, because nested access is so common.
;; - `get-in` is the cousin of `get` (and the granddaddy of our
;;    get-level-3 function!)

;; Try evaluating each one of these...
(get-in earth-alt [:atmosphere])
(get-in earth-alt [:atmosphere :other-gases])
(get-in earth-alt [:atmosphere :other-gases :argon])
;;                '--> imagine this as a "path" to the value


;; By the way, the function we defined earlier, is general enough
;; to work with keywords too!
(get-level-3 earth-alt :atmosphere :other-gases :argon)


;; EXERCISE:
;;
;; We saw `get-in` work for keywords. Does it work for strings too?
;; Uncomment, fix, and evaluate:

(get-in earth 'FIX)


;; Did that work? Why or why not?


;; EXERCISE:
;;
;; Use get-in to query other gases from the `earth` hash-map.
;; Type your expression below and evaluate it:



;; EXERCISE:
;;
;; Use get-in to query argon from `earth`'s atmosphere
;; Type your expression below and evaluate it:



;; Lesson:
;; - Given a hash-map and a path "key", `get` returns the value
;;   associated with the key.



;; Clojure "Vectors":

;; The square bracketed things we used with `get-in` are in fact a
;; Clojure datastructure. (Other languages may call these "Arrays".)
["atmosphere"] ; is a vector of one string
[:atmosphere :other-gases]  ; is a vector of two keywords


;; These are `indexed` collections, i.e. we can query a value in
;; a Vector, if we know what "index" position it occupies in the vector.


;; EXERCISE:
;;
;; What will this return?

(get [:foo :bar :baz] 0)
;; Note: We count position starting at `0`, not `1`, in Clojure


;; EXERCISE:
;;
;; What will this return?

(get-in [:foo [:bar :baz]] [1 1])


;; But we are actually just trying to find the "nth" item,
;; and Clojure gives us...
(nth [:foo :bar :baz] 0)

(nth (nth [:foo [:bar :baz]] 1)
     1)

;; Lesson:
;; - `get` and `get-in` are general enough to query Vectors too,
;;   using index number.
;; - but we'd rather just look up the `nth` item in Vectors


;; Basic Data Modeling:

;; We rarely use vectors to model objects like the Earth.
;; A hash-map is almost always the right way to model an object.
;; the thing we need to query.


;; What if we model the earth as a vector, instead of a hash-map?
(def earth-as-a-vector
  "Docstring: This vector represents Earth this way:
  [Name, Radius, Mass, Moons]"
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
;; are useless for other planets we may wish to define.


;; Lesson: Doing More With Less:

;; Clojure programmers rely on the power, and general-purpose
;; flexibility of hash-maps, as well as general-purpose functions,
;; to avoid getting stuck in such situations.

;; While nobody stops us from doing so, using vectors to model an object
;; (like the Earth) is clearly awkward.
;; - We must maintain label/name information about values separately
;;   (perhaps in the docstring)
;; - Our custom `get-xyz` functions are also far too "specialized",
;;   i.e. we can only sensibly use them to query _only_ the earth.
;; - And it opens us up to a whole host of errors:
;;   - we can easily lose track of what value represents what property
;;   - what if someone decides to add the number of man-made satellites
;;     between mass and moon?


;; Lesson-end Exercises:

;; EXERCISE:
;;
;; Define another planet `mercury`, using keywords as keys.
;; Use the information below.
;;
#_(;; FIXME
  "Mercury"    ; has...
  moons 0
  mass 0.0553  ; recall we assume Earth mass is 1
  radius 0.383 ; recall we assume Earth radius is 1
  atmosphere   ; % of total volume
      oxygen      42.0
      sodium      29.0
      hydrogen    22.0
      helium      6.0
      potassium   0.5
      other-gases 0.5)



;; EXERCISE:
;;
;; Query the planet `mercury` in 3 ways:
;; - with nested `get`
;; - with get-in
;; - with keywords
;; Type your solutions below:



;; EXERCISE:
;;
;; Write a custom function to do a two-level deep query on `mercury`.
;; - It should be able to query earth, and earth-alt as well.
;; Type your solution below:





;; RECAP:
;;
;; - hash-maps let us conveniently represent objects we wish to
;;   model and query
;; - We can query hash-maps variously with keywords, `get`, and `get-in`
;; - If we use keywords as keys in hash-maps, querying is dead-simple
;; - We can define our own functions with `defn`, using this syntax:
;;
;;   (defn function-name
;;         [arg1 arg2 arg3 ... argN]
;;         (body of the function))
;;
;; - Using general-purpose data structures, and writing general-purpose
;;   functions lets us do more with less
