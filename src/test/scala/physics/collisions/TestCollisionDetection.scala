package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.collisions.Collisions.Colliders.{Collider, CircleCollider, RectangleCollider}
import physics.collisions.Collisions.{CollisionDetector, P2d}


class TestCollisionDetection extends AnyFlatSpec:
  import Collisions.CollisionDetectors.given

  def detect[A <: Collider](c1: A, c2: A)(using col: CollisionDetector[A]): Boolean =
    col.detect(c1, c2)

  "A CollisionDetector" should "detect collision between two circular entities" in {
    val c1 = CircleCollider(P2d(0,0), 2)
    val c2 = CircleCollider(P2d(1,1), 0.5)
    assert(detect(c1, c2))
  }

  it should "also detect collision between rectangular entities" in {
    val c1 = RectangleCollider(P2d(5,5), 50, 50)
    val c2 = RectangleCollider(P2d(20,10), 60, 40)
    assert(detect(c1, c2))
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircleCollider(P2d(0,0), 0.1)
    val c2 = CircleCollider(P2d(1,1), 0.5)
    assert(!detect(c1, c2))
  }
