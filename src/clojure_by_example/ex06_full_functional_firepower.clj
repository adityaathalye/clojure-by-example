(ns clojure-by-example.ex06-full-functional-firepower)


;; WORK IN PROGRESS


;; Ex06: Lesson Goals
;; - This is more of a code-reading section, designed to:
;;
;; - Show how to "keep state at the boundary"; e.g.:
;;   - ingest sophisticated planets from a JSON file
;;   ----------------Input boundary----------------
;;   - punch it into a purely functional data processing pipeline
;;   - do awesome things with just
;;     - `map`, `filter`, `reduce`,
;;     - simple helper functions (with/without control flow), and
;;     - models of the world as pure data structures
;;   ----------------Output boundary---------------
;;   - spit out various kinds of outputs into output files
;;
;;
;; - Show where mutability is useful and desirable; e.g. I/O
;;   (but where it isn't we try hard to avoid it)
;;
;; - Also give a feel for how we can use all the lessons learned
;;   so far, to design flexible function APIs for good "composability".




;; Previously, in Clojure By Example...
;;
;; We lamented our immutable fate.
;;
;; WHY IS Clojure DOING THIS TO US???!!!
;;
;; And how are we to get anything done if we can't change things???
;;
;; HOW???



;; First, picture what a pure function does:
;;
;; [ Input Data ]
;;       |
;;       v
;; [ Function ]  --->x NO side-effects allowed!
;;       |
;;       v
;; [ Output Data ]
;;
;; i.e. a pure function is a data -to-> data transformation.



;; Now, picture what _we_ want to do:
;;
;; [ Outside World ]
;;        |
;;        v
;; --------------------------------------------------
;; [ I/O Boundary ] ; Ingest the state of the World.
;; --------------------------------------------------
;;        |
;;        v
;; [ Input Data ]  ; Values that we can't ever mutate.
;;        |
;;        v
;; [ Our Program ] ; A chain / pipeline of pure functions?
;;        |
;;        v
;; [ Output Data ] ; Values that we can't ever mutate.
;;        |
;;        v
;; --------------------------------------------------
;; [ I/O Boundary ] ; Update the state of the World.
;; --------------------------------------------------
;;        |
;;        v
;; [ Outside World ] ; New and Improved!
;;
;;
;; i.e., barring I/O Boundaries, where we read/write state,
;; could our whole program be a pure data-to-> data transformation?


;; Let's do this...


;; Outside World
;;
;; - A bunch of planetary sensors have written some JSON-formatted data
;;   into some files on disk.
;;
;; - Our Planetary Data Scientists have told us the format for each
;;   JSON file.
;;
;; - They want us to help them consolidate that data into a single file.
;;
;; - Maybe they will run batch jobs later? Who knows...
;;
;; - For now, we only need to prove to them that we can read their data,
;;   and write to the file they want.


;; They told us sensors would dump files here...
;;
(def sensor-data-dir
  "resources/sensor_data/")


;; They gave us:
;; - The file names for each sensor, and
;; - Told us the schema for the JSON in each file
;;
;; We decided to:
;; - Make a look-up table of files (as a hash-map), and
;; - Document the schema as in-line comments, for this dirty prototype.
;;
(def sensor-data-files
  {;; {"Planet Name":{"radius":<num>}, ...}
   :planets "planet_detector.json"

   ;; {"Planet Name":<num-moons>, ...}
   :moons "moon_detector.json"

   ;; {"Planet Name":<atmospheric property hash-map>, ...}
   :atmosphere "atmospheric_detector.json"})


;; They told us:
;; - The file name to put the fully denormalized planetary data.
;;
(def consolidated-data-file
  "consolidated_data.json")
;;
;; And, they said the JSON inside should follow this schema:
;;
;; [{"pname":"Planet Name",
;;   "moons":<num>,
;;   "atmosphere":<atmosphere data hash-map>},
;;
;;  {"pname" ...},
;;
;;  {"pname" ...}]


;; ---------------------- Input Boundary begins ------------------------

(defn ingest-json-file
  [dir-path file-name]
  (let [file-path (str dir-path file-name)]
    (with-open [reader (clojure.java.io/reader file-path)]
      (clojure.data.json/read reader))))


(defn gather-all-sensor-data!
  "Use our global collection of sensor keys to look up all the
  sensor data files, and ingest them at one go. Return a hash-map
  containing each sensor's data mapped to the corresponding sensor key."
  [data-dir sensor-files-map]
  (let [ingest-sensor-data
        (fn [out-map [sensor-key sensor-file]]
          (assoc out-map
                 sensor-key (ingest-json-file data-dir
                                              sensor-file)))]
    (reduce ingest-sensor-data
            {} ; out-map starts empty
            sensor-files-map)))

(gather-all-sensor-data! sensor-data-dir
                         sensor-data-files) ; try it

;; --------------------- Purely Functional Program begins --------------


(defn sensor-data->planet
  "Given a sensor key, a planetary hash-map, and a tuple having
  sensor data for a planet, inject the sensor data under the
  planet name found in the planetary hash-map."
  [sensor-key planets [pname sensor-pdata]]
  (assoc-in planets            ; given these planets as a hash-map
            [pname sensor-key] ; follow this nested path
            sensor-pdata))     ; and assoc this value under that path
;; Note:
;; Just like `assoc` (into map) is the partner of `get` (from map),
;; `assoc-in` (nested map) is the partner of `get-in` (from nested map).


(defn moons->planets
  "Inject moons sensor data, into planets sensor data."
  [moons planets]
  (reduce (partial sensor-data->planet :moons)
          planets
          moons))


(defn atmosphere->planets
  "Inject atmosphere sensor data, into planets sensor data."
  [atmosphere planets]
  (reduce (partial sensor-data->planet :atmosphere)
          planets
          atmosphere))


(defn denormalise-planetary-data
  "Given a hash-map of planetary data (keyed by planet names),
  return just the planetary data, with the planet's names added in.

  Also ensure all keys are keywordized, for convenient look-ups."
  [planets]
  (map (fn [[pname pdata]]
         (let [keywordized-pdata (clojure.walk/keywordize-keys
                                  pdata)]
           (assoc keywordized-pdata
                  :name pname)))
       planets))


(defn denormalized-planetary-data
  "Given all sensor data, produce a collection of denormalized
  planetary data."
  [{:keys [planets atmosphere moons]
    :as all-sensor-data}]
  ((comp denormalise-planetary-data
         (partial atmosphere->planets atmosphere)
         (partial moons->planets moons))
   planets))


;; ---------------------- Output Boundary begins -----------------------


;; First try this, to check packaged data...
(denormalized-planetary-data
 (gather-all-sensor-data! sensor-data-dir
                          sensor-data-files))

;; Does the result look familiar?


(defn write-out-json-file
  [dir-path file-name data]
  (let [file-path (str dir-path file-name)]
    (with-open [writer (clojure.java.io/writer file-path)]
      (clojure.data.json/write data writer))))


(defn ingest-export-sensor-data!
  [data-dir source-data-files dest-data-file]
  (write-out-json-file
   data-dir
   dest-data-file
   (denormalized-planetary-data
    (gather-all-sensor-data! data-dir source-data-files))))


;; Try it!
(ingest-export-sensor-data!
 sensor-data-dir
 sensor-data-files
 consolidated-data-file)

;; --------------------- Output Boundary ends ------------------------

;; Done!

;; Or are we???

;; Because now...

;; The Planetary Data Scientists reveal their nefarious intentions:

(clojure-by-example.ex04-control-flow/colonize-habitable-planets!
 (denormalized-planetary-data
  (gather-all-sensor-data! sensor-data-dir
                           sensor-data-files)))
;; Note:
;; - For this to work, colonize-habitable-planets must be complete
;; - And you should have first evaluated it, to make it unable
;; - Which means, you have to first correctly solve ex04



;; RECAP:
;; - If we carefully keep side-effecting functions at our program
;;   "boundary", we can design purely functional "core" logic.
;;   This makes it much easier to reason about most of our program,
;;   and to test it.
;;
;; - TBD...
