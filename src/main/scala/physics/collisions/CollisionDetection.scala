package physics.collisions

import physics.{Pair, Position}
import physics.collisions.CollisionDetection.CollisionBoxes.{CircleCollisionBox, CollisionBox, RectangleCollisionBox}
import physics.dynamics.PhysicalEntity

object CollisionDetection:

  trait CollisionChecker[A, B]:
    def check(a: A, b: B): Boolean
  object CollisionBoxes:
    trait CollisionBox
    case class CircleCollisionBox(origin: Position, radius: Double) extends CollisionBox
    case class RectangleCollisionBox(topLeft: Position, height: Double, width: Double) extends CollisionBox

  object CollisionCheckers:
    given CircleToCircleChecker: CollisionChecker[CircleCollisionBox, CircleCollisionBox] with
      override def check(c1: CircleCollisionBox, c2: CircleCollisionBox): Boolean =
        val dx = c1.origin.x - c2.origin.x
        val dy = c1.origin.y - c2.origin.y
        val dist = Math.sqrt(dx * dx + dy * dy)
        dist < c1.radius + c2.radius

    given RectangleToRectangleChecker: CollisionChecker[RectangleCollisionBox, RectangleCollisionBox] with
      override def check(c1: RectangleCollisionBox, c2: RectangleCollisionBox): Boolean =
        !(c1.topLeft.x > c2.topLeft.x + c2.width ||
          c1.topLeft.x + c1.width < c2.topLeft.x ||
          c1.topLeft.y > c2.topLeft.y + c2.height ||
          c1.height + c1.topLeft.y < c2.topLeft.y)
        
