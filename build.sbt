ThisBuild / organization := "com.example"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

val scalatestVersion = "3.2.19"

lazy val core = (project in file("core"))
    .settings(
        name := "core",
        libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % scalatestVersion % "test",
        libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    )

// lazy val app = (project in file("app"))
//   .settings(
    // other settings
//   ).dependsOn(core)