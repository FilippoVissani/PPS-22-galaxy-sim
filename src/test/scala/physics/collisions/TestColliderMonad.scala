package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec

class TestColliderMonad extends AnyFlatSpec:
  import physics.collisions.monads.ColliderMonad.*
  import physics.collisions.instances.ImpactInstances.given
  import physics.collisions.instances.IntersectionInstances.given

  "A Collider" should "be used in for-comprehensions" in {
    for
      y <- collides(1, 1)
      res <- if y then impact(1, 1) else unit(0)
    yield assert(res + 1 == 3)
  }
