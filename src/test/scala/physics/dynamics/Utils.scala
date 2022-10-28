package physics.dynamics

import physics.*
import physics.dynamics.GravitationLaws.*
import PhysicsFormulas.*

object Utils:
  case class PhysicalEntityImpl(override val mass: Mass = 1000,
                                override val position: Position = Pair(0, 0),
                                override val speedVector: SpeedVector = Pair(0, 0),
                                override val gForceVector: GravityForceVector = Pair(0, 0)) extends PhysicalEntity

//  val earth: PhysicalEntityImpl = PhysicalEntityImpl(5.972e24, Pair(astronomicUnit * 10167, 0), 29290, Pair(0, 29290), Pair(0, 0))
//  val sun: PhysicalEntityImpl = PhysicalEntityImpl(2.0e30)
  val semiMayorAxis = 149.60e6
  val earthEccentricity = 0.0167
  val deltaTime: Double = daySec * 1 //one day
  val blackHole: PhysicalEntityImpl = PhysicalEntityImpl(mass = solarMass * 5, position = Pair(0,0), speedVector = Pair(0, 0), gForceVector = Pair(0, 0))
  val sun: PhysicalEntityImpl = PhysicalEntityImpl(mass = solarMass, position = Pair(blackHole.position.x + astronomicUnit * 5, blackHole.position.y + astronomicUnit * 5), speedVector = Pair(0, 50000), gForceVector = Pair(0, 0))
  val earth: PhysicalEntityImpl = PhysicalEntityImpl(mass = earthMass, position = Pair(sun.position.x + astronomicUnit, sun.position.y + astronomicUnit), speedVector = Pair(0, 29290), gForceVector = Pair(0, 0))
  val moon: PhysicalEntityImpl = PhysicalEntityImpl(mass = earthMass * 0.0123, position = Pair(earth.position.x + 384400, earth.position.y + 384400), speedVector = Pair(0, 3683), gForceVector = Pair(0, 0))
  