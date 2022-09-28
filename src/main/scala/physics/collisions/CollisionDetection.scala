package physics.collisions

import physics.Pair
import physics.collisions.CollisionDetection.Colliders.{CircleCollider, Collider}
import physics.dynamics.PhysicalEntity

object CollisionDetection:
  type Position = Pair[Double, Double]
  case class P2d(x: Double, y: Double)

  object Colliders:
    trait Collider
    case class CircleCollider(origin: Position, radius: Double) extends Collider
    case class RectangleCollider(topLeft: Position, height: Double, width: Double) extends Collider

  import Colliders.*

  trait CollisionDetector[A <: Collider, B<: Collider]:
    def detect(c1: A, c2: B): Boolean

  object CollisionDetectors:
    given CircleToCircleDetector: CollisionDetector[CircleCollider, CircleCollider] with
      override def detect(c1: CircleCollider, c2: CircleCollider): Boolean =
        val dx = c1.origin.x - c2.origin.x
        val dy = c1.origin.y - c2.origin.y
        val dist = Math.sqrt(dx*dx + dy*dy)
        dist < c1.radius + c2.radius

    given RectangleToRectangleDetector: CollisionDetector[RectangleCollider, RectangleCollider] with
      override def detect(c1: RectangleCollider, c2: RectangleCollider): Boolean =
        ! (c1.topLeft.x > c2.topLeft.x + c2.width ||
          c1.topLeft.x + c1.width < c2.topLeft.x ||
          c1.topLeft.y > c2.topLeft.y + c2.height ||
          c1.height + c1.topLeft.y < c2.topLeft.y)
        
