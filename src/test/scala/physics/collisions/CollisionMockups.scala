package physics.collisions

import physics.collisions.Collision.Collision
import physics.collisions.instances.CollisionInstances
import physics.{Pair, Position}
import physics.rigidbody.CollisionBoxes.*
import physics.collisions.instances.CollisionInstances.given

object CollisionMockups:
  trait SphericalEntity:
    val origin: Position
    val radius: Double
    def collisionBox: CircleCollisionBox = CircleCollisionBox(origin, radius)
    
  case class Nebula(origin: Position, radius: Double, mass: Double) extends SphericalEntity
  case class Star(origin: Position, radius: Double, mass: Double) extends SphericalEntity

  given StarNebulaCollision: Collision[Star, Nebula] =
    Collision.from[Star, Nebula](
      (s, n) => CollisionInstances.CircleToCircleCollision.collisionFun(s.collisionBox, n.collisionBox)
    )((s, n) => s.copy(mass = s.mass + n.mass / 1.5))