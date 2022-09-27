package physics.collisions

import physics.collisions.CollisionDetection.Colliders.Collider
import physics.collisions.CollisionDetection.CollisionDetector

trait CollisionDetectionTest:
  def testDetection[A <: Collider, B<: Collider](c1: A, c2: B)(using col: CollisionDetector[A, B]): Boolean =
    col.detect(c1, c2)
