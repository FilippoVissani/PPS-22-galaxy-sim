package physics.collisions

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen
import physics.collisions.Collisions.Colliders.CircleCollider
import physics.collisions.Collisions.{P2d, RigidBody}
import physics.collisions.Collisions.CollisionManager.detect
import physics.collisions.Collisions.CollisionDetectors.given

class CollisionDetectionSpec extends AnyFeatureSpec with GivenWhenThen:
  class CircularEntity(pos: P2d, radius: Double) extends RigidBody[CircleCollider]:
    override def collider: CircleCollider = CircleCollider(pos, radius)

  Feature("The user can see that two entities are colliding") {
    info("As a programmer")
    info("I want to check whether two RigidBodies are colliding")

    Scenario("Two RigidBodies have colliders that overlap"){
      Given("Two RigidBodies")
      val c1 = CircularEntity(P2d(0,0), 2)
      val c2 = CircularEntity(P2d(1,1), 0.5)
      When("I try to detect a collision between them")
      val detection = detect(c1, c2)
      Then("I'm able to check that it is in place")
      assert(detection)
    }

    Scenario("Two RigidBodies have colliders that don't overlap"){
      Given("Two RigidBodies")
      val c1 = CircularEntity(P2d(0,0), 0.1)
      val c2 = CircularEntity(P2d(1,1), 0.5)
      When("I try to detect a collision between them")
      val detection = detect(c1, c2)
      Then("I'm able to check that it is not in place")
      assert(!detection)
    }

  }
