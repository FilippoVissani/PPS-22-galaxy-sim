package physics.collisions

import physics.collisions.Collisions.Colliders.Collider
import physics.collisions.Collisions.CollisionDetector

trait CollisionDetectionTest:
  def testDetection[A <: Collider](c1: A, c2: A)(using col: CollisionDetector[A]): Boolean =
    col.detect(c1, c2)
