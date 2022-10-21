package physics.rigidbody

import physics.Position

object CollisionBoxes:
  /** A basic shape to be used alongside [[Intersection]] for implementing collision checking abstracting from the real
   * shape of the objects we want to check. */
  sealed trait CollisionBox
  
  /** A circular [[CollisionBox]] */
  case class CircleCollisionBox(origin: Position, radius: Double) extends CollisionBox
