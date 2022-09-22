package galaxy_sim.model

import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.Collisions.Colliders.CircleCollider
import physics.collisions.Collisions.{P2d, RigidBody}
import physics.dynamics.PhysicalEntity

trait CelestialBody extends PhysicalEntity with RigidBody[CircleCollider]:
  def name: String
  def radius: Double

object CelestialBody:
  def apply(name: String,
            gForceVector: GravityForceVector, 
            speedVector: SpeedVector, 
            mass: Mass, 
            position: Position, 
            aphelionSpeed: Speed,
            radius: Double): CelestialBody = CelestialBodyImpl(name, gForceVector, speedVector, mass, position, aphelionSpeed, radius)
  def typeOf(e: CelestialBody): CelestialBodyType = ???

  private case class CelestialBodyImpl(override val name: String,
                                       override val gForceVector: GravityForceVector,
                                       override val speedVector: SpeedVector,
                                       override val mass: Mass,
                                       override val position: Position,
                                       override val aphelionSpeed: Speed,
                                       override val radius: Double) extends CelestialBody:

    override val collider: CircleCollider = CircleCollider(P2d(position.x, position.y), radius)

enum CelestialBodyType:
  case MassiveStar
  case RedSuperGiant
  case Supernova
  case BlackHole
  case LowMassStar
  case RedGiant
  case PlanetaryNebula
  case WhiteDwarf
  case BlackDwarf
  case Planet
  case Asteroid
  case InterstellarCloud