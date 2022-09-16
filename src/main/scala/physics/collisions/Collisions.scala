package physics.collisions

object Collisions:
  case class P2d(x: Float, y: Float)
  sealed trait Collider
  case class CircleCollider(origin: P2d, radius: Float)
  class CircleToCircleDetector:
    def detect(c1: CircleCollider, c2: CircleCollider): Boolean =
      val dx = c1.origin.x - c2.origin.x
      val dy = c1.origin.y - c2.origin.y
      val dist = Math.sqrt(dx*dx + dy*dy)
      dist < c1.radius + c2.radius
