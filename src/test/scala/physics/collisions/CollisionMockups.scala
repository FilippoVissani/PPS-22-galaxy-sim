package physics.collisions

import physics.collisions.impact.Impact
import physics.collisions.intersection.Intersection
import physics.collisions.instances.IntersectionInstances
import physics.{Pair, Position}
import physics.rigidbody.CollisionBoxes.*
import physics.collisions.instances.IntersectionInstances.given

object CollisionMockups:
  trait SphericalEntity:
    val origin: Position
    val radius: Double
    def collisionBox: CircleCollisionBox = CircleCollisionBox(origin, radius)

  case class Star(origin: Position, radius: Double, mass: Double) extends SphericalEntity

  given StarCollision: Intersection[Star] =
    Intersection.from(
      (s, n) => IntersectionInstances.CircleToCircleCollision.intersects(s.collisionBox, n.collisionBox)
    )

  given StarImpact: Impact[Star] =
    Impact.from(
      (s1, s2) => s1.copy(mass = s1.mass + s2.mass / 1.5)
    )