ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-22-galaxy-sim",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    assembly / mainClass := Some("galaxy_sim.Main"),
    assembly / assemblyJarName := "galaxy-sim.jar",
    assembly / test := (Test / test).value,
  )
