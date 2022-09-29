package galaxy_sim.model

import galaxy_sim.model.CelestialBodyAliases.{Radius, Temperature}
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.CollisionDetection.Colliders.CircleCollider
import physics.collisions.CollisionEngine.RigidBody

object CelestialBodyAliases:
  type Temperature = Double
  type Radius = Double

case class CelestialBody(override val mass: Mass,
                         override val aphelionSpeed: Speed,
                         override val gForceVector: GravityForceVector,
                         override val speedVector: SpeedVector,
                         override val position: Position,
                         name: String,
                         birthTime: Double = 0,
                         radius: Radius,
                         temperature: Temperature) extends RigidBody[CircleCollider]:

  override def collider: CircleCollider = CircleCollider(position, radius)

  def typeOf: CelestialBodyType = EntityIdentifierProlog().checkEntityType(mass, temperature)

enum CelestialBodyType:
  case MassiveStar
  case RedSuperGiant
  case Supernova
  case BlackHole
  case Planet
  case Asteroid
  case InterstellarCloud
