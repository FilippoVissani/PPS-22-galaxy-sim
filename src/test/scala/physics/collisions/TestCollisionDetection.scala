package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.Pair
import physics.collisions.CollisionDetection.Colliders.{CircleCollider, Collider, RectangleCollider}
import physics.collisions.CollisionDetection.CollisionDetector


class TestCollisionDetection extends AnyFlatSpec with CollisionDetectionTest:
  import CollisionDetection.CollisionDetectors.given

  "A CollisionDetector" should "detect collision between two circular entities" in {
    val c1 = CircleCollider(Pair(0,0), 2)
    val c2 = CircleCollider(Pair(1,1), 0.5)
    assert(testDetection(c1, c2))
  }

  it should "also detect collision between rectangular entities" in {
    val c1 = RectangleCollider(Pair(5,5), 50, 50)
    val c2 = RectangleCollider(Pair(20,10), 60, 40)
    assert(testDetection(c1, c2))
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircleCollider(Pair(0,0), 0.1)
    val c2 = CircleCollider(Pair(1,1), 0.5)
    assert(!testDetection(c1, c2))
  }
