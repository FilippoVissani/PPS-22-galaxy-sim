package physics.collisions

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{GivenWhenThen, stats}
import physics.Pair
import physics.ref.CollisionBoxes.CircleCollisionBox
import physics.ref.Collisions.given
import physics.collisions.CollisionMockups.{Nebula, Star, StarNebulaCollision}
import physics.collisions.CollisionMockups.given
import physics.ref.Collision

class CollisionSpec extends AnyFeatureSpec with GivenWhenThen:

  Feature("The user can see that two entities are colliding") {
    info("As a programmer")
    info("I want to check whether two RigidBodies are colliding")

    Scenario("Two RigidBodies have colliders that overlap"){
      Given("Two RigidBodies")
      val c1 = CircleCollisionBox(Pair(0,0), 2)
      val c2 = CircleCollisionBox(Pair(1,1), 0.5)
      When("I try to detect a collision between them")
      val collision = Collision.collides(c1, c2)
      Then("I'm able to check that it is in place")
      assert(collision)
    }

    Scenario("Two RigidBodies have colliders that don't overlap"){
      Given("Two RigidBodies")
      val c1 = CircleCollisionBox(Pair(0,0), 0.1)
      val c2 = CircleCollisionBox(Pair(1,1), 0.5)
      When("I try to detect a collision between them")
      val collision = Collision.collides(c1, c2)
      Then("I'm able to check that it is not in place")
      assert(!collision)
    }

    Scenario("Two entities that previously where colliding are distancing themselves") {
      Given("Two colliding entities")
      val star = Star(Pair(0,0), 2, 100)
      val nebula = Nebula(Pair(1, 1), 0.5, 10)
      val collision = Collision.collides(star, nebula)
      assert(collision)
      When("They move out of collision scope")
      val newNebula = nebula.copy(origin = Pair(3,3))
      Then("The collision is no more detected")
      val newCollision = Collision.collides(star, newNebula)
      assert(! newCollision)
    }

    Scenario("Two entities that weren't colliding are coming close enough") {
      Given("Two entities that aren't colliding")
      val star = Star(Pair(0, 0), 2, 100)
      val nebula = Nebula(Pair(3, 3), 0.5, 10)
      val collision = Collision.collides(star, nebula)
      assert(!collision)
      When("They enter collision scope")
      val newNebula = nebula.copy(origin = Pair(1,1))
      Then("The collision is detected")
      val newCollision = Collision.collides(star, newNebula)
      assert(newCollision)
    }
  }

  Feature("The user can see the effect of a collision between entities") {
    info("As a programmer")
    info("I want to establish a mapping between colliding entities and the result of the collision")


    import physics.ref.CollisionSyntax.*
    Scenario("Two entities are colliding") {
      Given("Two colliding entities")
      val star = Star(Pair(0, 0), 2, 100)
      val nebula = Nebula(Pair(1, 2), 10, 1)
      When("I solve the collision")
      val res = star impactWith nebula
      Then("I can see the result of the collision")
      assert(res == Star(Pair(0.0,0.0),2.0,100.66666666666667))
    }
  }
