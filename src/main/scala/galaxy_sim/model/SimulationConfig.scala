package galaxy_sim.model

import physics.dynamics.GravitationLaws.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.PhysicsFormulas.*
import physics.*
import scala.util.Random


object SimulationConfig:
  val windowSize = 90
  /** Time between two requests of the simulation state. */
  val frameRate = 33
  /** Used to maintain stable the speed of the iterations */
  val iterationInterval = 1
  val bounds: Boundary = Boundary(0, lightYear * 2, 0, lightYear * 2)
//  val solarSystemBounds: Boundary = Boundary(0, astronomicUnit * 50, 0, astronomicUnit * 50)
  val blackHoleDistance: Double = astronomicUnit * 5
  val deltaTime: Double = daySec
  val radiusScale = 1e5

  val blackHole: CelestialBody = CelestialBody(mass = solarMass * 80,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 0),
    position = Pair(bounds.rightBound / 2, bounds.bottomBound / 2),
    name = "Black hole",
    radius = 3_000_000 * radiusScale,
    temperature = 1300)

  val body01: CelestialBody = CelestialBody(mass = solarMass,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 50000),
    position = Pair(blackHole.position.x + blackHoleDistance, blackHole.position.y + blackHoleDistance),
    name = "Body01",
    radius = 695_508 * radiusScale,
    temperature = 1100)

  val body02: CelestialBody = CelestialBody(mass = 0.33e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 47900),
    position = Pair(body01.position.x + (astronomicUnit * 0.39), body01.position.y + (astronomicUnit * 0.39)),
    name = "Body02",
    radius = 48000 * radiusScale,
    temperature = 167)

  val body03: CelestialBody = CelestialBody(mass = 4.87e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 35000),
    position = Pair(body01.position.x + (astronomicUnit * 0.72), body01.position.y + (astronomicUnit * 0.72)),
    name = "Body03",
    radius = 12000 * radiusScale,
    temperature = 464)

  val body04: CelestialBody = CelestialBody(mass = earthMass,
    gForceVector = Pair(0, 0),
    speedVector = Pair(0, 29290),
    position = Pair(body01.position.x + (astronomicUnit * 1), body01.position.y + (astronomicUnit * 1)),
    name = "Body04",
    radius = 12000 * radiusScale,
    temperature = 15)

  val body05: CelestialBody = CelestialBody(mass = 0.642e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 24100),
    position = Pair(body01.position.x + (astronomicUnit * 1.52), body01.position.y + (astronomicUnit * 1.52)),
    name = "Body05",
    radius = 6700 * radiusScale,
    temperature = -65)

  val body06: CelestialBody = CelestialBody(mass = 1898e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 13100),
    position = Pair(body01.position.x + (astronomicUnit * 5.2), body01.position.y + (astronomicUnit * 5.2)),
    name = "Body06",
    radius = 143000 * radiusScale,
    temperature = -110)

  val body07: CelestialBody = CelestialBody(mass = 568e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 9700),
    position = Pair(body01.position.x + (astronomicUnit * 9.54), body01.position.y + (astronomicUnit * 9.54)),
    name = "Body07",
    radius = 120000 * radiusScale,
    temperature = -140)

  val body08: CelestialBody = CelestialBody(mass = 86.8e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 6900),
    position = Pair(body01.position.x + (astronomicUnit * 19.2), body01.position.y + (astronomicUnit * 19.2)),
    name = "Body08",
    radius = 51000 * radiusScale,
    temperature = -195)

  val body09: CelestialBody = CelestialBody(mass = 102e24,
    gForceVector = Pair(0,0), speedVector = Pair(0, 5400),
    position = Pair(body01.position.x + (astronomicUnit * 30.06), body01.position.y + (astronomicUnit * 30.06)),
    name = "body09",
    radius = 49000 * radiusScale,
    temperature = -200)

  val body10: CelestialBody = CelestialBody(mass = 102e24,
    gForceVector = Pair(0,0),
    speedVector = Pair(0, 4700),
    position = Pair(body01.position.x + (astronomicUnit * 39), body01.position.y + (astronomicUnit * 39)),
    name = "Body10",
    radius = 230_000 * radiusScale,
    temperature = -225)

  val interstellarCloud2: CelestialBody =
    CelestialBody(mass = 5.972e24,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair( blackHoleDistance - (astronomicUnit/2), blackHoleDistance - (astronomicUnit/2)),
      name = "Interstellar Cloud2",
      radius = 1_000_000,
      temperature = 0)

  def groupOFInterstellarClouds(cloudsNumber: Int): Set[CelestialBody] =
    (0 until cloudsNumber).map(x => {
      val rand = Random
      CelestialBody(mass = 5.972e24 - Math.pow(10, x),
        gForceVector = Pair(0, 0),
        speedVector = Pair(0, 37000 - x * 1000),
        position = Pair(rand.between(bounds.leftBound, bounds.rightBound), rand.between(bounds.topBound, bounds.bottomBound)),
        name = "# " + x.toString,
        radius = 1_000_000 * radiusScale,
        temperature = rand.between(101,1000))
    }).toSet
