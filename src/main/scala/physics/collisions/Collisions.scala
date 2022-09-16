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
    def detect[A: CollisionDetector](pairOfColliders: A): Boolean =
      summon[CollisionDetector[A]].detect(pairOfColliders)

  trait CollisionDetector[PairOfColliders]:
    def detect(p: PairOfColliders): Boolean

  object CollisionDetectors:
    given CircleToCircleDetector: CollisionDetector[(CircleCollider, CircleCollider)] with
      override def detect(p: (CircleCollider, CircleCollider)): Boolean =
        val c1 = p._1
        val c2 = p._2
        val dx = c1.origin.x - c2.origin.x
        val dy = c1.origin.y - c2.origin.y
        val dist = Math.sqrt(dx*dx + dy*dy)
        dist < c1.radius + c2.radius