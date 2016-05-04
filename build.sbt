import sbt._

//import play.sbt.Colors
//import play.sbt.PlayImport._
//import play.sbt.routes.RoutesKeys._
import sbt.Keys._

val modulePrompt = { state: State =>

  val extracted = Project.extract(state)
  import extracted._

  (name in currentRef get structure.data).map { name =>
    "[" + scala.Console.CYAN + name + scala.Console.RESET + "] $ "
  }.getOrElse("> ")

}

val projectName = "common-utils"
name := projectName
moduleName := projectName
organization := "bb"
version := "0.0.0"
scalaVersion := "2.11.8"
shellPrompt := modulePrompt

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint", // Enable recommended additional warnings.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
  "-Ywarn-numeric-widen" // Warn when numerics are widened.
  //      "-Xlog-implicits" // Show more about implicit error
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"))

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.5.0" withSources(),
  "com.typesafe.play" %% "play-json" % "2.5.3",
  "org.specs2" %% "specs2-core" % "3.8" % "test",
  "org.specs2" %% "specs2-junit" % "3.8" % "test",
  "junit" % "junit" % "4.12" % "test",
  "org.postgresql" % "postgresql" % "9.4.1208",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.github.tminglei" %% "slick-pg" % "0.13.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.13.0"
)

scalacOptions ++= Seq("-Yrangepos")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
