- [Introduction](#introduction)
  - [Intended usage](#intended-usage)
  - [Contributions](#contributions)
  - [Workshop Goals](#workshop-goals)
  - [Workshop Anti-Goals](#workshop-anti-goals)
- [Suggested learning mindset](#suggested-learning-mindset)
- [Setup Instructions](#setup-instructions)
- [Course Design Philosophy](#course-design-philosophy)
- [Credits](#credits)
- [Copyright and License](#copyright-and-license)

# Introduction

This workshop aims to get your brain and fingers accustomed to just enough of
the [Clojure](https://clojure.org) programming language to start doing useful things with it.

In other words, "What could one do with just a _little_ bit of Clojure?".

## What is Clojure?

Clojure is an interactive functional programming language that can run on many platforms
like the [JVM](https://clojure.org/about/jvm_hosted), [.NET CLR](https://clojure.org/about/clojureclr), [Javascript](https://clojurescript.org/) (browsers, nodeJS, React Native), as [native binaries](https://github.com/BrunoBonacci/graalvm-clojure) via Graalvm, and even as [shell scripts](https://babashka.org/)!

It is [used by software teams worldwide](https://clojure.org/community/success_stories#) to deliver
high-value software systems at giant companies like Apple, Walmart, to "decacorns"
like GoJek, Nubank, to a wide array of startups, and one-person businesses like Partsbox.com.

Its interactivity and dynamism foster a sense of playfulness that attracts all manner
of [creative makers](http://radar.oreilly.com/2015/05/creative-computing-with-clojure.html)---hobbyist as well as serious artists and musicians.

A small but vibrant [global community](https://clojure.org/community/user_groups) is [busy building amazing things](https://github.com/trending/clojure?since=monthly) with the language.

## Intended usage

- Support a 1-day guided workshop for programmers new to Clojure (not absolute programming beginners).
- Also function as at-home learning material for said programmers.
- The `master` branch is heavily commented, for at-home use.
- A `solutions` branch will be available, as a companion to `master`.
  But don't peek at it in advance!
- You may see a `workshop-code` branch. Ignore it. It is meant only for
  workshop use, and is subject to deletion/re-creation.
- Incidentally, if you landed here while searching for Hirokuni Kim's
  "[Clojure By Example](https://kimh.github.io/clojure-by-example/)", well, follow the link!

## Contributions

- If you find bugs or errors, please send a PR (but please
  don't change the course structure or pedagogy).

## Workshop Goals

- Acquire a "feel" of Clojure, for further self-study/exploration.
- Learn how Clojurists usually think with Clojure to solve problems.
- See how it's not so hard to do surprisingly powerful things with a
  mere handful of "primitive" functions, data structures, and ideas.
- Get you started with a good development setup and workflow that will
  serve you well if (when) you continue to program with Clojure, as a
  hobby, or at work!

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

Just do the following one by one, and you should be fine.

## Java

You need Java installed.

- Run `java -version` in your terminal.
- If Java is not installed, please [download and install Java from here](https://adoptopenjdk.net/).
- Any version should do, but prefer Java 8 or higher. We have not tested
  this project with Java 7.
- Once you are done, `java -version` should show you a Java version.

## VSCode + Calva

We support VSCode + Calva IDE in the classroom for this workshop. We suggest you use this setup, unless of course, you have already configured your favourite editor for Clojure development. We've listed alternate starter kits below (IntelliJ, Vim, Emacs, Atom), _but_ please avoid [bikeshedding](http://catb.org/jargon/html/B/bikeshedding.html) editors. Just complete the workshop first!

- Download and Install [VSCode](https://code.visualstudio.com/).
- Open VSCode and complete the initialization process.
- Open the "Extensions" Tab and search for "Calva", Install the "Calva:
  Clojure & ClojureScript Interactive Programming" extension.
- Alternatively you can visit the [Calva page](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) to install it.

Once installed:

- Clone the repository on your machine.
- In VSCode Use File > Open Folder... and open the cloned folder.
- Notice that Calva activates.
- Open the [Command Palette](https://code.visualstudio.com/docs/getstarted/userinterface#_command-palette) in VSCode using `⇧⌘P` on Mac or `Ctrl+Shift+P` on other systems.
- Type "Calva: Start Project REPL" and choose "Calva: Start a Project REPL and Connect (aka Jack-In)" from the list.
  - Select `deps.edn` when prompted for Project type. We are using [tools.deps](https://clojure.org/guides/deps_and_cli) for managing the project. You don't need to worry about it's details for this workshop.
  - VSCode will create a new pane called 'output.calva-repl' and you will see `clj꞉user꞉>` prompt in that screen.
- You have a working REPL now!
- Keep the [Paredit guide](https://calva.io/paredit/) handy, editing code will require some understanding of paredit.

## Your favourite editor:

You may find instructions for your favourite editor at one of these pages. But there are only so many choices. Ultimately, you must pick your poison and run with it:

- ["Clojure Tools" at clojure.org](https://clojure.org/community/tools)
- ["Essentials" at clojure-doc.org](http://clojure-doc.org/articles/content.html#essentials)
- [Christopher Bui says...](https://web.archive.org/web/20181223213500/https://cb.codes/what-editor-ide-to-use-for-clojure/)

# Course Design Philosophy

Just some peoples' opinion. You need not be slave to it ;-)

Almost anyone can hope to do more with more. Up to a point, that is.

Far too often, we end up doing _less_ with more; bogged down by the
complexity and blinding glitter of too much choice, and overabundance.

Figuring out how to do more with less feeds our curiosity, and it often
satisfies and empowers us deeply.

So, may you stay small and achieve important things.

Live long, and prosper.
\\\\//\_

# Credits

- [clj-pune](https://github.com/clj-pune) people, especially [kapilreddy](https://github.com/kapilreddy), and [jaju](https://github.com/jaju) for critique while making ["pratham"](https://github.com/clj-pune/pratham), the precursor to this project.
- [adityaathalye](https://github.com/adityaathalye), [jysandy](https://github.com/jysandy), and [kapilreddy](https://github.com/kapilreddy) for course design, code reviews, critique, commits, and being the core teaching staff at the first edition of this workshop at IN/Clojure 2018.
- All the workshop participants, and the many Clojurists who generously donated their time to make it successful.
- [inclojure-org](https://github.com/inclojure-org) for being the umbrella under which this work happened.

## Copyright and License

Copyright © 2017-2024 [IN/Clojure](http://inclojure.org/).

Distributed under the [MIT license](https://github.com/inclojure-org/clojure-by-example/blob/master/LICENSE).
