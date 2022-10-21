package physics.collisions.instances

import physics.collisions.Impact.Impact

/** Contains implementations of [[Impact]] for some basic types. */
object ImpactInstances:
  given IntImpact: Impact[Int] =
    Impact.from(_ + _)
