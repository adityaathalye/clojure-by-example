(ns clojure-by-example.ex02-small-functions)

;; Ex02: LESSON GOALS
;;
;; - Learn to define simple functions, and use them
;; - Learn a little bit about how functions behave
;; - Learn about a few useful built-in Clojure functions
;;   (We will use these in later exercises.)
;; - Stitch up ideas in this section with a small insight
;;   into how we can use functions on vectors and hash-maps.
;;   (Set up your intuition, for later exercises.)


;; First, some simple functions:


;; We define functions like this:
;;
;; (defn function-name
;;   "Documentation string (optional)."
;;   [arg1 arg2 arg3 ... argN]
;;   (body of the function))


;; `defn` stands for:
;; - "DEfine a FuNction,
;; - _and_ give it a globally-referenced name"


(defn same
  "Simply return the input unchanged."
  [x]
  x)

;; EXERCISE
;;
;; What will these return?

(same 42)

(same {:name "Earth" :moons 1})

(same [1 2 3 4 5])


;; We can define name-less functions too, like this:
;;
;; (fn [arg1 arg2 arg3 ... argN]
;;     (body of the function))
;;
;; `fn` is short for "define a FuNction, but _do not_ name it at all"
;;
;; E.g. This behaves _exactly_ like `same`, but it does not have a name:

(fn [x] x)



;; EXERCISE:
;;
;; What will these return?
;; - Hint: _mentally replace_ the function definition `(fn [x] x)`
;;   with _any name you like_, and imagine that is the function name.

((fn [x] x) 42)

((fn [x] x) {:name "Earth" :moons 1})

((fn [x] x) [1 2 3 4 5])


;; We will make good use of such "anonymous" functions soon.
;; Just understand they are just like 'regular' functions, except
;; they do not have a globally-referenced name (hence "anonymous").


;; Reminder: Do More With Less!
;; - Observe that `same` as well as `(fn [x] x)` appear to be capable
;;   of accepting _any_ kind of value and returning it.
;; - This function definition is very general-purpose indeed.
;; - We will frequently use this "general-purpose" idea.


;; Some "built-in" functions

;; Clojure has a function called `fn?`, that returns true if we
;; pass it something that is a function, and false otherwise.

;; None of these are functions. So, return `false`.

(fn? 42)

(fn? "moons")

(fn? [1 2 3 4 5])


;; We defined the function `same`, and truly, it is a function...
(fn? same)

;; Our "anonymous" function, is a function too. But of course!
(fn? (fn [x] x))

;; And `fn?` itself is a... ______ ?
(fn? fn?)


;; Wait a minute...!!!
;;
;; We just passed functions as arguments to `fn?`, and it worked!
;;
;; Is `fn?` special, or can we pass any function to any function?

;; Well we know `fn?` is a function...
;; - Suppose we pass `fn?` to `same`, do we get back `fn?` unchanged?

(= fn?
   (same fn?))


;; How about the anonymous version of `same`?
;; Does it return `fn?` unchanged as well?
(= 'FIX
   ((fn [x] x) fn?))

;; How about this?
;; What do we get if we pass `same` to itself?
(= 'FIX
   (same same))


;; Lesson: Functions also behave like "values":
;;
;; - `42` is a value. `"moons"` is a value. `:moons` is a value.
;;   - Values represent themselves directly
;;   - Values are unique in the whole universe (42 is NOT "forty two")
;;   - Values never change. They are constant forever and ever.
;;
;; - All Clojure function also are "values", including user-defined
;;   functions (like `same`). And, just like the other values...
;;   - We can pass functions as arguments to other functions,
;;     without any special syntax or declarations.
;;   - Functions can _return_ functions as results.
;;   - We can compare any two functions and tell if they are
;;     exactly the same thing.


;; Some other "built-in" Clojure functions

;; Return `true` if even, `false` otherwise
(even? 2)

;; Return `true` if odd, `false` otherwise
(odd? 3)

;; Increment by one
(inc 42)

;; Clojure's `identity` function is exactly like our `same` function
(= 'FIX
   (identity :moon)
   (same :moon))


;; `map` a function over a collection...
(map inc [1 2 3 4 5 6])

;; Such that...
( 1 2 3 4 5 6)  ; each item of input
;;| | | | | |   ; is incremented by `inc` to give a result where
( 2 3 4 5 6 7)  ; each output item "maps" back to an input item

;; In general, we can use `map` to express one-to-one "mappings"
;; of input and output, by way of a function.



;; EXERCISE:
;;
;; What should this return?
;; First predict the answer, then evaluate to confirm.
(map even? [1 2 3 4 5 6])
;; Hint: mentally apply the `even?` function to each item, one by one,
;; and build up a collection of results of each function application.


;; How about this?

(map odd? [1 2 3 4 5 6])


;; And this?

(map identity [1 2 3 4 5 6]) ; Recall: `identity` is just like `same`


;; And this?

(map (fn [x] x) [1 2 3 4 5 6])

;; Our anonymous "identity" function is a drop-in replacement
;; for `identity`, as well as `same`.



;; Now for a little bit of fun with `map` and some planets.


;; This is a Clojure vector... of numbers

[1 2 3 4 5 6]


;; This is a Clojure vector... of Clojure hash-maps.

[{:name "Mercury" :moons 0}
 {:name "Venus"   :moons 0}
 {:name "Earth"   :moons 1}
 {:name "Mars"    :moons 2}] ; (Yes we can do this. More on this later!)


;; Let's name our collection of planets as, um... `planets`

(def planets [{:name "Mercury" :moons 0}
              {:name "Venus"   :moons 0}
              {:name "Earth"   :moons 1}
              {:name "Mars"    :moons 2}])


;; Recall that we can query a map, like this:

(:name {:name "Mercury" :moons 0})

;; That is, a keyword _behaves like a function_ of a hash-map.


;; EXERCISE
;;
;; What if we pass a keyword instead of a real function, to `map`?
;; - What should the following map expression return?
;; - Predict the answer, and then evaluate to confirm.

(map :name planets)

;; (map :name over `planets`, which is vector of planet hash-maps)



;; Stitching it all together...


;; Let's define another little function.

(defn planet-has-moons?
  "Given a 'planet' (assume it's a hash-map), return true if
  it has at least one moon."
  [planet]
  (> (:moons planet)
     0))

(planet-has-moons? {:name "Mercury" :moons 0})

(planet-has-moons? {:name "Earth" :moons 1})

(planet-has-moons? {:name "Mars" :moons 2})


;; EXERCISE
;;
;; Instead of querying each map, why not query all of them at one go?

#_(map 'FIX 'FIX)


;; Also, as we now know, we can use anonymous functions creatively...


;; EXERCISE
;;
;; Replace 'FIX with your own anonymous function that works
;; just like `planet-has-moons?`.

#_(map 'FIX  planets)


;; EXERCISE
;;
;; And, finally, prove that both variants do exactly the same thing:

#_(= (map planet-has-moons? 'FIX)

     ('FIX 'FIX 'FIX)  ; use anonymous function

     [false false true true])



;; RECAP:
;; - Functions are easy to define
;; - Functions can _accept_ functions as arguments
;; - Functions can _return_ functions as arguments
;; - Clojure has nifty "built-in" functions
;;   - e.g. the function `map` lets us express a mapping of an
;;     input collection to an output collection, by way of a function.


;; DO MORE WITH LESS:
;; - Even the most simple functions can be general-purpose
;; - Clojure keywords behave like functions of hash-maps
;; - Since keywords can query hash-maps, we can completely avoid writing
;;   custom "getter" functions to query values in our hash-maps.
;; - We can put hash-maps in vectors, to make vectors of hash-maps.
;; - Since `map` accepts keywords in place of functions, we can
;;   combine the above tiny set of ideas, to query many planetary hash-
;;   maps at one go.
