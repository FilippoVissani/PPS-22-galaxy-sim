package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.Pair
import physics.ref.CollisionBoxes.{CircleCollisionBox, CollisionBox}


class TestCollisionDetection extends AnyFlatSpec:
  import physics.ref.Collisions.given
  import physics.ref.Collision.*

  "A CollisionDetector" should "detect collision between two circular entities" in {
    val c1 = CircleCollisionBox(Pair(0,0), 2)
    val c2 = CircleCollisionBox(Pair(1,1), 0.5)
    assert(collides(c1, c2))
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircleCollisionBox(Pair(0,0), 0.1)
    val c2 = CircleCollisionBox(Pair(1,1), 0.5)
    assert(! collides(c1, c2))
  }
