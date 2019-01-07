(ns clojure-by-example.utils.ex07-boldly-go)


;; EX07: Lesson Goals
;;
;; - Quickly see how to start and architect your own project.
;;   - namespaces, dependencies, project structuring etc...
;;
;; - To be able to run your project as a binary (jar) like this:
;;
;; java -jar ./path/to/the_library_jarfile.jar "arg1"  "arg2" "arg3"



;; Your First Clojure Library
;; - Let's turn code from ex06 into a library for planetary exploration.
;; - Follow the procedure below, step by step.
;;
;;
;; * Use leiningen to create a new project skeleton.
;;
;;   - Open a terminal session and execute:
;;
;;     lein new planet_coloniser
;;
;;
;; * Open the directory in your IDE and observe its structure.
;;
;;
;; * Create a `utils` directory in which to put I/O utility functions.
;;   (Putting utility functions in `utils` is only a convention,
;;    not a rule.)
;;
;;   - Terminal:
;;     mkdir src/planet_coloniser/utils
;;
;;   - Or, in your IDE:
;;     - right-click on src/planet_coloniser, and create new folder
;;
;;
;; * Inside this new `utils` directory, create two new files:
;;   - `ingest.clj` and
;;   - `export.clj`
;;
;;   - Again, you can right-click on your `utils` dir in your IDE,
;;     and create New File from the pop-up menu.
;;
;;
;; * Update `ingest.clj` with ingest utility functions.
;;
;;   - Open the file and add this "ns declaration" at the top:
;;
;;   (ns planet-coloniser.utils.ingest)
;;
;;   - Observe the dir name is planet_coloniser, but the ns
;;     declaration has planet-coloniser. This is the convention:
;;     - hyphens separate words in ns names
;;     - dots separate directories and files in ns names
;;     - underscores from dir or file names become hyphens in ns names
;;     - and ns names are all lower case
;;
;;   - Copy-paste the "input" function definitions from ex06,
;;     below the ns declaration.
;;
;;
;; * Update `ingest.clj`, and project, for external dependency
;;
;;   - `ingest` uses `clojure.data.json`, which is an external
;;     dependency that we have to specify explicitly.
;;
;;   - Inside `project.clj`, update :dependencies value to look like:
;;
;;    :dependencies [[org.clojure/clojure "1.10.0"    ; latest as of 01 Jan 2019
;;                   [org.clojure/data.json "0.2.6"]] ; add for `ingest`
;;
;;   - Inside `injest.clj`, update the ns declaration to look like:
;;
;;     (ns planet-coloniser.utils.ingest
;;       (:require [clojure.data.json]))
;;
;;
;; * Update `export.clj`. Open the file and:
;;
;;   - Add the appropriate "ns declaration" at the top
;;
;;   - Copy-paste only the `write-out-json-file` function here.
;;
;;   - Later, we will port code from the other function to another file.
;;
;;   - Also ensure, the ns form looks like this:
;;
;;     (ns planet-coloniser.utils.export
;;       (:require [clojure.data.json])) ; we use this in export too
;;
;;
;; * Create `sensor_processor.clj`, for our core "pure" logic:
;;
;;   - Create it under `src/planet_coloniser/`
;;
;;   - Add the appropriate ns declaration
;;
;;   - Copy-paste all the pure functions in there
;;
;;   - Update the ns form to also require clojure.walk
;;
;;
;; * Update `src/planet_coloniser/core.clj`
;;   - This will contain the entry point for the outside world,
;;     into our planet processor.
;;
;;   - Delete the dummy function from the namespace
;;
;;   - Update the `ns` declaration to look like this:
;;
;; (ns planet-coloniser.core
;;   (:gen-class) ; add this, and the :require expression below:
;;   (:require [planet-coloniser.sensor-processor :as sensproc]
;;             [planet-coloniser.utils.ingest :as ingest]
;;             [planet-coloniser.utils.export :as export]))
;;
;;  - Copy `ingest-export-sensor-data!` from ex06
;;
;;  - Rename it to `-main`.
;;
;;  - Now, make the body of `-main` look like the function below:
;;    - notice prefixes to functions, like export/, ingest/, sensproc/
;;    - Why do we do this?
;;
;; (defn -main
;;   [data-dir source-data-files dest-data-file]
;;   (let [source-data-files (ingest/ingest-json-file! data-dir
;;                                                     source-data-files)
;;         export-as-json (partial export/write-out-json-file!
;;                                 data-dir
;;                                 dest-data-file)]
;;     (export-as-json
;;      (sensproc/denormalise-planetary-data
;;       (ingest/gather-all-sensor-data! data-dir
;;                                       source-data-files)))))
;;
;;
;; * Let's bring in sensor data, for convenience:
;;   - Copy over `sensor_data` directory from clojure-by-example.
;;     - From:
;;       /Path/To/clojure-by-example/resources/sensor_data/
;;     - To:
;;       /Path/To/planet_coloniser/resources/sensor_data/
;;
;;
;; * In the new `sensor_data` directory, create a new JSON file:
;;
;;   - call it `sensor_data_files.json`
;;
;;   - add the following JSON to it:
;;
;;     {"planets":"planet_detector.json",
;;     "moons":"moon_detector.json",
;;     "atmosphere":"atmospheric_detector.json"}
;;
;;
;; * Let's make the project runnable:
;;
;;   - Update `project.clj` to specify entry point.
;;
;;   - After :dependencies, add the location of the main function:
;;
;;   :main planet-coloniser.core
;;
;;
;; * Check if the project has indeed become runnable:
;;
;;   - In the terminal, cd to the root directory of your
;;    `planet_coloniser` project.
;;
;;   - Use leiningen to run the library:
;;
;;     lein run "resources/sensor_data/" "sensor_data_files.json" "lein_out.json"
;;
;;   - Did that work?
;;     - Check resources/lein_out.json
;;
;;
;; * Let's bump our project version to `0.1.0` :-D
;;   - In project.clj, delete `-SNAPSHOT` from the project version
;;     (see it at the top of the project definition)
;;
;;
;; * Finally, let's prep the project for building a java executable.
;;   - Update `project.clj` for Ahead-Of-Time compilation,
;;     required in order to produce a standalone Java executable jar.
;;
;;   - Below :main, add this:
;;
;;     :aot :all
;;
;;   - Use leiningen to Build the jar:
;;     - In your terminal, cd into the root of your project,
;;       and execute:
;;
;;       lein uberjar
;;
;;  - Observe the build output in your terminal.
;;
;;  - Do you see two jar files created under `planet_coloniser/target`?
;;
;;
;; * Now run the "standalone" jar as:
;;
;;    java -jar target/planet_coloniser-0.1.0-standalone.jar "resources/sensor_data/" "sensor_data_files.json" "jar_out.json"
;;
;;   - Did that work?
;;     - Check the output file
;;
;;
;; * If you've reached this far...
;;   - Congratulations, you just built a brand new Clojure project
;;     from scratch!
;;
;;


;; HOMEWORK:
;;
;; * Why did we have to do all this?
;;   Ben Vandgrift has a nice explanation at:
;;   "Clojure: 'Hello World' from the Command Line"
;;   http://ben.vandgrift.com/2013/03/13/clojure-hello-world.html
