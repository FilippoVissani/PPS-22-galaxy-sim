package galaxy_sim.model

import galaxy_sim.model.CelestialBodyAliases.{Radius, Temperature}
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.collisions.rigidbody.RigidBody.CircularEntity
import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}

object CelestialBodyAliases:
  type Temperature = Double
  type Radius = Double

trait CelestialBody[A <: CelestialBodyType] extends CircularEntity:
  def name: String
  def birthTime: Double = 0
  def radius: Radius
  def temperature: Temperature

object CelestialBody:
  def apply[A <: CelestialBodyType](
    mass: Mass,
    aphelionSpeed: Speed,
    gForceVector: GravityForceVector,
    speedVector: SpeedVector,
    position: Position,
    name: String,
    birthTime: Double = 0,
    radius: Radius,
    temperature: Temperature): CelestialBody[A] = CelestialBodyImpl(mass, aphelionSpeed, gForceVector, speedVector, position, name, birthTime, radius, temperature)
  private case class CelestialBodyImpl[A <: CelestialBodyType](
    override val mass: Mass,
    override val aphelionSpeed: Speed, override val gForceVector: GravityForceVector,
    override val speedVector: SpeedVector,
    override val position: Position,
    override val name: String,
    override val birthTime: Double,
    override val radius: Radius,
    override val temperature: Temperature) extends CelestialBody[A]

sealed trait CelestialBodyType
case object MassiveStar extends CelestialBodyType 
case object RedSuperGiant extends CelestialBodyType 
case object Supernova extends CelestialBodyType 
case object BlackHole extends CelestialBodyType 
case object Planet extends CelestialBodyType 
case object Asteroid extends CelestialBodyType 
case object InterstellarCloud extends CelestialBodyType 
