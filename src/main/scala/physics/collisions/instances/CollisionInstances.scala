package physics.collisions.instances

import physics.collisions.Collision.Collision
import physics.rigidbody.CollisionBoxes.CircleCollisionBox

object CollisionInstances:
  given IntToIntCollision : Collision[Int] =
    Collision.from[Int](_ == _)(_ + _)

  given CircleToCircleCollision: Collision[CircleCollisionBox] =
    Collision.from[CircleCollisionBox]((c1, c2) => {
      val dx = c1.origin.x - c2.origin.x
      val dy = c1.origin.y - c2.origin.y
      val dist = Math.sqrt(dx * dx + dy * dy)
      dist < c1.radius + c2.radius
    })((c1, _) => c1)
