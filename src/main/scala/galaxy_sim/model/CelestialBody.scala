package galaxy_sim.model

import galaxy_sim.model.BodyAliases.{Radius, Temperature}
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.Collisions.Colliders.CircleCollider
import physics.collisions.Collisions.{P2d, RigidBody}
import physics.dynamics.PhysicalEntity

object BodyAliases:
  type Radius = Double
  type Temperature = Double

trait Body:
  val physicalEntity: PhysicalEntity
  export physicalEntity.*
  def radius: Radius
  def temperature: Temperature

object Body:
  def apply(physicalEntity: PhysicalEntity, radius: Radius, temperature: Temperature): Body =
    BodyImpl(physicalEntity, radius, temperature)

  private case class BodyImpl(override val physicalEntity: PhysicalEntity,
                              override val radius: Radius,
                              override val temperature: Temperature) extends Body

object BodyOperations:
  extension (b: Body)
    def updatePhysicalEntity(f: PhysicalEntity => PhysicalEntity): Body =
      Body(f(b.physicalEntity), b.radius, b.temperature)
      
    def updateRadius(f: Radius => Radius): Body =
      Body(b.physicalEntity, f(b.radius), b.temperature)

    def updateTemperature(f: Temperature => Temperature): Body =
      Body(b.physicalEntity, b.radius, f(b.temperature))

trait CelestialBody extends RigidBody[CircleCollider]:
  def name: String
  def birthTime: Double
  val body: Body
  export body.*

object CelestialBody:
  def apply(name: String,
            birthTime: Double = 0,
            body: Body): CelestialBody =
    CelestialBodyImpl(name, birthTime, body)

  private case class CelestialBodyImpl(override val name: String,
                                       override val birthTime: Double,
                                       override val body: Body) extends CelestialBody :
    override val collider: CircleCollider = CircleCollider(P2d(body.position.x, body.position.y), radius)

object CelestialBodyOperations:
  extension (c: CelestialBody)
    def typeOf: CelestialBodyType = EntityIdentifierProlog().checkEntityType(c.mass, c.temperature)

    def updateName(name: String): CelestialBody =
      CelestialBody(name, c.birthTime, c.body)

    def updateBirthTime(f: Double => Double): CelestialBody =
      CelestialBody(c.name, f(c.birthTime), c.body)
      
    def updateBody(f: Body => Body): CelestialBody =
      CelestialBody(c.name, c.birthTime, f(c.body))

enum CelestialBodyType:
  case MassiveStar
  case RedSuperGiant
  case Supernova
  case BlackHole
  case Planet
  case Asteroid
  case InterstellarCloud