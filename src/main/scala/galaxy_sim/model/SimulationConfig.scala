package galaxy_sim.model

import physics.dynamics.GravitationLaws.*
import physics.{Pair, SpeedVector}
import physics.dynamics.PhysicalEntity
import physics.dynamics.PhysicsFormulas.*

object SimulationConfig:
  /** Time between two requests of the simulation state. */
  val frameRate = 33
  //  val bounds: Boundary = Boundary(0, astronomicUnit * 7, 0, astronomicUnit * 7)
  val bounds: Boundary = Boundary(0, lightYear, 0, lightYear)
  val blackHoleDistance = astronomicUnit * 3
  val deltaTime = daySec //40 //e-10
  val radiusScale = 1e4

  val sun2: CelestialBody =
    CelestialBody(mass = solarMass,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 50000),
      position = Pair(bounds.rightBound / 2, bounds.bottomBound / 2),
      name = "Sun",
      radius = 695_508 * radiusScale,
      temperature = 1100)

  val blackHole: CelestialBody =
    CelestialBody(mass = solarMass * 15,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 0),
      position = Pair(bounds.rightBound / 2, bounds.bottomBound / 2),
      name = "Black hole",
      radius = 30,
      temperature = 10)

  val sun: CelestialBody =
    CelestialBody(mass = solarMass,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 50000),
      position = Pair(blackHole.position.x + blackHoleDistance, blackHole.position.y + blackHoleDistance),
      name = "Sun",
      radius = 695_508 * radiusScale,
      temperature = 1100)
  //      position = Pair(blackHoleDistance, 0),

  val earth: CelestialBody =
    CelestialBody(mass = earthMass,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(1, 29290),
      position = Pair(sun2.position.x + astronomicUnit * 1.0167, sun2.position.y),
      name = "Earth",
      radius = 695_508 * radiusScale,
      temperature = 150)

  val moon: CelestialBody =
    CelestialBody(mass = earthMass * 0.0123,
      aphelionSpeed = 3683,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 3683),
      position = Pair(earth.position.x + 384400, earth.position.y + 0),
      name = "Moon",
      radius = 1_737.5 * radiusScale,
      temperature = 110)
  //    position = Pair(blackHoleDistance + astronomicUnit * 1.0167 + 384400, 0),

  val earth2: CelestialBody =
    CelestialBody(mass = earthMass,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(-1, 29290),
      position = Pair(sun2.position.x - astronomicUnit * 1.0167, sun2.position.y),
      name = "Earth2",
      radius = 695_508 * radiusScale,
      temperature = 150)
  //      position = Pair(blackHoleDistance + astronomicUnit * 1.0167, 0),

  val interstellarCloud2: CelestialBody =
    CelestialBody(mass = 5.972e24,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair( blackHole.position.x  -(astronomicUnit/2), blackHole.position.y  -(astronomicUnit/2)),
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
