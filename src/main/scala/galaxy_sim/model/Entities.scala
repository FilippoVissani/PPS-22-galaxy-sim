package galaxy_sim.model

import galaxy_sim.model.StarType.*

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

object Body:

  def apply(name: String,
            mass: Mass,
            volume: Volume,
            speed: Speed,
            position: Position): Body =
    BodyImpl(name, mass, volume, speed, position)

  private case class BodyImpl(override val name: String,
                              override val mass: Mass,
                              override val volume: Volume,
                              override val speed: Speed,
                              override val position: Position) extends Body

trait Entity:
  def body: Body

enum StarType:
  case MassiveStar, RedSuperGiant, Supernova, BlackHole, LowMassStar, RedGiant, PlanetaryNebula, WhiteDwarf, BlackDwarf

trait Star extends Entity:
  def starType: StarType
  def oneYearOlder: Star

object Star:
  def apply(body: Body, starType: StarType): Star = StarImpl(body, starType)

  private case class StarImpl(override val body: Body, override val starType: StarType) extends Star:

    override def oneYearOlder: Star = ???

    private def nextType: StarType = starType match
      case MassiveStar => RedSuperGiant
      case RedSuperGiant => Supernova
      case Supernova => BlackHole
      case LowMassStar => RedGiant
      case RedGiant => PlanetaryNebula
      case PlanetaryNebula => WhiteDwarf
      case WhiteDwarf => BlackDwarf
      case _ => starType



trait Planet extends Entity

object Planet:
  def apply(body: Body): Planet = PlanetImpl(body)

  private case class PlanetImpl(override val body: Body) extends Planet

trait Asteroid extends Entity

object Asteroid:
  def apply(body: Body): Asteroid = AsteroidImpl(body)

  private case class AsteroidImpl(override val body: Body) extends Asteroid

trait InterstellarCloud extends Entity

object InterstellarCloud:
  def apply(body: Body): InterstellarCloud = InterstellarCloudImpl(body)

  private case class InterstellarCloudImpl(override val body: Body) extends InterstellarCloud
