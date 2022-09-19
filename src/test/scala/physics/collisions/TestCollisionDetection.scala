package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import physics.collisions.Collisions.Colliders.RectangleCollider
import physics.collisions.Collisions.CollisionManager.detect
import physics.collisions.Collisions.{CollisionDetector, P2d, RigidBody}


class TestCollisionDetection extends AnyFlatSpec:
  import Collisions.Colliders.CircleCollider
  import Collisions.CollisionManager
  import Collisions.CollisionDetectors.given

  class CircularEntity(pos: P2d, radius: Double) extends RigidBody[CircleCollider]:
    override def collider: CircleCollider = CircleCollider(pos, radius)

  class RectangularEntity(pos: P2d, width: Double, height: Double) extends RigidBody[RectangleCollider]:
    override def collider: RectangleCollider = RectangleCollider(pos, height, width)

  "A CollisionDetector" should "detect collision between two circular entities" in {
    val c1 = CircularEntity(P2d(0,0), 2)
    val c2 = CircularEntity(P2d(1,1), 0.5)
    assert(detect(c1, c2))
  }

  it should "also detect collision between rectangular entities" in {
    val c1 = RectangularEntity(P2d(5,5), 50, 50)
    val c2 = RectangularEntity(P2d(20,10), 60, 40)
    assert(detect(c1, c2))
  }

  it should "not detect collisions between colliders that are too distant" in {
    val c1 = CircularEntity(P2d(0,0), 0.1)
    val c2 = CircularEntity(P2d(1,1), 0.5)
    assert(!detect(c1, c2))
  }
