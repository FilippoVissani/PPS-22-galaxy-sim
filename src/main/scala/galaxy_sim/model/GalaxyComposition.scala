package galaxy_sim.model
 
import galaxy_sim.model.BodyStructureAliases.{Radius, Temperature}
import physics.collisions.rigidbody.RigidBody.CircularEntity
import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
 
trait GenericBody[A]:
  def concreteBody: A
 
  def map[B](f: A => B): GenericBody[B]
 
  def flatMap[B](f: A => GenericBody[B]): GenericBody[B]
 
object GenericBody:
  def apply[A](concreteBody: A): GenericBody[A] = GenericBodyImpl[A](concreteBody)
 
  private case class GenericBodyImpl[A](override val concreteBody: A) extends GenericBody[A]:
    override def map[B](f: A => B): GenericBody[B] = GenericBody[B](f(concreteBody))
 
    override def flatMap[B](f: A => GenericBody[B]): GenericBody[B] = f(concreteBody)
 
object BodyStructureAliases:
  type Temperature = Double
  type Radius = Double

case class BodyStructure(
  mass: Mass,
  aphelionSpeed: Speed,
  gForceVector: GravityForceVector,
  speedVector: SpeedVector,
  position: Position,
  name: String,
  birthTime: Double = 0,
  radius: Radius,
  temperature: Temperature,
  ) extends CircularEntity
 
case class MassiveStar(bodyStructure: BodyStructure):
  export bodyStructure.*
case class RedSuperGiant(bodyStructure: BodyStructure):
  export bodyStructure.*
case class Supernova(bodyStructure: BodyStructure):
  export bodyStructure.*
case class BlackHole(bodyStructure: BodyStructure):
  export bodyStructure.*
case class Planet(bodyStructure: BodyStructure):
  export bodyStructure.*
case class Asteroid(bodyStructure: BodyStructure):
  export bodyStructure.*
case class InterstellarCloud(bodyStructure: BodyStructure):
  export bodyStructure.*

class GalaxyStructure(
  massiveStars: Set[GenericBody[MassiveStar]] = Set(),
  redSuperGiants: Set[GenericBody[RedSuperGiant]] = Set(),
  supernovas: Set[GenericBody[Supernova]] = Set(),
  blackHoles: Set[GenericBody[BlackHole]] = Set(),
  planets: Set[GenericBody[Planet]] = Set(),
  asteroids: Set[GenericBody[Asteroid]] = Set(),
  interstellarClouds: Set[GenericBody[InterstellarCloud]] = Set(),
)
