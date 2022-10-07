package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector
import physics.dynamics.GravitationLaws.astronomicUnit

object SimulationConfig:
  val bounds: Boundary = Boundary(0, astronomicUnit * 3, 0, astronomicUnit * 3)

  val deltaTime = 1000d
  
  val blackHole: CelestialBody =
    CelestialBody(mass = 2.0e30,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 0),
      position = Pair(0, 0),
      name = "Black Hole",
      radius = 20,
      temperature = 1100)

  val interstellarCloud: CelestialBody =
    CelestialBody(mass = 5.972e24,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(astronomicUnit, 0),
      name = "Interstellar Cloud",
      radius = 10,
      temperature = 0)

  val interstellarCloud2: CelestialBody =
    CelestialBody(mass = 5.972e24,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(-(astronomicUnit/2), 0),
      name = "Interstellar Cloud2",
      radius = 10,
      temperature = 0)

  val groupOFInterstellarClouds: Set[CelestialBody] =
    (0 until 100).map(x => {
      CelestialBody(mass = 5.972e24 - Math.pow(10, x),
        aphelionSpeed = 29290 - x * 1000,
        gForceVector = Pair(0, 0),
        speedVector = Pair(0, 29290 - x * 1000),
        position = Pair(-(astronomicUnit - Math.pow(100, x)), 0),
        name = "Interstellar CloudX",
        radius = 10,
        temperature = 0)
    }).toSet
