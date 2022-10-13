package galaxy_sim.model

import galaxy_sim.model.CelestialBodyAliases.{Radius, Temperature}
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.collisions.rigidbody.RigidBody.CircularEntity
import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}

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
                         temperature: Temperature) extends CircularEntity:
  def typeOf: CelestialBodyType = EntityIdentifierProlog().checkEntityType(mass, temperature)

sealed trait CelestialBodyType
case object MassiveStar extends CelestialBodyType
case object RedSuperGiant extends CelestialBodyType
case object Supernova extends CelestialBodyType
case object BlackHole extends CelestialBodyType
case object Planet extends CelestialBodyType
case object Asteroid extends CelestialBodyType
case object InterstellarCloud extends CelestialBodyType
