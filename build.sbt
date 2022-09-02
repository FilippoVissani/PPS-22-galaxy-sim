ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name := "PPS-22-galaxy-sim",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    assembly / mainClass := Some("it.unibo.test.Main"),
    assembly / assemblyJarName := "galaxy-sim.jar",
    assembly / test := (Test / test).value,
  )

coverageFailOnMinimum := true
coverageMinimumStmtTotal := 90
coverageMinimumBranchTotal := 90
coverageMinimumStmtPerPackage := 90
coverageMinimumBranchPerPackage := 85
coverageMinimumStmtPerFile := 85
coverageMinimumBranchPerFile := 80