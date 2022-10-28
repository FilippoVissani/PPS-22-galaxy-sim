package galaxy_sim.model

import physics.dynamics.GravitationLaws.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.PhysicsFormulas.*
import physics.*

object SimulationConfig:
  val windowSize = 90
  /** Time between two requests of the simulation state. */
  val frameRate = 33
//  val bounds: Boundary = Boundary(0, astronomicUnit * 6, 0, astronomicUnit * 6)
  val bounds: Boundary = Boundary(0, lightYear, 0, lightYear)
  val solarSystemBounds: Boundary = Boundary(0, astronomicUnit * 50, 0, astronomicUnit * 50)
  val blackHoleDistance: Double = astronomicUnit * 4
  val deltaTime: Double = daySec
  val radiusScale = 1e4

  val blackHole: CelestialBody =
    CelestialBody(mass = solarMass * 20,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 0),
      position = Pair(bounds.rightBound / 2, bounds.bottomBound / 2),
      name = "Black hole",
      radius = 30,
      temperature = 10)

  val sun: CelestialBody = CelestialBody(mass = solarMass, gForceVector = Pair(0, 0), speedVector = Pair(0, 50000), position = Pair(solarSystemBounds.rightBound / 2 , solarSystemBounds.bottomBound / 2), name = "Sun", radius = 695_508 * radiusScale, temperature = 1100)

//  val earth2: CelestialBody = CelestialBody(mass = earthMass, gForceVector = Pair(0, 0), speedVector = Pair(-1, 29290), position = Pair(sun.position.x - astronomicUnit * 1.0167, sun.position.y), name = "Earth2", radius = 695_508 * radiusScale, temperature = 150)

  val mercury: CelestialBody = CelestialBody(mass = 0.33e24, gForceVector = Pair(0,0), speedVector = Pair(0, 47900), position = Pair(sun.position.x + (astronomicUnit * 0.39), sun.position.y + (astronomicUnit * 0.39)), name = "Mercury", radius = 48 * radiusScale, temperature = 167)

  val venus: CelestialBody = CelestialBody(mass = 4.87e24, gForceVector = Pair(0,0), speedVector = Pair(0, 35000), position = Pair(sun.position.x + (astronomicUnit * 0.72), sun.position.y + (astronomicUnit * 0.72)), name = "Venus", radius = 12 * radiusScale, temperature = 464)

  val earth: CelestialBody = CelestialBody(mass = earthMass, gForceVector = Pair(0, 0), speedVector = Pair(0, 29290), position = Pair(sun.position.x + (astronomicUnit * 1), sun.position.y + (astronomicUnit * 1)), name = "Earth", radius = 12 * radiusScale, temperature = 15)

  val mars: CelestialBody = CelestialBody(mass = 0.642e24, gForceVector = Pair(0,0), speedVector = Pair(0, 24100), position = Pair(sun.position.x + (astronomicUnit * 1.52), sun.position.y + (astronomicUnit * 1.52)), name = "Mars", radius = 6.7 * radiusScale, temperature = -65)

  val jupiter: CelestialBody = CelestialBody(mass = 1898e24, gForceVector = Pair(0,0), speedVector = Pair(0, 13100), position = Pair(sun.position.x + (astronomicUnit * 5.2), sun.position.y + (astronomicUnit * 5.2)), name = "Jupiter", radius = 143 * radiusScale, temperature = -110)

  val saturn: CelestialBody = CelestialBody(mass = 568e24, gForceVector = Pair(0,0), speedVector = Pair(0, 9700), position = Pair(sun.position.x + (astronomicUnit * 9.54), sun.position.y + (astronomicUnit * 9.54)), name = "Saturn", radius = 120 * radiusScale, temperature = -140)

  val uranus: CelestialBody = CelestialBody(mass = 86.8e24, gForceVector = Pair(0,0), speedVector = Pair(0, 6900), position = Pair(sun.position.x + (astronomicUnit * 19.2), sun.position.y + (astronomicUnit * 19.2)), name = "Uranus", radius = 51 * radiusScale, temperature = -195)

  val neptune: CelestialBody = CelestialBody(mass = 102e24, gForceVector = Pair(0,0), speedVector = Pair(0, 5400), position = Pair(sun.position.x + (astronomicUnit * 30.06), sun.position.y + (astronomicUnit * 30.06)), name = "Neptune", radius = 49 * radiusScale, temperature = -200)

  val pluto: CelestialBody = CelestialBody(mass = 102e24, gForceVector = Pair(0,0), speedVector = Pair(0, 4700), position = Pair(sun.position.x + (astronomicUnit * 39), sun.position.y + (astronomicUnit * 39)), name = "Pluto", radius = 2.3 * radiusScale, temperature = -225)


  val interstellarCloud2: CelestialBody =
    CelestialBody(mass = 5.972e24,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair( blackHoleDistance - (astronomicUnit/2), blackHoleDistance - (astronomicUnit/2)),
      name = "Interstellar Cloud2",
      radius = 10,
      temperature = 0)

  def groupOFInterstellarClouds(cloudsNumber: Int): Set[CelestialBody] =
    (0 until cloudsNumber).map(x => {
      CelestialBody(mass = 5.972e24 - Math.pow(10, x),
        gForceVector = Pair(0, 0),
        speedVector = Pair(0, 37000 - x * 1000),
        position = Pair(blackHoleDistance - (astronomicUnit - Math.pow(100, x)), blackHoleDistance - (astronomicUnit - Math.pow(100, x))),
        name = "Interstellar Cloud " + x.toString,
        radius = 10,
        temperature = 0)
    }).toSet
