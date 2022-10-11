package physics.collisions

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen
import physics.Pair
import physics.collisions.Collider.*
import physics.collisions.CollisionDetection.CollisionBoxes.CircleCollisionBox
import physics.collisions.CollisionDetection.CollisionCheckers.given
import physics.collisions.CollisionMockups.{Nebula, Planet, Star, System}

class CollisionSpec extends AnyFeatureSpec with GivenWhenThen:

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

  Feature("The user can see the effect of a collision between entities") {
    info("As a programmer")
    info("I want to establish a mapping between colliding entities and the result of the collision")

    Scenario("Two entities are colliding") {
      Given("Two colliding entities")
      val star = Star(Pair(0, 0), 2, 100)
      val nebula = Nebula(Pair(1, 2), 10, 1)
      val collidingStar = Collider(star)
      When("I solve the collision")
      val result = collidingStar >< nebula
      Then("I can see the result of the collision")
      val expectedResult = Some(System(
        Pair(0.0, 0.0),
        Star(Pair(0.0, 0.0), 2.0, 50.0),
        List(Planet(Pair(1.0, 1.0), 0.1, 10.0), Planet(Pair(0.5, 1.5), 0.01, 1.0))
      ))
      assert(result equals expectedResult)
    }
  }
