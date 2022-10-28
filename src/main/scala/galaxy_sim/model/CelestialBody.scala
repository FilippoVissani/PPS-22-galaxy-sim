package galaxy_sim.model

import galaxy_sim.model.CelestialBodyAliases.{Radius, Temperature}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.Lifecycle.bodyType
import galaxy_sim.prolog.EntityIdentifier
import physics.*
import physics.rigidbody.RigidBody.CircularEntity
import galaxy_sim.model.SimulationConfig.*
import physics.dynamics.GravitationLaws.*


import scala.util.Random

/** Defines type aliases used in CelestialBody. */
object CelestialBodyAliases:
  type Temperature = Double
  type Radius = Double

/** Definition of celestial body.
 *  
 *  @param mass
 *  @param aphelionSpeed
 *  @param gForceVector
 *  @param speedVector
 *  @param position
 *  @param name
 *  @param radius
 *  @param temperature
 */
case class CelestialBody(
  override val mass: Mass,
  override val aphelionSpeed: Speed,
  override val gForceVector: GravityForceVector,
  override val speedVector: SpeedVector,
  override val position: Position,
  name: String,
  radius: Radius,
  temperature: Temperature,
  ) extends CircularEntity

object CelestialBody:
  def generateSystem(b: CelestialBody, maxRand: Int = 10): List[CelestialBody] =
    def randomNum: Int = Random.between(0, maxRand)
    def fun(b: CelestialBody): List[CelestialBody] =
      val toDivide = randomNum
      (0 to toDivide).map(_ =>
        b.copy(
          mass = b.mass / toDivide,
          position = b.position + Pair(randomNum, randomNum)
        )
      ).toList :+ b

    EntityIdentifier.checkEntityType(b.mass, b.temperature) match
      case Supernova => fun(b)
      case MassiveStar => fun(b)
      case _ => List(b)

/** Defines possible types for a celestial body. */
enum CelestialBodyType:
  case MassiveStar
  case RedSuperGiant
  case Supernova
  case BlackHole
  case Planet
  case Asteroid
  case InterstellarCloud
