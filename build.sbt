ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

val scalatestVersion = "3.2.19"

lazy val core = (project in file("core"))
    .settings(
        name := "core",
        libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % scalatestVersion % "test",
        libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test",
        libraryDependencies += "dev.zio" %% "zio-json" % "0.7.1"
    )


lazy val app = (project in file("app"))
    .settings(
        name := "app",
        libraryDependencies += "dev.zio" %% "zio" % "2.0.19",
        libraryDependencies += "dev.zio" %% "zio-json" % "0.7.1",
        libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.19",
        libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC9",
        libraryDependencies += "io.circe" %% "circe-core" % "0.14.1",
        libraryDependencies += "io.circe" %% "circe-generic" % "0.14.1",
        libraryDependencies += "io.circe" %% "circe-parser" % "0.14.1",
    ).dependsOn(core)

lazy val root = (project in file("."))
    .aggregate(core, app)
    .settings(
        publish := {},
        publishLocal := {}
    )