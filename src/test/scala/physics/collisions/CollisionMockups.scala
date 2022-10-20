package physics.collisions

import physics.{Pair, Position}
import physics.ref.CollisionBoxes.*
import physics.ref.{Collision, Collisions}
import physics.ref.Collisions.given

object CollisionMockups:
  trait SphericalEntity:
    val origin: Position
    val radius: Double
    def collisionBox: CircleCollisionBox = CircleCollisionBox(origin, radius)
    
  case class Nebula(origin: Position, radius: Double, mass: Double) extends SphericalEntity
  case class Star(origin: Position, radius: Double, mass: Double) extends SphericalEntity

  given StarNebulaCollision: Collision[Star, Nebula] =
    Collision.from[Star, Nebula](
      (s, n) => Collisions.CircleToCircleCollision.collisionFun(s.collisionBox, n.collisionBox)
    )((s, n) => s.copy(mass = s.mass + n.mass / 1.5))