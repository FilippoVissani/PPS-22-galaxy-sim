ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-22-galaxy-sim",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    libraryDependencies += "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1",
    assembly / mainClass := Some("galaxy_sim.Main"),
    assembly / assemblyJarName := "galaxy-sim.jar",
    assembly / test := (Test / test).value,
  )
