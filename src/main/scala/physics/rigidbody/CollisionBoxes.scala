package physics.rigidbody

import physics.Position

object CollisionBoxes:
  sealed trait CollisionBox
  case class CircleCollisionBox(origin: Position, radius: Double) extends CollisionBox
