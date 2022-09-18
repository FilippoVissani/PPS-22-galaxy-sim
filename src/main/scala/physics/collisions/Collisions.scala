package physics.collisions

object Collisions:
  case class P2d(x: Float, y: Float)

  object Colliders:
    trait Collider
    case class CircleCollider(origin: P2d, radius: Float) extends Collider

  import Colliders.*
  type PairOfColliders = (Collider,Collider)

  object Detector:
    import CollisionDetectors.given
    def detect[A <: Collider, B <: Collider](c1: A, c2: B)(using col: CollisionDetector[A, B]): Boolean =
      col.detect(c1, c2)


  trait CollisionDetector[A <: Collider, B <: Collider]:
    def detect(c1: A, c2: B): Boolean

  object CollisionDetectors:
    given CircleToCircleDetector: CollisionDetector[CircleCollider, CircleCollider] with
      override def detect(c1: CircleCollider, c2: CircleCollider): Boolean =
        val dx = c1.origin.x - c2.origin.x
        val dy = c1.origin.y - c2.origin.y
        val dist = Math.sqrt(dx*dx + dy*dy)
        dist < c1.radius + c2.radius