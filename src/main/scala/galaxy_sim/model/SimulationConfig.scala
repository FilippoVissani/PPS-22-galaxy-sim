package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector
import physics.dynamics.GravitationLaws.*

object SimulationConfig:
//  val bounds: Boundary = Boundary(0, astronomicUnit * 4, 0, astronomicUnit * 4)
  val bounds: Boundary = Boundary(0, astronomicUnit * 3, 0, astronomicUnit * 3)

  val moon: CelestialBody =
    CelestialBody(mass = earthMass * 1.2 / 100,
    aphelionSpeed = 3683,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 3683),
    position = Pair(astronomicUnit * 1.0167 + 384400, 0),
    name = "Moon",
    radius = 5,
    temperature = 110)

  val sun: CelestialBody =
    CelestialBody(mass = solarMass,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 50000),
      position = Pair(0, 0),
      name = "Sun",
      radius = 30,
      temperature = 1100)

  val earth: CelestialBody =
    CelestialBody(mass = earthMass,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(astronomicUnit * 1.0167, 0),
      name = "Earth",
      radius = 10,
      temperature = 150)

  val blackHole: CelestialBody =
    CelestialBody(mass = solarMass * 4,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 0),
      position = Pair(0, 0),
      name = "Sagittarius A*",
      radius = 30,
      temperature = 10)

/*
val sun: CelestialBody =
  CelestialBody(mass = 2.0e30,
    aphelionSpeed = 60000,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 60000),
    position = Pair(astronomicUnit, 0), //Pair(26_000 * lightYear, 0)
    name = "Sun",
    radius = 20,
    temperature = 1100)

val earth: CelestialBody =
  CelestialBody(mass = 5.972e24,
    aphelionSpeed = 29290,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 29290),
    position = Pair(astronomicUnit * 1.0167, 0), //Pair(lightYear * astronomicUnit * 1.0167, 0)
    name = "Earth",
    radius = 10,
    temperature = 0)

val blackHole: CelestialBody =
  CelestialBody(mass = 4_154_000,
    aphelionSpeed = 0,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 0),
    position = Pair(0, 0),
    name = "Black Hole",
    radius = 20,
    temperature = 10
  )*/
