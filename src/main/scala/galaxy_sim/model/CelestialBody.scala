package galaxy_sim.model

import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.Collisions.Colliders.CircleCollider
import physics.collisions.Collisions.{P2d, RigidBody}
import physics.dynamics.PhysicalEntity

trait CelestialBody extends RigidBody[CircleCollider]:
  def name: String
  def radius: Double
  def birthTime: Double
  def body: PhysicalEntity

object CelestialBody:
  def apply(name: String,
            radius: Double,
            birthTime: Double,
            physicalEntity: PhysicalEntity): CelestialBody =
    CelestialBodyImpl(name, radius, birthTime, physicalEntity)

  def typeOf(b: CelestialBody): CelestialBodyType = ???

  def updateName(b: CelestialBody, name: String): CelestialBody =
    CelestialBody(name, b.radius, b.birthTime, b.body)

  def updateRadius(b: CelestialBody)(f: Double => Double): CelestialBody =
    CelestialBody(b.name, f(b.radius), b.birthTime, b.body)

  def updateBirthTime(b: CelestialBody)(f: Double => Double): CelestialBody =
    CelestialBody(b.name, b.radius, f(b.birthTime), b.body)

  def updatePhysicalEntity(b: CelestialBody)(f: PhysicalEntity => PhysicalEntity): CelestialBody =
    CelestialBody(b.name, b.radius, b.birthTime, f(b.body))

  private case class CelestialBodyImpl(override val name: String,
                                       override val radius: Double,
                                       override val birthTime: Double,
                                       override val body: PhysicalEntity) extends CelestialBody:
    override val collider: CircleCollider = CircleCollider(P2d(body.position.x, body.position.y), radius)

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