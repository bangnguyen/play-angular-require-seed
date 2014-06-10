import play.Project._

name := "play-angular-require-seed"

version := "2.2.2"

libraryDependencies ++= Seq(
  // WebJars infrastructure
  "org.webjars" % "webjars-locator" % "0.13",
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  // WebJars dependencies
  "org.webjars" % "underscorejs" % "1.6.0-1",
  "org.webjars" % "jquery" % "1.11.0-1",
   "org.webjars" % "bootstrap" % "3.1.1-1" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.2.14" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-bootstrap" % "0.11.0",
 // "org.webjars" % "ng-grid" % "2.0.11",
  "org.webjars" % "ng-table" % "0.3.1",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "com.github.tototoshi" %% "scala-csv" % "1.0.0",
  "com.sksamuel.elastic4s" % "elastic4s_2.10" % "1.2.0.0",
  "com.netflix.astyanax" % "astyanax-cassandra" % "1.56.48",
  "com.netflix.astyanax" % "astyanax-core" % "1.56.48",
  "com.netflix.astyanax" % "astyanax-entity-mapper" % "1.56.48",
  "com.netflix.astyanax" % "astyanax-queue" % "1.56.48",
  "com.netflix.astyanax" % "astyanax-thrift" % "1.56.48"
)

playScalaSettings

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/repo"

// This tells Play to optimize this file and its dependencies
requireJs += "main.js"

// The main config file
// See http://requirejs.org/docs/optimization.html#mainConfigFile
requireJsShim := "build.js"

// To completely override the optimization process, use this config option:
//requireNativePath := Some("node r.js -o name=main out=javascript-min/main.min.js")
