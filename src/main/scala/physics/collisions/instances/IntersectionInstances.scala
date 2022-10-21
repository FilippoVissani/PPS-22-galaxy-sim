package physics.collisions.instances

import physics.collisions.Intersection.Intersection
import physics.rigidbody.CollisionBoxes.CircleCollisionBox

/** Contains implementations of [[Intersection]] for some base types. */
object IntersectionInstances:
  given IntToIntCollision : Intersection[Int] =
    Intersection.from(_ == _)

  given CircleToCircleCollision: Intersection[CircleCollisionBox] =
    Intersection.from((c1, c2) => {
      val dx = c1.origin.x - c2.origin.x
      val dy = c1.origin.y - c2.origin.y
      val dist = Math.sqrt(dx * dx + dy * dy)
      dist < c1.radius + c2.radius
    })
