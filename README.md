Site
====

This repository contains the code for our site, https://kotlindiscord.com (which
has not yet launched).

Here's a list of the main technologies we're using:

* [Fomantic UI](https://fomantic-ui.com/) (Community fork of 
  [Semantic UI](https://semantic-ui.com/))
* [Kotlin](https://kotlinlang.org/) (of course)
* [KTor](https://ktor.io/)
* [Pebble Templates](https://pebbletemplates.io/)
* [PostgreSQL](https://www.postgresql.org/) (via 
  [Exposed](https://github.com/JetBrains/Exposed) and 
  [Harmonica](https://github.com/KenjiOhtsuka/harmonica))

Setting Up Your Environment
===========================

In order to work on this project, we require that you have the following things 
installed:

* [OpenJDK](https://adoptopenjdk.net/) 11 or later
* [NodeJS](https://nodejs.org/en/) 12 or later with NPM

We also recommend the use of [IntelliJ IDEA](https://www.jetbrains.com/idea/).
The instructions below will assume that you're using IDEA, so remember to adjust
if you're using a different editor.

Installing Dependencies
-----------------------

In order to make use of Fomantic UI (The CSS framework we've used here), you'll need
to install it. Simply run `npm i` after cloning the repository, and Fomantic UI will
be installed to `src/semantic` and compiled into `build/semantic`.

If you clear your `build` directory (or modified something in `src/semantic`), you can 
rebuild Fomantic by opening a terminal in `src/semantic` and running `npx grunt build`.

We are currently not customising Fomantic UI, so we added the `src/semantic` directory
to the `.gitignore`. This may change in the future.

Setting Up IDEA
---------------

* Install the [Pebble plugin](https://plugins.jetbrains.com/plugin/9407-pebble).
  You'll need this to work with the Pebble templates we use in this project.
* Open the project in IDEA and allow it to import the `build.gradle`.
* Open the `Settings` window from the `File` menu, expand `Languages & Frameworks`
  in the sidebar and navigate to `Template Data Languages`.
* Click the dropdown next to `Project Language`, and select `Pebble`.
* Click the `+` on the right-hand side of the window and select 
  `src/main/resources/templates`.
* Select the `Language` dropdown for the entry you just created, and change it to 
  `HTML`.

Running the Project
-------------------

To run the project, simply use the `run` Gradle task. You'll need to set the following environment
variables, or the application will fail to start - feel free to leave their values empty if you're
not working with them, though.

* `DISCORD_CLIENT_ID` / `DISCORD_CLIENT_SECRET`: These are used for Discord OAuth logins and
  correspond with the relevant settings in the Discord developer area.
