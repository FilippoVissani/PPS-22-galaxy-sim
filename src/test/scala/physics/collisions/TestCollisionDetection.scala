package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.Pair
import physics.collisions.Collider.*
import physics.collisions.CollisionDetection.CollisionBoxes.{CircleCollisionBox, CollisionBox, RectangleCollisionBox}


class TestCollisionDetection extends AnyFlatSpec:
  import CollisionDetection.CollisionCheckers.given

  "A CollisionDetector" should "detect collision between two circular entities" in {
    val c1 = CircleCollisionBox(Pair(0,0), 2)
    val c2 = CircleCollisionBox(Pair(1,1), 0.5)
    val collider = Collider(c1) % c2
    assert(collider != Collider.None())
  }

  it should "also detect collision between rectangular entities" in {
    val c1 = RectangleCollisionBox(Pair(5,5), 50, 50)
    val c2 = RectangleCollisionBox(Pair(20,10), 60, 40)
    val collider = Collider(c1) % c2
    assert(collider != Collider.None())
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircleCollisionBox(Pair(0,0), 0.1)
    val c2 = CircleCollisionBox(Pair(1,1), 0.5)
    val collider = Collider(c1) % c2
    assert(collider == Collider.None())
  }
