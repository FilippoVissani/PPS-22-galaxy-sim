package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector
import physics.dynamics.GravitationLaws.*

object SimulationConfig:
  val bounds: Boundary = Boundary(0, lightYear * 3, 0, lightYear * 3)
  val blackHoleDistance = lightYear
  val deltaTime = 1000d

  val moon: CelestialBody =
    CelestialBody(mass = earthMass * 0.0123,
    aphelionSpeed = 3683,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 10),
    position = Pair(blackHoleDistance + astronomicUnit * 1.0167 + 384400, 0),
    name = "Moon",
    radius = 5,
    temperature = 110)

  val sun: CelestialBody =
    CelestialBody(mass = solarMass,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 50000),
      position = Pair(blackHoleDistance, 0),
      name = "Sun",
      radius = 30,
      temperature = 1100)

  val earth: CelestialBody =
    CelestialBody(mass = earthMass,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(blackHoleDistance + astronomicUnit * 1.0167, 0),
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

  val interstellarCloud2: CelestialBody =
    CelestialBody(mass = 5.972e24,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(-(astronomicUnit/2), 0),
      name = "Interstellar Cloud2",
      radius = 10,
      temperature = 0)

  def groupOFInterstellarClouds(cloudsNumber: Int): Set[CelestialBody] =
    (0 until cloudsNumber).map(x => {
      CelestialBody(mass = 5.972e24 - Math.pow(10, x),
        aphelionSpeed = 29290 - x * 1000,
        gForceVector = Pair(0, 0),
        speedVector = Pair(0, 29290 - x * 1000),
        position = Pair(-(astronomicUnit - Math.pow(100, x)), 0),
        name = "Interstellar CloudX",
        radius = 10,
        temperature = 0)
    }).toSet
