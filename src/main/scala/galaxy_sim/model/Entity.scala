package galaxy_sim.model

type Point2D = Pair[Double, Double]
type Vector2D = Pair[Double, Double]
type Mass = Double
type Volume = Double
type Speed = Vector2D
type Position = Point2D

trait Body:
  def name: String
  def mass: Mass
  def volume: Volume
  def speed: Speed
  def position: Position

enum EntityType:
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

case class Entity(override val name: String,
                  override val mass: Mass,
                  override val volume: Volume,
                  override val speed: Speed,
                  override val position: Position) extends Body

object Entity:
  def typeOf(e: Entity): EntityType = ???