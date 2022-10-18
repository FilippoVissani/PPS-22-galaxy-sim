package physics.collisions

import physics.{Pair, Position}
import physics.collisions.CollisionDetection.CollisionBoxes.CircleCollisionBox
import physics.collisions.CollisionDetection.CollisionChecker
import physics.collisions.CollisionDetection.CollisionCheckers.given
import physics.collisions.CollisionSolving.CollisionSolver

object CollisionMockups:
  trait SphericalEntity:
    val origin: Position
    val radius: Double
    def collisionBox: CircleCollisionBox = CircleCollisionBox(origin, radius)
    
  case class Nebula(origin: Position, radius: Double, mass: Double) extends SphericalEntity
  case class Star(origin: Position, radius: Double, mass: Double) extends SphericalEntity
  case class Planet(origin: Position, radius: Double, mass: Double) extends SphericalEntity
  case class System(origin: Position, star: Star, planets: List[Planet])

  given StarToNebulaChecker: CollisionChecker[Star, Nebula] with
    override def check(a: Star, b: Nebula): Boolean =
      CircleToCircleChecker.check(a.collisionBox, b.collisionBox)
      
  given PlanetToPlanetChecker: CollisionChecker[Planet, Planet] with
    override def check(a: Planet, b: Planet): Boolean =
      CircleToCircleChecker.check(a.collisionBox, b.collisionBox)
      
  given StarToNebulaSolver: CollisionSolver[Star, Nebula, System] with
    override def solve(a: Star, b: Nebula): System =
      System(a.origin, a.copy(mass = a.mass/2), List(Planet(a.origin + Pair(1, 1), 0.1, a.mass/10), Planet(a.origin + Pair(0.5, 1.5), 0.01, a.mass/100)))
      
  given PlanetToPlanetSolver: CollisionSolver[Planet, Planet, Planet] with
    override def solve(a: Planet, b: Planet): Planet =
      val bigger = if a.mass >= b.mass then a else b
      Planet(bigger.origin, bigger.radius * 1.1, bigger.mass * 1.5)