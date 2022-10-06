package physics.collisions

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen
import physics.Pair
import physics.collisions.Collider.*
import physics.collisions.CollisionDetection.CollisionBoxes.CircleCollisionBox
import physics.collisions.CollisionDetection.CollisionCheckers.given

class CollisionDetectionSpec extends AnyFeatureSpec with GivenWhenThen:

  Feature("The user can see that two entities are colliding") {
    info("As a programmer")
    info("I want to check whether two RigidBodies are colliding")

    Scenario("Two RigidBodies have colliders that overlap"){
      Given("Two RigidBodies")
      val c1 = CircleCollisionBox(Pair(0,0), 2)
      val c2 = CircleCollisionBox(Pair(1,1), 0.5)
      When("I try to detect a collision between them")
      val collider = Collider(c1) % c2
      Then("I'm able to check that it is in place")
      assert(collider != Collider.None())
    }

    Scenario("Two RigidBodies have colliders that don't overlap"){
      Given("Two RigidBodies")
      val c1 = CircleCollisionBox(Pair(0,0), 0.1)
      val c2 = CircleCollisionBox(Pair(1,1), 0.5)
      When("I try to detect a collision between them")
      val collider = Collider(c1) % c2
      Then("I'm able to check that it is not in place")
      assert(collider == Collider.None())
    }

  }
