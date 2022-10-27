package physics.dynamics

import physics.*
import physics.dynamics.GravitationLaws.*
import PhysicsFormulas.*

object Utils:
  case class PhysicalEntityImpl(override val mass: Mass = 1000,
                                override val position: Position = Pair(0, 0),
                                override val aphelionSpeed: Speed = 10000,
                                override val speedVector: SpeedVector = Pair(0, 0),
                                override val gForceVector: GravityForceVector = Pair(0, 0)) extends PhysicalEntity

  val earth: PhysicalEntityImpl = PhysicalEntityImpl(5.972e24, Pair(astronomicUnit * 10167, 0), 29290, Pair(0, 29290), Pair(0, 0))
  val sun: PhysicalEntityImpl = PhysicalEntityImpl(2.0e30)
  val semiMayorAxis = 149.60e6
  val earthEccentricity = 0.0167
  val deltaTime: Double = daySec * 1 //one day
