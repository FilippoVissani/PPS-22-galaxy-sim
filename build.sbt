ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val AkkaVersion = "2.6.20"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-22-galaxy-sim",
    libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.jfree" % "jfreechart" % "1.5.3"
    ),
    assembly / mainClass := Some("galaxy_sim.Main"),
    assembly / assemblyJarName := "galaxy-sim.jar",
    assembly / test := (Test / test).value,
  )
