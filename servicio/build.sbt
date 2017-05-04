import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.servicio",
      scalaVersion := "2.12.2",
      version      := "1.0"
    )),
    name := "servicio",
    libraryDependencies += scalaTest % Test
  )
