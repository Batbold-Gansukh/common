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

val projectName = "common"
name := projectName
moduleName := projectName
organization := "bb"
version := "0.0.0"
scalaVersion := "2.12.4"
shellPrompt := modulePrompt

//lazy val root = (project in file(".")).enablePlugins(PlayScala)
//enablePlugins(PlayScala)

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
  "io.monix" %% "monix" % "2.3.2",
  "org.typelevel" %% "cats-core" % "1.0.0" withSources(),
  "org.specs2" %% "specs2-core" % "4.0.2" % "test",
  "org.specs2" %% "specs2-junit" % "4.0.2" % "test",
  "junit" % "junit" % "4.12" % "test",
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "com.github.tminglei" %% "slick-pg" % "0.15.4",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.15.4",
  "com.typesafe.play" %% "play" % "2.6.10",
  "com.typesafe.play" %% "play-json-joda" % "2.6.8",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
)

scalacOptions ++= Seq("-Yrangepos")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

resolvers += Resolver.mavenLocal
