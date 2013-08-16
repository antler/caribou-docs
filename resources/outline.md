# What is Caribou?

Caribou is a dynamic web application ecosystem for crafting production ready web
inhabitants with minimal effort.

Forged in the fire of daily requirements, Caribou is a collection of solutions
to problems web developers face every day.

The philosophy behind its development is to solve every problem we come across
once, in a general way that can be reused over and over.

Anything unnecessary or obstructive has been burned away from the wear of
constant use.

The result is Caribou, the kernel of usefulness that has emerged from years of
this basic practice.

# Getting Started

## Installing Caribou

Caribou depends on the java runtime, so the zeroth step would be to install a
JVM if you don't have one already: >
http://www.java.com/en/download/help/index_installing.xml

Next, install Leiningen (which provides the `lein` command) if it is not already
installed: > http://leiningen.org/

Once you have the `lein` command, create a profile that includes `lein-caribou`
(the Leiningen Caribou plugin).

* Create a file called `~/.lein/profiles.clj` with the following contents:

```clj 
{:user 
  {:plugins [[lein-ring "0.8.2"] 
             [lein-caribou "2.2.8"]]}}
```

* (note these versions may have increased.  Check http://clojars.org for latest
  version information)

* Run `lein help` to test out your setup.  If you see some helpful output you
  are ready to go!

## Creating a New Site

To create a new Caribou project, type this at the command line:

``` 
% lein caribou create taiga 
```

This will create a new directory structure under the name `taiga` and prime a
new H2 database for use with Caribou.

If you don't want to use H2 you can configure Caribou to use other database
backends.

## Running the Site

To run the site as it exists, simply:

```
% cd taiga
% lein ring server
```

A new window will appear in your browser under http://localhost:33333 .  

Congratulations!  You are now running Caribou.

# Components of a Caribou Project

Caribou is not a single library, but an ecosystem of interacting components,
each of which has the ability to stand on its own.  This idea lends a quality of
composability to the Caribou world.  If some capability does not
exist, it can be created on its own and then easily linked into a working
Caribou instance.

That said, there are some core components that lay the foundation for everything
that follows.

## Base Libraries

### Core

[Caribou Core](http://github.com/antler/caribou-core) lays the foundation for
all of the other libraries by capturing the data model of a site as data!  A
data model is traditionally only an implicitly defined being, existing as a
conglomeration of migrations and tables whose relationships are only formed
through happy accidents within application code. Caribou Core structures the
data model in a way that allows it to serve a variety of other purposes,
including the construction of queries that filter and order based on the
relationships between models.

[Caribou Core](https://github.com/antler/caribou-core) can be used on its own if
all you need is the dynamic models as an interface to a database backend.  In
practice it is usually supporting a site running the Caribou Frontend, Admin and
API, but nothing is stopping you from using it independently of an HTTP Ring
server.

The heart of Core is the Model system, which abstracts over a database schema
and provides methods for making schema transformations through transformations
on pure clojure data.  In every way, Model (which represents a database table)
itself is a Model, with an association to Fields (which represent the columns of
a database table) which is also a Model.  This is a radical choice, and lays the
foundation for the rest of Caribou's power.  The ability to treat Models and
Fields themselves as data enables Caribou to generate an Admin and API for you
automatically, and countless other benefits that you will discover as you go
deeper into the Caribou ecosystem.

### Frontend

[Caribou Frontend](https://github.com/antler/caribou-frontend) uses Core as the
data layer foundation and is built on the [Clojure Ring
protocol](https://github.com/ring-clojure/ring).  Ring is a flexible HTTP
protocol for Clojure that abstracts over the HTTP request and response
lifecycle, turning them into plain Clojure maps.  In practice this is an
extremely powerful way to compose handlers and functionality into a robust web
server.

Frontend adds onto the Core and Ring base a fully-functional routing, controller
and rendering system.  If Core is the M, then Frontend is the VC.  At the heart
of this system is the Page, which associates routes to the controllers that run
when they are matched and the templates that are ultimately rendered with data
retrieved and defined in the controllers.

### Admin

[Caribou Admin](https://github.com/antler/caribou-admin) provides a
browser-based interface to all of the Caribou functionality.  Things you would
previously need someone to code for you can be done with the click of a button.
Adding new Models, adding new Fields to those Models, creating content based on
those Models, adding Pages for routing and rendering, localizing content for
many languages and locales, adding Accounts and managing Permissions, all of
this is accessible through the Admin interface.  No need to build a custom admin
for every project!  This alone cuts down on the development time of a project by
a large degree, and is one of the huge advantages of using Caribou to build your
site.

### API

[Caribou API](http://github.com/antler/caribou-api) provides a RESTful API in a
variety of formats (json, xml or csv) which tap into any content you create in
Caribou.  Create a new Model and instantly an endpoint representing that Model
is available.  Add some content for that Model, the content magically appears in
the API results.  Use any of the options for filtering and selecting content as
URL parameters that would previously only be available programmatically.  The
API again is a tangible upshot of Caribou's Model as Data approach.

## Peripheral Libraries

Besides the base libraries, there is a whole tundra of associated libraries that
exist on their own, but also contribute to the Caribou ecosystem.

### Lichen

[Lichen](http://github.com/antler/lichen) is a standalone image resizing library
that enables Caribou to define image sizes during template creation.  Lichen
creates the newly resized version of the image the first time it is requested,
then reuses the cached version on each subsequent request transparently, so the
developer never needs to worry about it.  Declare what you want, Lichen worries
about how to most efficiently perform the task.

### Antlers

[Antlers](http://github.com/antler/antlers) is a templating library that grew
out of the raw Mustache spec, but adapted to ever expanding demands from day to
day use.  Today it is a fully functional templating system with blocks, helper
functions, loop variables and a host of other practical features that makes it
the cornerstone of rendering in Caribou.  Of course, nothing is stopping you
from using whatever template system you want, but if you need it, it's there.

# Basic Concepts

## Models and Fields

Models in Caribou are a representation of the data of your application.  Models
can be created like any content, but models are special in that creating a model
enables a new type of content to be created.  Conceptually there should be a
model for every variety of real world data that the application is capturing.
Each model has a set of Fields which represent the different types of data that
the model keeps track of.  These fields can be of a variety of types, things
like text, images, numbers, dates, and even associations to other models.

As an example, to create an application that lets you create presentations, you
could have a Presentation model that would have a "title" text field and maybe a
"preview" image field, and a Slide model with a "caption" text field and an
"image" field with an image containing the slide content.  Then, you could
create an association between Presentation and Slide so that Presentation has a
collection of Slide objects.  Once these models are created, you could start
creating Presentations and populate them with Slides.

This flexibility in defining what kind of data your application will contain
enables an endless variety of possible applications to be built.  Once a model
exists in the system, you can read data from the API or create new content in
the Admin.

Model itself is a model, with a collection of Fields (which is also a model!)
To read more, check out the [Introduction to Data
Modeling](#introduction-to-data-modeling).

## Pages

Pages are the way to define how urls are matched in your application.  Each page
represents a route that can be matched when a user navigates to a particular
url.  Once a route is matched, the controller associated to that page is
triggered with the parameters defined by that route.  Pages form a nested
structure, so if a page is a child of another page, it inherits its parent's
route and adds its own unique path onto it.  In this way the routing structure
of an application can be organized hierarchically, simplifying what could
otherwise be a complicated tangle of routes.

Read more at [Defining Pages and Routes](#defining-pages-and-routes).

## Controllers and Templates

Once a route has been matched, the corresponding controller is triggered.  A
controller in Caribou is just a Clojure function that takes a single argument,
`request`, and renders to the browser whatever that function returns.

In practice, a controller can use the built in template system called
[Antlers](http://github.com/antler/antlers) to render html or json (or any other
format for that matter).  Any parameters passed into the built in `render` call
will be available in the template.

To see more on how this is done, check out the section on
[Rendering Templates](#rendering-templates)

# Configuring Caribou

## Default Directory Structure

The Caribou directory structure is designed to be simple and flexible.  
Running `tree` in the root illuminates the structure:

```
├── app
├── project.clj
├── resources
│   ├── config
│   │   ├── development.clj
│   │   ├── production.clj
│   │   ├── staging.clj
│   │   └── test.clj
│   ├── public
│   │   ├── css
│   │   │   ├── fonts
│   │   │   │   ├── caribou.eot
│   │   │   │   ├── caribou.svg
│   │   │   │   ├── caribou.ttf
│   │   │   │   └── caribou.woff
│   │   │   └── taiga.css
│   │   ├── favicon.ico
│   │   ├── ico
│   │   │   └── favicon.ico
│   │   └── js
│   │       └── taiga.js
│   └── templates
│       ├── errors
│       │   ├── 404.html
│       │   └── 500.html
│       └── home.html
├── src
│   ├── immutant
│   │   └── init.clj
│   └── taiga
│       ├── boot.clj
│       ├── controllers
│       │   └── home.clj
│       ├── core.clj
│       ├── hooks
│       │   └── model.clj
│       └── migrations
│           ├── admin.clj
│           ├── default.clj
│           └── order.clj
├── taiga_development.h2.db
├── taiga_development.trace.db
```

There are some main features to take note of for now.

### project.clj

First is the `project.clj`, which configures `lein` and holds information about
dependencies and plugins.  You will be editing this when you want to add a new
Clojure library to your project, for instance.  Also, this is where you define
various options about how the site runs, including the port, the handler and an
init function that is run on boot.  Full details can be found in the
configuration section on project.clj.

### resources

The `resources` directory has three branches: `config`, `public`, and
`templates`.

* **config**

The first, `config`, holds all the configuration files for the various
environments that your Caribou app will eventually run in.  The name of each
environment maps to a configuration file with the same name and suffixed by
`.clj`.  So in the "development" environment Caribou will use the
`development.clj` config file.  For now the app defaults to `development`, but
there are things you will want to shut down for production that are helpful in
development, like automatic code reloading.  For this Caribou provides a
`production.clj` with its own set of configuration options.

* **public**

Anything in `public` will be accessible as a static resource under the URL that
maps to this directory structure.  If all you have is static content, just throw
a bunch of files in here where you want them to be accessed and you are good to
go!  We have put some helpful files in here to get you started, (css and js) but
nothing is set in stone.  Have at!

* **templates**

Here is where all of the dynamic templates go.  In Caribou, you can create
content that can then be accessed from templates.  Caribou uses a template
engine called Antlers by default: https://github.com/antler/antlers .  The docs
for using antlers are all on that page.

### src

`src` holds all of the Clojure files that run your Caribou site.  There is an
`immutant` subdirectory for configuring Immutant (which is an optional app
container): http://immutant.org/ .  You can ignore this one for now.  Next to
that is a directory named after your project (here that is "taiga").  All of
your site specific code will go in here.

There are some notable entries in your project source folder:

* **core.clj**

This is the entry point into your Caribou project, and ultimately what gets
executed on boot.  You can change everything about how Caribou runs from inside
this file, from replacing pages and models to defining configuration to
executing handlers for every request.  For now, the structure is set up to run
Caribou how it was designed to be run, but never forget that you have ultimate
control of this.

* **boot.clj**

This file governs which configuration file gets loaded.  You can also change
configuration options inside this file that apply to all running environments,
if you wish.

* **migrations**

This directory contains data migrations that specify how your data evolves over
time.  You can add your own migrations in addition to the migrations necessary
to run your site for the first time.  Any migration files added here must be
included in `order.clj`.  This is necessary so that the migration system knows
what order to run the migrations in.  The database keeps track of which
migrations have been run, so no migration is ever run twice on one database.

* **hooks**

Hooks are defined per model.  There are a variety of points in the content
lifecycle where custom code can be run, if desired.  This is covered in the
configuration section on hooks.

* **controllers**

Controllers are called when an http request is matched by a page that references
that controller.  This is how routes are linked to actual Clojure functions that
eventually render a template or a return a valid response of some kind.

### The default H2 database

This will be named after your project with the suffix "_development.h2.db".  By
default Caribou uses H2 because it is an all java database which requires no
native dependencies.  You will probably want to swap this out with your own
database backend, but Caribou will work fine if all you ever want to use is H2.

## How Configuration Works in Caribou

Caribou avoids holding any global state and elects rather to store state
particular to the application in a configuration map that is owned by the user.
This has a number of advantages, the first being that no code is tied to a
particular configuration.  Configurations can be swapped in and out and Caribou
will pick up and run with that configuration as if it had been using it the
whole time.

That given, there are a fair number of options and state that Caribou keeps
track of and requires to run, so not just any map will work.  In the
`caribou.config` and `caribou.app.config` namespace there are a number of
functions which facilitate the construction, modification and reading of these
configuration maps.

Once you have a configuration map, you can call any Caribou methods inside of a
`caribou.core/with-caribou` block.

```clj 
(let [config (pull-config-map-from-somewhere)] 
  (caribou.core/with-caribou config 
    (... ))  ;; block of code that assumes a caribou config 
```

As we progress we will illuminate a number of Caribou calls that work in this
manner.

Also, in order to access a value that lives inside a Caribou configuration, use
`caribou.config/draw`:

```clj
(caribou.config/draw :models :model :id) ---> The id of the Model model.
```

## Initializing a Caribou Configuration

In general, we will refer to namespaces inside a Caribou project as
`{project}.foo`, since we don't know what you named your project.  So if you
named your project "taiga" and we are talking about the `{project}.core`
namespace, that means `taiga.core`.

Caribou configuration is done by passing in a configuration map to the
`caribou.core/init` call in the main `{project}.boot` namespace.  By convention,
this map is obtained as a result of calling the
`caribou.config/config-from-environment` method on a default configuration map
obtained from `caribou.app.config/default-config`.

```clj 
(let [default (caribou.app.config/default-config) 
      config (caribou.config/config-from-environment default)] 
  (caribou.core/init config))
```

`caribou.core/init` sets up all the state that Caribou needs to run and stores
it in the config object passed into it.  Once a config map has been through
`caribou.core/init` it is ready to be used for any Caribou related operation
that needs to be performed.

`caribou.config/config-from-environment` just reads the result of whatever file
in `resources/config/{environment}.clj` matches the current environment setting
and merges that map into the default map you provide.  By default the
environment is "development", but it can be set as a java option (which can be
done in a number of ways).  One of the easiest is to set it in your env like so:

``` 
% export _JAVA_OPTIONS=-Denvironment=production 
```

This is a standard method for setting JVM options from the command line.  (For
other methods check the java documentation).

Even though this is the default method for Caribou configuration, you can
configure Caribou in any way that gets a configuration map with the right
options into `caribou.core/init` in `{project}.boot`.  Your `{project}.core`
will call `{project}.boot/boot` to obtain this map when setting up the initial
handler.

## Configuration Options

Caribou is highly configurable in a number of ways.  Caribou configuration is
meant to work out of the box, while still allowing for any changes that might be
desired along the way.

### Default Configuration

There are a variety of options for configuring a Caribou site.  Most of these
you will not need immediately, but they are documented here for when they do
become necessary.

Here is a map of all default configuration options:

```clj
{:app {:use-database        true
       :public-dir "public"
       :default-locale "global"
       :localize-routes ""}
 :actions (atom {})
 :assets {:dir "app/"
          :prefix nil
          :root ""}
 :aws {:bucket nil
       :credentials nil}
 :controller {:namespace "{project}.controllers"
              :reload true
              :session-defaults (atom {})}
 :database {:classname    "org.h2.Driver"
            :subprotocol  "h2"
            :host         "localhost"
            :protocol     "file"
            :path         "/tmp/"
            :database     "taiga_development"
            :user         "h2"
            :password     ""}
 :error {:handlers (atom {})
         :templates (atom {})
         :show-stacktrace false}
 :field {:constructors (atom {})
         :namespace "{project}.fields"
         :slug-transform [[#"['\"]+" ""]
                          [#"[_ \\/?%:#^\[\]<>@!|$&*+;,.()]+" "-"]
                          [#"^-+|-+$" ""]]}
 :handler (atom nil)
 :hooks {:namespace "{project}.hooks"
         :lifecycle (atom {})}
 :index {:path "caribou-index"
         :default-limit 1000
         :store (atom nil)}
 :logging {:loggers [{:type :stdout :level :debug}]}
 :models (atom {})
 :nrepl {:port nil 
         :server (atom nil)}
 :pages (atom ())
 :pre-actions (atom {})
 :query {:queries (atom {})
         :enable-query-cache  false
         :query-defaults {}
         :reverse-cache (atom {})}
 :reset (atom nil)
 :routes (atom (flatland/ordered-map))
 :template {:helpers (atom {})
            :cache-strategy :never}
}
```

As you can see, there is a whole rainbow of options to choose from.  Let's take
them one by one.

### app

Here is where we hold the most general configuration level options.

* **use-database**

Determines whether or not a database is in use.  Usually left at `true`.

* **public-dir**

The directory that holds all of the static resources a site contains.  Anything
placed in the public directory is available at the url representing its file
path without having to go through the router.

* **default-locale**

The name given to the default locale.  If you are not using localization you can
safely ignore this option.  If you are using localization, this is the locale
that is given to request maps if no other locale is specified.

### actions

This is an atom with a map containing all controller actions in the site.  You
probably won't have to interact with this one directly, unless you have custom
actions that are not defined in controller files.

### assets

Anything having to do with uploaded files is configured in this map.  The
available keys in the assets map are:

* **dir**

This specifies where local files on disk will be stored after upload.  "app/" by
default, could be anywhere on the filesystem.

* **prefix**

When using s3 for storing assets, this defines the prefix inside the bucket that
will be appended to the beginning of any asset path.  This provides a means to
have assets from many sites stored in a single bucket (if desired).

* **root**

The asset root can be used in templates to prefix a given asset with a different
host.  This way different environments can have assets that originate from
different hosts, like one set of assets for staging and one set for production
for example.

### aws

Information about how to connect to amazon is stored here.  Because the
configuration can be different for different environments, you could have one
amazon bucket or account for one environment, and a different account or bucket
for another environment.

* **bucket**

The name of the s3 bucket that assets will be stored in.

* **credentials**

A map containing your AWS credentials of the form `{:access-key
"YOUR-ACCESS-KEY" :secret-key "YOUR-SECRET-KEY"}`

### controller

The various options for configuring controllers.

* **namespace**

The namespace prefix where all of the controllers in your site live.  Defaults
to `{project}.controllers`, which means that any controller namespace you want
to reference must start with `{project}.controllers.{controller}`.  Actions are
functions inside your controller namespace, so the `index` action inside your
`home` controller in the `taiga` project would be found at
`taiga.controllers.home/index`.

* **reload**

Defaults to true.  This reloads every action on every request, which is helpful
in development when you are modifying them all the time, but you probably want
to turn it off in production unless you are modifying your controllers at
runtime (which is not suggested for production!)

* **session-defaults**

Anything placed into the session-defaults atom will be available in a fresh
session created when a user first visits your site.

### database

Any and all information for connecting to a database go in this map.  Usually
the main feature of each environment's config file, it holds a variety of
options, some of which are relevant only to certain databases:

* **classname** -- *required*

The Java class representing the driver for the database.  You can't really
connect to the db unless there is a class that handles the connection, which
there is for every database we have encountered.

* **subprotocol** -- *required*

This string represents the subprotocol that is used to connect to the database
through the driver.  Every driver has some specific options (usually only one).

Current possible values: postgresql, mysql, h2

* **host** -- *required*

What host does your database live on?  For local database development this will
most likely be `localhost`, but in many situations this is a remote server.

* **database** -- *required*

The actual name of your database.

* **user** -- *required*

The user that is being used to access the database.

* **password** -- *required*

The password that belongs to the given user.

* **protocol**

This is a string representing the mode the database is connected to with, if
applicable.  For instance, H2 can use file access, tcp access or a variety of
others.  Ignore if this does not apply.

* **path**

For accessing file based databases, this represents the location of your
database on disk.  Again, only necessary for file based databases.

#### Some example database configurations

Here are a couple of examples of database configurations to get you started:

* Postgresql

```clj
{:database 
  {:classname "org.postgresql.Driver" 
   :subprotocol "postgresql"
   :host "127.0.0.1" 
   :database "caribou_test" 
   :user "caribou" 
   :password "TUNDRA"}}
```

* Mysql

```clj
{:database 
  {:classname "com.mysql.jdbc.Driver" 
   :subprotocol "mysql" 
   :host "localhost" 
   :database "caribou_test" 
   :user "caribou" 
   :password "TUNDRA"}} 
```

* H2

H2 requires a couple more fields to identify that you are using a file based
database and to specify the path.  (notice `:protocol` and `:path` are both
present, but not `:host`)

```clj
{:database 
  {:classname "org.h2.Driver" 
   :subprotocol "h2" 
   :protocol "file"
   :path "./" 
   :database "caribou_development" 
   :user "h2" 
   :password ""}} 
```

### error

When errors occur, these options governs how they are handled.

* **handlers**

This map holds custom error handlers for specific error codes.  So if you wanted
to do some custom action when a 404 is hit for instance, you could associate a
:404 key into this map with the value of a function to be run whenever a 404
occurs.  If no handler exists for that error, the default error handler is run.

* **templates**

A map holding templates that will be rendered in the case of various error
codes.  So a template that lives under the :404 key will be rendered whenever a
404 error occurs.

* **show-stacktrace**

Set this option to true if you want the stacktrace of any exception to appear in
the browser.  Not desirable for production when it is better practice to render
a custom 500 page, but in development this can be handy (especially if you
conjure a lot of stacktraces!)  Otherwise, the stacktrace is rendered out to the
logs and a 500 template is rendered in the browser.  Defaults to false.

### field 

* **constructors**

A map that contains all the Field constructors.  Since Field is a protocol, to
create one requires calling a constructor.  This is a map of Field type names to
functions which construct a Field of that type.  Handled automatically by
Caribou, you probably don't need to mess with this, but it is here if you need
it.

* **namespace**

A namespace to hold any custom user-defined Field types.  Any records you define
that implement the Field protocol that live in this namespace will be added as
types that can be created like any other built in Field type.

* **slug-transform**

Whenever a piece of content of a Model with a Field of type "slug" is saved, the
value for that Field is generated from a linked text Field according to the
transformation encoded in this configuration property.  By default this
transformation removes quotes and turns special characters and spaces into a
dash (-).  Want underscores instead?  Override this config option.

### hooks

Hooks are run at specific point during every piece of content's lifecycle.  The
various hook points are:

* **During create these hooks are called in order:** 

```clj
:before-save
:before-create 
:after-create 
:after-save 
```

* **During an update, these hooks are called in order:** 

```clj 
:before-save
:before-update 
:after-update 
:after-save 
```

* **When a piece of content is destroyed, these hooks are run:** 

```clj
:before-destroy 
:after-destroy 
```

* **namespace**

The namespace where the various hooks into the Model lifecycle go.  Every hook
namespace has a name of the form {hooks-namespace}.{model-name}, and hooks are
added in a function called {hooks-namespace}.{model-name}/add-hooks.

* **lifecycle**

The actual hooks that get run.  Rather than modifying this directly, just call
`caribou.hooks/add-hooks` from a file named after that model in your hooks
namespace.  

### index

The index options control how content is indexed in the built in Lucene search
engine.  This is used in the Admin but you can also use it in your own site.
http://lucene.apache.org/

Caribou uses the clucy library to abstract over the raw Java Lucene interface:
https://github.com/weavejester/clucy

* **path**

The directory that will hold the index files.  Defaults to "caribou-index".

* **default-limit**

The maximum number of documents a search will return.  Defaults to 1000.

* **store**

An atom of the actual clucy index object, if you need to perform any custom
operations on it.

### logging

Logging contains a list of logger specifications under the :loggers key.  These
specifications are a map containing two keys: `:type` and `:level`.  `:type`
indicates what endpoint the logger will output to (the default logger writes to
:stdout), and `:level` indicates what level of Caribou events to pay attention
to.

The different types currently supported are `:stdout`, `:file` and `:remote`.
`:stdout` simply outputs to stdout, and is the default logger type.  If `:file`
is chosen, you must also add a `:file` key to the map pointing at the file to
log to.  If the logger type is `:remote` then you must also include a `:host`
key which indicates what remote host to log to.  In the case of a remote host,
it uses UDP to send packets to the host, so the host must be running syslog and
must be configured to allow access from the server sending the packets.

The levels in order from most critical to least critical are:

```clj 
:emergency 0 
:alert 1 
:critical 2 
:error 3 
:warning 4 
:warn 4 
:notice 5
:informational 6 
:info 6 
:debug 7 
:trace 7 
```

If you set a logger to watch at `:warn` level for instance, it will ignore any
event below `:warn`, but output all messages from `:warn` level up to
`:emergency`.  `:emergency` level events are always output.

### models

This is a map that contains all Models in the system.  During a call to
`caribou.core/init` the Models are loaded from memory and added to this map
under a key containing the slug of the Model.  If you want to define Models that
are not represented in the Model table, you can add more keys to this map
(though this is probably unnecessary).

There is a whole section on [Creating Models](#creating-models) later on.

### nrepl

Nrepl provides a repl running inside the Caribou process that can be connected
to from the command line or from inside an editor with nrepl support:
https://github.com/clojure/tools.nrepl .  This is a great way to interact with a
running Caribou process and inspect or alter state using a given configuration.

If a `:port` is provided, then an nrepl server will be booted at that port when
Caribou is initialized.  In that case, a reference to the running server will be
stored in the atom under `:server`.  If no `:port` option is present, nrepl will not
be booted.

### pages

This provides a reference to the page tree for this Caribou instance.  Most
likely this will be populated during the definition of the handler in your
`{project}.core` namespace.  `{project}.core/reload-pages` is a function that
adds whatever routes you have to your site, which gets passed into the
invocation of the root handler, `caribou.app.handler/handler`, so that it can
reload the pages whenever necessary.  This is all covered in the section on
[Defining Pages and Routes](#defining-pages-and-routes).

### pre-actions

This configuration option holds the current map of existing pre-actions for
different pages.  Keyed by the slug of a page, pre-actions will be run before a
given action is evaluated.  This could be used for things like authorization or
processing of request parameters.  See the section on [Defining
Pre-Actions](#defining-pre-actions) for more details.

### query

The `query` option is the domain of the query cache.  Turned off by default, the
query cache will cache the results of every query map that
`caribou.model/gather` sees.  There are a variety of entries in this map that
play different roles in the inner workings of the query cache.

* **enable-query-cache**

To turn on the query-cache, simply set this option to `true` in your config.
Not necessary for development, but a good thing to do in production if you know
that your content is not necessarily changing often.  Even if it does change,
the cache will be invalidated on any update to that model, so your site will
remain current.

* **queries**

An atom containing the map of queries to results.  Populated automatically by
the query cache (but fun to inspect, if you are into that kind of thing).

* **reverse-cache**

Tracks the models that are hit by each query.  Invalidates those caches in the
case of an update.

* **query-defaults**

This map will be added automatically to any query issues through a
`caribou.model/gather`.  Want to restrict your content to only those "enabled"?
This is the place to do it.

### reset

This is a reference to a user-defined function passed into the initial creation
of the frontend Caribou handler in your `{project}.core/init` function.  It
should do any kind of necessary initialization work that your site requires
(like loading pages or defining routes, for instance).  It is added
automatically in a newly generated Caribou site.

### routes

This is an ordered map of your routes.  The routes map url patterns to the
actions that are triggered by them.  One by one each pattern is tested against
an incoming url until it is matched or a 404 is issued.  Once a route is matched
the corresponding action is called with the request map as a parameter.  See
more at [Defining Pages and Routes](#defining-pages-and-routes).

### template

The various options pertaining to the built-in template rendering live here.

* **cache-strategy**

This option governs the caching strategy used by the template engine.  The
possible values are currently `:never` or `:always`.

* **helpers**

This is a map containing the default helpers that will be available during the
rendering of every template.  To find out all about helpers check out the
section on [Template Helpers](#template-helpers).

# Introduction to Data Modeling

Defining models for an application is the heart of a Caribou project.  Once a
model is created a host of capabilities are automatically generated for that
newly created model.  This section details the means for creating new models and
expanding on existing models.

## Creating Models

Creating a model is just like creating any other content in a Caribou project.
The first step is to acquire a configuration map, which is detailed in the
[How Configuration Works in Caribou](#how-configuration-works-in-caribou)
section.  

Assuming a configuration exists and it is called `config`, a model can be
created from the repl with the following call:

```clj
(caribou.core/with-caribou config 
  (caribou.model/create 
   :model
   {:name "Presentation"
    :fields [{:name "Title" :type "string"}
             {:name "Preview" :type "asset"}]}))
```

Some things to note about this code:

* The first line calls `caribou.core/with-caribou` with an existing
configuration map.  This configuration map among other things contains
information about the database connection.  Since this call is creating a new
model, this will actually generate a new table for that model inside whatever
database is referred to by the given configuration map under its `:database`
key.  This means of configuration means that you can create models in different
databases just by swapping out the configuration map.  For clarity, from here on
out we will assume the config map is provided.

* The next line calls the fundamental function `caribou.model/create`.  This
call is used to create any content inside of a Caribou project, and corresponds
to inserting a new row in the database given by the configuration map.

* The next line contains only the key `:model`, and signifies that we are
creating a model, as opposed to any other content type currently known to the
system.  Once a model is created (in this case the Presentation model), content
of that variety can be created using the same call, but swapping out the key
here (which for the case of Presentations, would be `:presentation`).  If a call
to `caribou.model/create` is made with a key that does not represent a current
model known to the system this will throw an exception.

* Next comes a map of properties that define the model being created.  This list
of properties has a key for each Field in the Model model.  Given a different
model, the available keys in this map would be different.

* Ultimately, the definition of a model really depends on the fields in that
model.  In this case, two custom fields are created for the Presentation model,
a Title of type "string", and a Preview of type "asset".  Once this model
exists, new Presentations can be created that have titles and previews in the
same manner:

```clj
(caribou.model/create 
 :presentation
 {:title "Caribou!"
  :preview {:source "path/to/preview/image.png"}}))
```

In this way, creating a model allows new kinds of content to be created.
Everything else in Caribou flows from this basic idea.

## Field Types

There are a number of different field type models can have.  Here is a summary:

* **address** - Store a location as a set of fields or lat/lng pairs.
* **asset** - Represents any kind of file, including images.
* **boolean** - Represent a single true/false value.
* **decimal** - Store a single decimal value of arbitrary precision.
* **enum** - Represent a finite set of possible values.
* **integer** - A single number with no decimal digits.
* **password** - Store an encrypted value that can be matched but not read.
* **position** - A value that automatically increments when new content is added.
* **slug** - A string that depends on some other string field for its value, and
    reformats that string according to the [field](#field) configuration.
* **string** - The workhorse.  Represents a single short string.
* **structure** - Stores arbitrary clojure data structures in EDN format.  
* **text** - Used to store arbitrarily long text.  
* **timestamp** - Represents dates and times of all varieties.

## Associations

Beyond the simple field types, much of the richness of a Caribou model structure
lies in the associations that are created between models in the system.  Model
and Field have this relationship, where Model has a "collection" of Fields and
Fields are a "part" of Model.  This provides a one to many relationship between
the Model model and the Field model.

Every association in Caribou is represented by a field in the corresponding
models, which means that there is an association field in each model
representing the two sides of the association.  This means each association type
has a reciprocal type, and that every association has one and only one
reciprocal association field that lives in another model somewhere.

The different types of associations available in Caribou are:

* **collection** - This association field type represents a collection of
    things, meaning there are potentially many pieces of content associated to
    any content of this model type.  The reciprocal type of association is the
    "part".
    
* **part** - The reciprocal to "collection", this means that any content of this
    model variety will potentially belong to content of the model that it is a
    "part" of.  Any content that is part of another collection cannot belong to
    another collection.
    
* **link** - The link association type is its own reciprocal, and represents a
    many to many relationship to another model.  This behaves just like a
    collection except that the associated content can have many associations as
    well.

## Creating and Updating Content

As detailed before at the end of [Creating Models](#creating-models), once a
model has been created, new content can be created according to that model.

```clj
(caribou.model/create 
 :model
 {:name "Presentation"
  :fields [{:name "Title" :type "string"}
           {:name "Preview" :type "asset"}]}))

(def caribou-presentation
  (caribou.model/create 
   :presentation
   {:title "Caribou!"
    :preview {:source "path/to/preview/image.png"}})))
```

The first call to `caribou.model/create` creates the Presentation *model*, and
the second creates new Presentation *content*.  Notice the fields defined during
model creation are available during content creation time.  Next, let's create a
new Slide model and associate it to Presentation:

```clj
(caribou.model/create 
 :model
 {:name "Slide"
  :fields [{:name "Image" :type "asset"}
           {:name "Caption" :type "string"}
           {:name "Presentation" :type "part"
            :target-id (caribou.config/draw :models :presentation :id)}]}))
```

The key here is that we made a new field called "Presentation" of type "part".
In order to associate this new field to the Presentation model, we need the id
of the Presentation model, which lives inside the current Caribou config.  It
can be accessed using the `caribou.config/draw` method, which indexes anywhere
inside the currently applied configuration map.  In this case, we need only the
`:id`, which is passed in as the new association field's `:target-id`.

Since the new "Presentation" field inside the Slide model is of type "part", a
reciprocal "collection" association is automatically created inside of the
Presentation model.  Now, Slides can be created and associated to Presentations:

```clj
(def first-slide
  (caribou.model/create 
   :slide
   {:caption "Welcome to Caribou!"
    :image {:source "welcome/to/caribou.jpg"}
    :presentation caribou-presentation})))
```

Since Presentation has a collection of Slides, you can also create Slides in the
context of a given Presentation using `caribou.model/update`:

```clj
(caribou.model/update
 :presentation 
 (:id caribou-presentation)
 {:title "Caribou Redux!"
  :slides [{:caption "Explaining Caribou Models"
            :image {:source "explaining/caribou/models.jpg"}}
           {:caption "How to Update a Caribou Model"
            :image {:source "updating/caribou/models.jpg"}}]}))
```

This creates two new Slides and associates them to the given presentation.  A
couple things to note about this update:

* `caribou.model/update` requires an additional parameter which is the `:id` of
  the preexisting content item you wish to update.  This is automatically
  generated when a content item is first created, so is present in the map that
  is returned from the original call to `caribou.model/create` that created that
  content item (above this was stored under the var `caribou-presentation`).

* To add items into the collection, we provide a vector of maps under the
  `:slides` key in the update.  This works just as well for create.  Each map in
  the collection vector will be created and associated to the given object.  In
  fact, this is how we created the model originally, since `:fields` is a
  collection that lives in the Model model.  If one of these maps contains an
  `:id`, it will find the associated item with the given id and update it rather
  than creating a new one.

## Default Model Fields

There are a number of default fields that are added to a model automatically.
These play various roles in managing the content internally, and also provide
some handy features that all content is likely to need.  These fields are:

* **:id** -- The `:id` represents a unique integer identifier that is used
    throughout Caribou.  Every content item in Caribou is given an `:id`, and
    all content can be retrieved based on its model type and its `:id`.  This is
    also the mechanism under the scenes that tracks how different items are
    associated to one another.  `:id` always increments starting from `1`, so
    every item obtains a unique `:id` within its model table.

* **:position** -- The `:position` field allows content to be ordered in an
    arbitrary fasion.  Without the `:position` field we would be stuck
    retrieving content only by name, or id or title or something.  `:position`
    allows people to order content exactly how it should appear.  Without
    outside intervention, `:position` increments automatically starting from
    `1`, just like `:id`.  `:position` however can change, whereas once an `:id`
    is acquired it is invariant for the lifetime of the application.

* **:locked** -- This boolean field, if `true`, prevents the given content item
    from being modified by a `caribou.model/update` call.  This is handy to
    protect the built in model fields from arbitrary changes which could
    undermine the very functioning of Caribou itself.  That is not to say built
    in models are unchangeable: new fields can be added to any model.  But
    someone cannot remove the "Name" field from a model, for instance.  Caribou
    needs this field to run.  Probably you will not need to set this field
    yourself, but you could have a vital content item that plays a similar role
    in the application as a whole, in which case setting it to `locked` will
    safeguard that content from changing out from under you.

* **:created-at** -- This is a timestamp that is set automatically when a piece
    of content is created.  This way you always know when something was created!

* **:updated-at** -- This is another timestamp, but it gets set every time
    something is updated.  Can be useful to order by this if you always want the
    most recent content (or least recent!)

## Retrieving Content

Once models and content have been created, the ideal thing would be to be able
to retrieve it again!  This capability is provided by the `caribou.model/gather`
and `caribou.model/pick` functions.

To retrieve all Presentations in the system, we just gather them:

```clj
(def all-presentations
  (caribou.model/gather :presentation))
  
--> [{:id 1 :title "Caribou Redux!" :preview {...} ...}] ;; a lot of information not shown here
```

`caribou.model/pick` is just like gather, except it only returns a single item:

```clj
(def first-presentation
  (caribou.model/pick :presentation))

--> {:id 1 :title "Caribou Redux!" :preview {...} ...}
```

Without arguments, `pick` will return the first item, and `gather` will return
all items.  To refine our results, an options map can be passed in as the second
argument:

```clj
(def all-presentations
  (caribou.model/gather
   :presentation
   {:where {:title "Caribou Redux!"}}))
  
--> [{:id 1 :title "Caribou Redux!" :preview {...} ...}]
```

This map presents one of the features of a gather map, `:where`.  The full list is:

* **:where** -- present conditions which narrow and refine the results.
* **:include** -- fetch associated content along with the primary results.
* **:order** -- order the gathered results based on given criteria.
* **:limit** -- limit primary results to a certain number.
* **:offset** -- index into results by the given offset.

Let's take a look at these one by one.

### **:where**

One of the great sources of power for the gather call is that the `:where` map
can express conditions across associations:

```clj
(def redux-slides
  (caribou.model/gather
   :slide
   {:where {:presentation {:title "Caribou Redux!"}}}))
  
--> [{:id 1 :caption "Welcome to Caribou!" ...}
     {:id 2 :caption "Explaining Caribou Models" ...} 
     {:id 3 :caption "How to Update a Caribou Model" ...}]
```

The point here is that we are gathering slides based on a condition that exists
on the associated Presentation item.  This is cool.

You can also have parallel conditions.  This acts like a logical "AND":

```clj
(def redux-slides
  (caribou.model/gather
   :slide
   {:where {:presentation {:title "Caribou Redux!"}
            :id {:>= 2}}}))
  
--> [{:id 2 :caption "Explaining Caribou Models" ...} 
     {:id 3 :caption "How to Update a Caribou Model" ...}]
```

### **:include**

One thing you will notice right away when gathering content is that though
associations exist, associated items do not come through the regular
`caribou.model/gather` call by default.  This is what the `:include` map is for.
The `:include` map defines a nested set of association field names that trigger
the retrieval of associated content.

```clj
(def redux-and-slides
  (caribou.model/pick
   :presentation
   {:where {:title "Caribou Redux!"}
    :include {:slides {}}}))
  
--> {:id 1 
     :title "Caribou Redux!" 
     :preview {...}
     :slides [{:id 1 :caption "Welcome to Caribou!" ...}
              {:id 2 :caption "Explaining Caribou Models" ...} 
              {:id 3 :caption "How to Update a Caribou Model" ...}]}
```

The `:include` map can travel arbitrarily deep along the model association
graph, so if Slide had a collection of another model, say "Paragraphs", then you
could retrieve those as well with another level of the `:include` map:

```clj
(def redux-slides-and-paragraphs
  (caribou.model/pick
   :presentation
   {:where {:title "Caribou Redux!"}
    :include {:slides {:paragraphs {}}}}))
  
--> {:id 1 
     :title "Caribou Redux!" 
     :preview {...}
     :slides [{:id 1 :caption "Welcome to Caribou!" :paragraphs [...] ...}
              {:id 2 :caption "Explaining Caribou Models" :paragraphs [...] ...} 
              {:id 3 :caption "How to Update a Caribou Model" :paragraphs [...] ...}]}
```

You can also perform parallel includes, so if a Presentation also had an
association to an existing "Person" model called "Authors", you could retrieve
the Presentation, all its Slides and their Paragraphs, and the Authors of the
Presentation all in one gather call:

```clj
(def redux-authors-and-slide-paragraphs
  (caribou.model/pick
   :presentation
   {:where {:title "Caribou Redux!"}
    :include {:authors {}
              :slides {:paragraphs {}}}}))
  
--> {:id 1 
     :title "Caribou Redux!" 
     :preview {...}
     :authors [{:name "Donner"} {:name "Blitzen"} ...]
     :slides [{:id 1 :caption "Welcome to Caribou!" :paragraphs [...] ...}
              {:id 2 :caption "Explaining Caribou Models" :paragraphs [...] ...} 
              {:id 3 :caption "How to Update a Caribou Model" :paragraphs [...] ...}]}
```

Obviously this can get out of control, and it wouldn't be hard to pull in every
content item in the site in a single call.  Any single gather call can be broken
into individual gathers that fetch the content when needed.

### **:order**

The `:order` map is used to control the order of the returned items.  By
default, content is ordered based on that model's `:position` field, but any
order can be used.  Here is an example of ordering by `:updated-at`:

```clj
(def redux-slides-ordered-by-updated-at
  (caribou.model/gather
   :slide
   {:where {:presentation {:title "Caribou Redux!"}}
    :order {:updated-at :desc}}))
  
--> [{:id 3 :caption "How to Update a Caribou Model" :updated-at #inst "2013-06-21T22:37:35.883000000-00:00" ...}
     {:id 2 :caption "Explaining Caribou Models" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...} 
     {:id 1 :caption "Welcome to Caribou!" :updated-at #inst "2013-06-21T22:37:33.883000000-00:00" ...}]
```

The value for the property being ordered can be either `:asc` or `:desc`,
representing ascending or descending respectively.

The `:order` map, like the `:where` and `:include` map, can propagate across
associations, and order across many properties simultaneously:

```clj
(def redux-slides-parallel-ordering
  (caribou.model/gather
   :slide
   {:order {:updated-at :desc
            :id :asc
            :presentation {:title :desc}}}))
  
--> [{:id 3 :caption "How to Update a Caribou Model" :updated-at #inst "2013-06-21T22:37:35.883000000-00:00" ...}
     {:id 1 :caption "Welcome to Caribou!" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...}
     {:id 2 :caption "Explaining Caribou Models" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...}]
```

### **:limit**

The `:limit` option specifies a maximum number of items to retrieve, in the case
that there are more items than you wish to handle at any given time:

```clj
(def redux-slides-limited
  (caribou.model/gather
   :slide
   {:order {:updated-at :desc
            :id :asc
            :presentation {:title :desc}}
    :limit 2}))
  
--> [{:id 3 :caption "How to Update a Caribou Model" :updated-at #inst "2013-06-21T22:37:35.883000000-00:00" ...}
     {:id 1 :caption "Welcome to Caribou!" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...}]
```

One thing to note: only the outermost model is limited.  Any items included
across associations will not be limited.  Keep this in mind if you have items
with a large number of associated items in a collection or link.  In that case
it is better to not include the content directly, but rather to make an
additional gather on associated items once the outer item is retrieved:

```clj
(let [presentation   (caribou.model/pick
                      :presentation
                      {:where {:title "Caribou Redux!"}})
      limited-slides (caribou.model/gather
                      :slide
                      {:where {:presentation {:id (:id presentation)}}
                       :limit 2})]
  (assoc presentation :slides limited-slides))
```

### **:offset**

`:offset` is used in conjunction with `:limit`.  It finds subsequent sets of
content given whatever would be returned from the gather normally, but has been
excluded through the use of a `:limit`.

```clj
(def redux-slides-limited-and-offset
  (caribou.model/gather
   :slide
   {:order {:updated-at :desc
            :id :asc
            :presentation {:title :desc}}
    :limit 2
    :offset 1}))
  
--> [{:id 1 :caption "Welcome to Caribou!" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...}
     {:id 2 :caption "Explaining Caribou Models" :updated-at #inst "2013-06-21T22:37:34.883000000-00:00" ...}]
```

This can be used to implement pagination, for example.

## Data Migrations

When making model changes (which ultimately change the schema of the tables you
are working with), it is wise to implement them as migrations so that you can
recreate your schema in any database environment you will eventually encounter.
This makes it easy to switch databases or even database libraries and continue
to use your existing code.

To create a migration, there are three steps:

* Writing the migration
* Specifying the order of the migration
* Running the migration

Let's look at the first of these tasks.

### Writing a migration

First, create a new namespace inside your `src/{project}/migrations/` directory.
It should contain two functions, `migrate` and `rollback`:

```clj
(ns taiga.migrations.example
  (:require [caribou.model :as model]))
  
(defn migrate
  []
  (model/create
    :model
    {:name "Example"
     :fields [{:name "Content" :type "string"}]}))
     
(defn rollback
  [])
```

Once the migration exists, you have to add it into the order vector that keeps
track of migration order.  This is required because migrations are not
necessarily commutative: certain migrations must have already run before others
can make sense.  This is most apparent for a migration that adds a field to an
existing model: if the model hasn't been created yet, adding a field to it will
fail!

Every project has an `{project}.migrations.order` namespace in the
`src/{project}/migrations/order.clj` file.  Open this and add your migration to
the list somewhere it makes sense:

```clj
(def order ["default" "admin" "example"]) ;; <--- Here for example!
```

Once order has been instated, time to run the migration:

```
% lein caribou migrate resources/config/development.clj
```

The `lein caribou migrate` command accepts the path to a config file because it
needs to know what database to run the migration on.  This is helpful if you
have many different environments each with their own database (that may or may
not live on this local machine).  `lein caribou migrate` keeps track of which
migrations have already been run in that database and ensures each migration
only runs once and in the order specified in your `{project}.migrations.order`
namespace.

## Content Localization

Localizing content in Caribou means providing different values for the fields in
a content item depending on what "locale" the application is receiving requests
from.  Localization of a Model is done on a field by field basis.  This means
that even what items are associated to what can be localized if desired.

To begin, let's create a model that will hold content that varies between
locales (consider this example to be entirely contrived):

```clj
(caribou.model/create 
  :model 
  {:name "Wisdom" 
   :fields [{:name "quotation" 
             :type "string" 
             :localized true}]})
```

Notice the line `:localized true`.  This signifies that values stored in this
field will have different values based on which locale is being requested.

Next, let's create a new locale.  Because this is a tutorial, we will create a
locale for Klingon (complete with utterly fabricated locale code):

```clj
(caribou.model/create 
  :locale 
  {:language "Klingon" 
   :region "Qo'noS" 
   :code "ql-QN"})
```

These are the three required fields for created a locale.  Notice that creating
a locale is exactly the same as creating any other content in Caribou.  Locale
is a model.  Everything is a model.  Even Model is a model.

Next, let's create a new instance of our new Wisdom model.  This is easy, we
know how to do this:

```clj
(caribou.model/create
  :wisdom
  {:quotation "Trust, but verify"})
```

To get the basic instance back, we can call gather on the Wisdom model:

```clj
(caribou.model/gather :wisdom)

---> ({:id 1 :quotation "Trust, but verify" ...})
```

But the whole point is to pull the content for our new Klingon locale, "ql-QN".
To do this, we simply specify the locale code in the gather:

```clj
(caribou.model/gather :wisdom {:locale "ql-QN"})

---> ({:id 1 :quotation "Trust, but verify" ...})
```

This is great, but it still has the same value.  This is because we haven't
specified what the localized value should be.  To do that, let's call
`caribou.model/update` with the right locale:

```clj
(caribou.model/update 
  :wisdom
  1
  {:quotation "yIvoq 'ach yI'ol"}
  {:locale "ql-QN"})
```

Notice how `update` takes a second map.  The first map is only for specifying
what values the content has, while the second is full of modifiers and options
that won't actually be directly committed as values for this instance.

Now when we do our gather, we get the right values:

```clj
(caribou.model/gather :wisdom {:locale "ql-QN"})

---> ({:id 1 :quotation "yIvoq 'ach yI'ol" ...})
```

Whereas the original non-localized version still exists:

```clj
(caribou.model/gather :wisdom)

---> ({:id 1 :quotation "Trust, but verify" ...})
```

This non-localized version is actually part of the "global" locale, which is
always present.  The "global" locale also supplies values for instances that don't
have a value in the localized field.  So until a specific value is given to the
`quotation` for the "ql-QN" locale, it will inherit the value that exists in
"global".  This allows you to just override the content that needs to be
overridden and provide, for instance, the same image in all locales except the
specific ones that need their own image.

# Defining Routes and Pages

When a user visits a URL in your site, the Caribou Router is what matches that
URL and sends a request to a particular controller you have defined in your
`src/{project}/controllers` directory.  These controllers are just Clojure
namespaces which contain a collection of functions (which we call "actions"),
each of which can conjure a response based on a given request.

In order to perform this magic, you have to specify which URLs map to which
controller actions, and what parts of that URL are parsed and provided to the
action in the form of parameters.  This happens through the use of two new
concepts in Caribou: Routes and Pages.

Routes define a routing hierarchy which is based on URL paths.  Every route
defines a path (which is a string to match), a key which will be used to map it
to a page, and a set of child routes, each of which inherits the first part of
its path from its parent.  This tree will then be used by the router to route
requests based on their URL to the controller actions given by that route's key.

The simplest route would be one that matches the empty path, "/", and maps to a
home page.  This is given below:

```clj
["/" :home []]
```

The path is "/", the key is `:home`, and its children routes are empty.
Needless to say, routes for a site can become much more elaborate than this, but
they are all represented in this same format.

The above is a single route, but in practice routes come as a collection.  So an
example of the simplest routing a site could have would be something like the
following:

```clj
(def routes
  [["/" :home []]])
```  

As you can imagine, there could be several routes living in parallel:

```clj
(def routes
  [["/"               :home      []]
   ["/place"          :place     []]
   ["/somewhere-else" :somewhere []]])
```  

Pages are represented as a map where the keys are the same as those defined by
the routes, and each value is a specification of where to route any incoming
request that matches:

```clj
(def pages
  {:home {:GET {:controller "home" :action "index" :template "home.html"}}})
```

In this case, this page will be triggered by any route containing the `:home`
key, and the next map discerns which methods this page will match.  So in the
case of a GET request, the corresponding controller that will be activated is
the "home" controller, which is located in `src/{project}/controllers/home.clj`
in the `{project}.controllers.home` namespace, and the action that will be
called will be a function by the name of "index" defined in that namespace.

There can be multiple methods if desired:

```clj
(def pages
  {:home {:GET    {:controller "home"  :action "index"  :template "home.html"}
          :POST   {:controller "home"  :action "login"  :template "login.html"}
          :PUT    {:controller "home"  :action "update" :template "acknowledge.html"}
          :DELETE {:controller "hades" :action "perish" :template "writhing.html"}}})
```

Once we have a set of routes and a page map, we can combine them into a page
tree that Caribou can use to build a router.  The function for this is named
`caribou.app.pages/build-page-tree`, and it is called with a seq of routes and a
map of pages and returns a page tree:

```clj
(def page-tree
  (caribou.app.pages/build-page-tree routes pages))
```

Putting this all together we have the creation of a full page tree:

```clj
(def routes
  [["/" :home []]])

(def pages
  {:home {:GET {:controller "home" :action "index" :template "home.html"}}})

(def page-tree
  (caribou.app.pages/build-page-tree routes pages))
```

This can later be given to the initialization of the Caribou handler that will
be running your site.  It will define the routing structure and URL matching
that will be followed by the running app.

## Routes are Matched based on Paths

The routes you define govern the way URLs coming from requests will be matched.
So given a set of routes, you can tell how an incoming URL will be handled.
Take the following case:

```clj
(def routes
  [["/"               :home      []]
   ["/place"          :place     []]
   ["/somewhere-else" :somewhere []]])
```  

Here there are three separate routes.  Any incoming request will match one of
these routes, or trigger a 404.  Caribou routes match given a trailing slash or
not, so:

```
http://localhost:33333                   --->  :home
http://localhost:33333/                  --->  :home
http://localhost:33333/place             --->  :place
http://localhost:33333/place/            --->  :place
http://localhost:33333/somewhere-else    --->  :somewhere
http://localhost:33333/somewhere-else/   --->  :somewhere
http://localhost:33333/off-the-map       --->  404!
```

## Route Elements can be Variable

This is all well and good, but what if you want to pull up a model by id?  Do
you need a route for every id that could be called?

This is where variable slugs come into play.  You can specify a placeholder path
element with a `:`, and when the router matches it it will parse the path and
pass the value in as a named parameter.

Here is an example:

```clj
(def routes
  [["/"            :home           []]
   ["/place"       :general-place  []]
   ["/place/:name" :specific-place []]])
```

In this case, the router will match any URL of the form "/place/*" and assign
whatever the * is to a parameter called `:name`.  So:

```
http://localhost:33333                   --->  :home
http://localhost:33333/place             --->  :general-place
http://localhost:33333/place/hello       --->  :specific-place  {:name "hello"}
http://localhost:33333/place/earth       --->  :specific-place  {:name "earth"}
```

Once the request reaches your controller, you can access the value of `:name` in
the request map:

```clj
;; request to http://localhost:33333/place/earth

(defn place
  [request]
  (println (-> request :params :name)))
  
---> "earth"
```

One word of caution: a variable slug can shadow a specific slug, so the ordering
of your routes matters:

```clj
(def routes
  [["/place/:where"  :variable-place []]    ;; <--- absorbs all requests
   ["/place/here"    :right-here     []]])  ;; <--- never called!
```

This is easily resolved by swapping the order:

```clj
(def routes
  [["/place/here"    :right-here     []]    ;; <--- now this works
   ["/place/:where"  :variable-place []]])  ;; <--- called only if the previous fails to match
```

## Routes can be Nested
## Paths are Inherited from Parent Routes
## Pages Tie Routes to Controllers and Templates
## Defining a Siphon

## Providing your Pages to the Caribou Handler

Once you have acquired a page tree, you can pass it into a call to
`caribou.app.pages/add-page-routes`.  This is already happening inside your
`{project}.core` namespace in the `{project}.core/reload-pages` function (this
is where the Admin and the API are added into your site).  This function
is eventually handed to the core Caribou handler that runs your site so that all
routes can be reloaded when necessary:

```clj
(defn reload-pages
  []
  (pages/add-page-routes
   admin-routes/admin-routes
   'caribou.admin.controllers
   "/_admin"
   admin-core/admin-wrapper)

  (pages/add-page-routes
   api-routes/api-routes
   'caribou.api.controllers
   "/_api"
   api-core/api-wrapper)

  (pages/add-page-routes
   (pages/all-pages)
   (config/draw :controller :namespace)))
```

The first two calls to `pages/add-page-routes` add in the Admin and API routes
respectively.  The last one is currently adding in all the pages defined in the
database (usually created through the Admin) by calling `pages/all-pages`, but
you can give it any page tree you have created here.

Notice also that `pages/add-page-routes` has a number of additional arguments
that can be passed in.  The first argument is a page tree, and the second is the
controller namespace.  If you want to move where you store your controllers you
can change this in your config, or just hardcode something here (like was done
for the Admin and API, each of which have controller namespaces that live inside
those respective projects).  The third argument is a URL prefix, which is how
all the Admin and API routes end up living under "/\_admin" and "/\_api".  

An example `{project}.core/reload-pages` that does not include the Admin or API
but does use your custom routes and pages using a custom controller namespace
and a different URL prefix would look something like this:

```clj
(def routes
  [["/" :home []]])

(def pages
  {:home {:GET {:controller "home" :action "index" :template "home.html"}}})

(def page-tree
  (pages/build-page-tree routes pages))
  
(defn reload-pages
  []
  (pages/add-page-routes
   page-tree
   'some.other.controller.namespace
   "/lives/somewhere/else"))
```

Of course if you want to use the default controller namespace and have your
routes live at the root, it is as simple as:

```clj
(defn reload-pages
  []
  (pages/add-page-routes page-tree))
```

# Writing Controllers

The whole point of the Caribou router is to funnel requests to the appropriate
controller action with the right parameters.  Once a request comes through, an
action is simply a function that is called with the request map as an argument,
and evaluates to a valid response map.  In between of course, all kinds of magic
can happen.

## Controllers are Namespaces which contain Actions

To define a controller namespace, add a new file to your
`src/{project}/controllers` directory with the name of the new controller.  So
for this example it would be `src/taiga/controllers/example_controller.clj` with
the following contents:

```clj
(ns taiga.controllers.example-controller
  (:require [caribou.model :as model]
            [caribou.app.controller :as controller]))
```

Now you are ready to start writing some actions!

## Controller Actions are Functions

To create a controller action is simply to write a function.  Caribou uses the
[Ring protocol](https://github.com/ring-clojure/ring) as its basis for handling
requests and returning responses.  In its simplest form, a controller looks like
this:

```clj
(defn basic-action
  [request]
  {:status 200 :body "This is a simple response"})
```

Here we are ignoring anything in the request map and simply returning a response
of 200 with the body "This is a simple response".  No fancy markup, no database
transactions, nothing.  If you have simple needs, this may be all you require. 

However, it is likely that you will want some information that lives in the
request.  That is the subject of the next section.  

## Contents of the Request Map

Living in the request map are a variety of helpful keys that provide information
about the nature of the incoming request.  This is the basic information any
controller action can use to tailor a response to that specific request.  

There are some basic keys that are available in any Ring request, currently the
following:

```clj
:uri 
:scheme 
:content-type 
:content-length 
:character-encoding 
:headers 
:request-method 
:body 
:ssl-client-cert 
:remote-addr 
:server-name 
:server-port 
```

Yet more are added by some Ring middleware that Caribou includes by default
(which you are free to remove if you wish):

```clj
:cookies 
:session 
:query-string 
:params 
:query-params 
:form-params 
:multipart-params 
```

There is a salad of params types that is an artifact of each being provided by a
separate ring middleware handler.

Then there are the keys added by Caribou.  There are some basic ones which are
provided to help with rendering:

* **:template** A function which renders the template associated to this page
    when called with a map of substitution values.
* **:page** A reference to the Page item that was matched during routing time.
* **:is-xhr** A boolean which signifies whether or not this request is xhr.
* **:route-params** A map of any parameters extracted from the url when the
    route was matched.

And then there are all the helpers.  A helper is simply a clojure function that
lives inside request map.  Caribou provides a handful of helpers by default, and
you can add any more that seem helpful.

```clj
;; value handling
:equals 

;; string handling
:truncate 
:linebreak 
:smartquote 

;; routing
:route-for 

;; image resizing
:resize 

;; date handling
:now
:ago 
:hh-mm 
:yyyy-mm-dd 
:yyyy-mm-dd-or-current 
:date-year 
:date-month 
:date-day
```

## Parameters from Routes are Available in Controllers

In order to provide something beyond our first simple action, let's use some of
the information from the incoming request.  In this example, we use a `:name`
parameter to customize our response:

```clj
(defn parameter-action
  [request]
  (let [request-name (-> request :params :name)]
    {:status 200 :body (str "Hello " request-name "!")}))
```

This way, if this action is triggered by a page associated to the route
"/hello/:name" for instance, the `:name` parameter will be set by whatever the
value of the url is in that position.  So if someone makes a request to
"/hello/lichen" the response will come back as

```
Hello lichen!
```

One basic pattern that is used over and over is to pull up some content from a
model based on the value of a parameter and use that to form the response.  An
example would be, given the route "/hello/:name" and a request to
"/hello/antler", to pull up some content from a "User" model and respond with
something that lives in that instance.  In this case we can say that the User
model has a "Greeting" field that they prefer to be greeted by that is stored in
the database:

```clj
(defn pick-action
  [request]
  (let [request-name (-> request :params :name)
        user (model/pick :user {:where {:name request-name}})
        greeting (:greeting user)] ;; this user's :greeting is "Obo"
    {:status 200 :body (str greeting " " request-name "!")}))
```

The response for this would be:

```
Obo antler!
```

## Rendering Provides Data to Templates

If you are using Caribou's default templating language,
[Antlers](https://github.com/antler/antlers), you can use the built in
`caribou.app.controller/render` method to render your templates.  It will use
the template defined in the page that routed the request to this action in the
first place.  So instead of returning a map with `:status` and `:body` in it,
you can just call render on some parameters instead.  A basic call looks like
this:

```clj
(defn pick-action
  [request]
  (let [request-name (-> request :params :name)
        user (model/pick :user {:where {:name request-name}})]
    (controller/render (assoc request :user user))))
```

The user map for this example contains:

```clj
{:greeting "Salutations" :name "Tundra Warrior"}
```

Then in a template:

```html
{{user.greeting}} {{user.name}}!
```

And out comes!:

```
Salutations Tundra Warrior!
```

Any key that is present in the map passed into `caribou.app.controller/render`
can be used inside a template, including information about the request and the
currently rendering page.  So if you need a page title and the current URL for
instance,

```html
<html>
  <head>
    <title>{{page.title}}</title>
  </head>
  <body>
    <p>You are currently visiting {{uri}}!  Welcome!</p>
  </body>
</html>
```

More information about template rendering can be found in the
[Rendering Templates](#rendering-templates) section.

## Defining Pre-Actions

# Rendering Templates

## Data from the Render Map is Accessible in Templates
## Template Helpers
## Existing Helpers
## Working with Assets
## Templates can Inherit Structure from other Templates
## Templates can Invoke other Templates as Partials

# Miscellaneous Topics

## Connecting to a Caribou Repl

Caribou provides an embedded nrepl server that lives inside the context of the
currently running configuration.  This means you won't have to call every
`caribou.model/gather` or `caribou.model/create` inside a
`caribou.core/with-caribou` call.

For a new Caribou project the repl is enabled by default.  It lives under the 
configuration option 

```clj
{:nrepl {:port 44444}}
```

If you want to disable this simply remove this entry and the nrepl server will
disappear.  To use it, specify a port (44444 by default) and connect using your
favorite nrepl client!

## Search Indexing
## Query Cache

# Using the Admin

## Caribou comes with a Default Admin
## Creating Models in the Admin
## Creating Pages and Routes in the Admin
## Managing Content in the Admin
## Accounts in the Admin

# Using the API

The API is already running when you fire up a Caribou project.  Its function is
to make available all the content in your system as json, xml or csv.  Every
time a model is created, a corresponding API for that model is instantly
available.

## All Content is Accessible from the API

Once a new model is created, any instances of that model can be accessed at a
URL of the form:

```
http://localhost:33333/_api/{model-name}
```

So, to access the Model API, simply navigate to
http://localhost:33333/_api/model .  You will see a json representation of every
model in the system.  If you want a specific representation, add it as a file
extension to this basic URL structure:

```
http://localhost:33333/_api/model.json
http://localhost:33333/_api/model.xml
http://localhost:33333/_api/model.csv
```

## Options in the API

All the options you would pass into a `caribou.model/gather` are available in
the API.  Add any additional constraints as query parameters to refine your
selection:

```
http://localhost:33333/_api/model?include=fields&limit=2
```

## Changing the API root or removing the API

The routes for the API are added in your `{project}.core` namespace in the call
to `{project}.core/reload-pages`.  It will look something like this:

```clj
(defn reload-pages
  []
  (pages/add-page-routes
   admin-routes/admin-routes
   'caribou.admin.controllers
   "/_admin"
   admin-core/admin-wrapper)

  (pages/add-page-routes
   api-routes/api-routes
   'caribou.api.controllers
   "/_api"
   api-core/api-wrapper)

  (pages/add-page-routes
   (pages/all-pages)
   (config/draw :controller :namespace)))
```

As you can see, the Admin is loaded first, then the API.  If you want to change
where the API is located, just change the string "/_api" to your desired API
root.  And if you don't want the API enabled at all, simply remove the whole
`pages/add-page-routes` call to add the API routes.

# Deploying Caribou

## Ring Server (Jetty)

Using the built-in ring server is the simplest approach.  Simply run:

```
% lein ring server
```

at the project root and your site will come to life!  To set the port you can
change the options that live in your `project.clj`:

```clj
  :ring {:handler taiga.core/handler
         :init taiga.core/init
         :port 33333})
```

Set the port to any viable port number and restart!

## Tomcat

For Tomcat, the process is simple.

```sh
% lein ring uberwar
```

Once this has completed, drop the resulting jar into your running Tomcat
container's webapps directory.  Voila!

## Immutant (JBoss)

To deploy to Immutant, set up the
[lein-immutant](http://github.com/immutant/lein-immutant) plugin and then in
your Caribou project root simply type:

```sh
% lein immutant deploy
```

There is a generated Immutant configuration file that lives in
`src/immutant/init.clj`.  Any additional Immutant configuration can be done
there.  See the Immutant docs for help:  http://immutant.org/

## Beanstalk

To deploy to Beanstalk running Tomcat, the key is to use the
[lein-beanstalk](http://github.com/weavejester/lein-beanstalk) plugin and have
the right set of values in your `project.clj`.  Here is an example configuration:

```clj
:aws {:access-key "YOUR-AWS-ACCESS-KEY"
      :secret-key "YOUR-AWS-SECRET-KEY"
      :region "us-west-1"
      :beanstalk {:region "us-west-1"
                  :s3-region "us-west-1"
                  :app-name "taiga"
                  :s3-bucket "taiga-prod"
                  :environments [{:name "taiga-production"
                                  :env {"environment" "production"}}]}}
```

Then, run the `lein-beanstalk` command:

```
% lein beanstalk deploy
```

If your Beanstalk configuration with AWS is set up right, you now have a Caribou
project running in the cloud somewhere!  Congratulations.

## Heroku

Caribou by default is already set up to deploy to Heroku.  The main key is to
create a git repo and set the remote heroku target:

```sh
# set up the git repository
% git init
% git add .
% git commit -m "init"

# create the heroku remote and deploy
% heroku apps:create
% git push heroku master
% heroku ps:scale web=1

# open the deployed site
% heroku open
```

For any additional Heroku support, refer to the Heroku docs on this page:  
http://devcenter.heroku.com/articles/clojure


