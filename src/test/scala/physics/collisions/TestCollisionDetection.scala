package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.collisions.Collisions.Detector.detect
import physics.collisions.Collisions.{CollisionDetector, P2d}


class TestCollisionDetection extends AnyFlatSpec:
  import Collisions.Colliders.CircleCollider
  import Collisions.Detector
  import Collisions.CollisionDetectors.given


  "A CollisionDetector" should "detect collision between two colliders" in {
    val c1 = CircleCollider(P2d(0,0), 2)
    val c2 = CircleCollider(P2d(1,1), 0.5)
    assert(detect(c1, c2))
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircleCollider(P2d(0,0), 0.1)
    val c2 = CircleCollider(P2d(1,1), 0.5)
    assert(!detect(c1, c2))
  }
