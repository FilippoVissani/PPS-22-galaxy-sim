package galaxy_sim.model

import galaxy_sim.model.CelestialBodyAliases.{Radius, Temperature}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.*
import physics.rigidbody.RigidBody.CircularEntity
import galaxy_sim.model.SimulationConfig.*
import physics.dynamics.GravitationLaws.astronomicUnit

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
 *  @param birthTime
 *  @param radius
 *  @param temperature
 */
case class CelestialBody(override val mass: Mass,
                         override val aphelionSpeed: Speed,
                         override val gForceVector: GravityForceVector,
                         override val speedVector: SpeedVector,
                         override val position: Position,
                         name: String,
                         birthTime: Double = 0,
                         radius: Radius,
                         temperature: Temperature,
                         ) extends CircularEntity

/** Defines possible types for a celestial body. */
enum CelestialBodyType:
  case MassiveStar
  case RedSuperGiant
  case Supernova
  case BlackHole
  case Planet
  case Asteroid
  case InterstellarCloud
