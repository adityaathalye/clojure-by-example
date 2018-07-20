# Clojure By Example

What could one do with just a _little_ bit of Clojure?

## Intended usage
  - Support a 1-day guided workshop for programmers new to Clojure (not absolute programming beginners).
  - Also function as at-home learning material for said programmers.
  - The `master` branch is heavily commented, for at-home use
  - A `solutions` branch will be available, as a companion to `master`.
    But don't peek at it in advance!
  - Ignore the `workshop-code` branch. It is only for workshop use,
    and subject to deletion/re-creation.

## Contributions
  - If you find bugs or errors, please send a PR (but please
    don't change the course structure or pedagogy).

## Workshop Goals
  - Acquire a "feel" of Clojure, for further self-study/exploration.
  - Learn how Clojurists usually think with Clojure to solve problems.
  - See how it's not so hard to do surprisingly powerful things with a
    mere handful of "primitive" functions, data structures, and ideas.

## Workshop Anti-Goals
  - Try to explain Functional Programming theory or Clojure's innards.
    (Many free and paid tutorials and books do so very well.)
  - Try to fully cover Clojure primitives/features. (That's homework!)
  - Devolve into language wars, editor wars, syntax wars, type wars...
    (Life's too short, people.)
  - Focus too much on tooling or operational things. (At least not
    while there's fun to be had!)


# Suggested learning mindset
  - Think of this as an exercise in "constrained creativity".
  - Ignore details, achieve much with as little know-how as possible.
  - Focus on what things do; not what they are, or why they are.
  - Inform your _intuition for doing things_, and then use that to
    dive deeper into all the juicy details at your own pace, later.

Take what is useful, discard the rest.


# Setup Instructions

It's a liiitle bit of work. But not too bad.

Just do the following one by one, and you should be fine.

## Java 8

You will need Java to work with this Clojure workshop content.

First, make sure you have Java 8.

  - Run `java -version` in your terminal.
  - If Java is not installed, please [download and install Java 8 from here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
  - Once you are done, `java -version` should show you a Java 1.8.x version.

Notes:

  - If you have Java 9, that should be OK too.
  - The LightTable editor is known to break with Java 9. Use Java 8 instead.
  - We have not tested this project with Java 7.


## Leiningen

Follow [Leiningen setup instructions here](https://leiningen.org/).

### Fire up a REPL

  - Clone this project
  - Open your terminal, and do the following.
  - `cd` into this project's root directory
  - Use `lein repl` command to start a REPL with Leiningen.
  - Wait for it... the REPL will start and print out a message with some
    useful information
  - Locate the `port` and `host` information in the message. We will need this information soon.

Note:

  - [Boot](http://boot-clj.com/) should be fine too, but we have not tested this project with it.


## Code Editor and Tooling

Set up an editor and figure out how to evaluate Clojure code with it.

### LightTable

We used LightTable for our workshop. We suggest you do so too, unless of course, you have already set up your favourite editor for Clojure development. Avoid [bikeshedding](http://catb.org/jargon/html/B/bikeshedding.html) editors. Just complete the workshop first!

  - You may install LightTable from the [official website](http://lighttable.com/).
  - But you must have Java 8. LightTable breaks with Java 9.
  - On Mac OS, you may have to allow running the app in your security preferences to be able to open it.

Once installed:

  - Use LightTable's file menu to open this project.
  - In the left pane, navigate down to the first file `ex00...`, under the `src` folder.
  - Under `View` menu, click `Connections`. A right pane should open.
  - Under `Add Connection`, click `Clojure (remote REPL)` and complete the port number. Recall host:port information was printed to the terminal when you fired up a REPL in the previous section.
  - In the `ex00..` file, scroll down a little, till you see `(+ 1 2)`.
  - Place your cursor after the closing parenthesis `)` and hit Ctrl+Enter (Win/Linux), or Cmd+Enter (Mac).
  - You should see `3` appear in-line. This means you successfully connected and evaluated an expression.
  - Now you may start from the top of ex00 and work through the material.

Also keep [LightTable's documentation](http://docs.lighttable.com/tutorials/full/) handy in case you need editor help, as you solve the workshop material.


Optionally, add Parinfer for easier editing:

  - In LightTable, go to View -> Plugin Manager and search for "parinfer".
  - Install the Parinfer plugin by Maurício Szabo.
  - Parinfer is an editing system for Clojure that makes it easy for you to move Clojure code around without unbalancing parentheses.
  - We recommend going through the [Parinfer documentation here](https://shaunlebron.github.io/parinfer/). But don't get stuck there, just keep it handy.


### Alternative Starter Kits:

If you can't use LightTable for some reason (like can't downgrade to Java 8 from Java 9). You may try one of these. Although we haven't tested with these setups, the workshop material should work fine.

  - A [snazzy setup with Atom](https://medium.com/@jacekschae/slick-clojure-editor-setup-with-atom-a3c1b528b722).
  - Brave Clojure walks you through [a basic Emacs setup for learning Clojure](https://www.braveclojure.com/basic-emacs/).


### Your favourite editor:

You may find instructions for your favourite editor at one of these pages. But there are only so many choices. Ultimately, you must pick your poison and run with it:

  - ["IDEs and Editors" at dev.clojure.org](https://dev.clojure.org/display/doc/IDEs+and+Editors)
  - ["Essentials" at clojure-doc.org](http://clojure-doc.org/articles/content.html#essentials)
  - [Christopher Bui says...](https://cb.codes/what-editor-ide-to-use-for-clojure/)


# Course Design Philosophy

Just some peoples' opinion. You need not be slave to it ;-)

Almost anyone can hope to do more with more. Up to a point, that is.

Far too often, we end up doing _less_ with more; bogged down by the
complexity and blinding glitter of too much choice, and overabundance.

Figuring out how to do more with less feeds our curiosity, and it often
satisfies and empowers us deeply.

So, may you stay small and achieve important things.

Live long, and prosper.
\\\\//_


# Credits
  - [clj-pune](https://github.com/clj-pune) people, especially [kapilreddy](https://github.com/kapilreddy), and [jaju](https://github.com/jaju) for critique while making ["pratham"](https://github.com/clj-pune/pratham), the precursor to this project.
  - [adityaathalye](https://github.com/adityaathalye), [jysandy](https://github.com/jysandy), and [kapilreddy](https://github.com/kapilreddy) for course design, code reviews, critique, commits, and being the core teaching staff at the first edition of this workshop at IN/Clojure 2018.
  - All the workshop participants, and the many Clojurists who generously donated their time to make it successful.
  - [inclojure-org](https://github.com/inclojure-org) for being the umbrella under which this work happened.

## Copyright and License

Copyright © 2017-2018 [IN/Clojure](http://inclojure.org/).

Distributed under the [MIT license](https://github.com/inclojure-org/clojure-by-example/blob/master/LICENSE).
