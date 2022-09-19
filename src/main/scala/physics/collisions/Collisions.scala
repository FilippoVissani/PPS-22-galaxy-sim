package physics.collisions

import physics.collisions.Collisions.Colliders.Collider

object Collisions:
  case class P2d(x: Double, y: Double)
  trait RigidBody[T <: Collider]:
    def collider: T 

  object Colliders:
    trait Collider
    case class CircleCollider(origin: P2d, radius: Double) extends Collider
    case class RectangleCollider(topLeft: P2d, height: Double, width: Double) extends Collider

  import Colliders.*

  object CollisionManager:
    import CollisionDetectors.given
    def detect[A <: Collider, B <: Collider](c1: RigidBody[A], c2: RigidBody[B])(using col: CollisionDetector[A, B]): Boolean =
      col.detect(c1.collider, c2.collider)


  trait CollisionDetector[A <: Collider, B <: Collider]:
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