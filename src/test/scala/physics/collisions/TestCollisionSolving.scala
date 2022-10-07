package physics.collisions

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import physics.{Pair, Position}
import physics.collisions.CollisionMockups.*
import physics.collisions.CollisionMockups.given
import physics.collisions.Collider.*

class TestCollisionSolving extends AnyFeatureSpec with GivenWhenThen:

  Feature("As a user, I want to see the effect of the collision between a Star and a Nebula") {
    Given("A Star and a Nebula, the former detecting a collision with the latter")
    val star = Star(Pair(0,0), 2, 100)
    val nebula = Nebula(Pair(1, 2), 10, 1)
    val collidingStar = Collider(star)
    When("I solve the collision effect between them")
    val result = collidingStar >< nebula
    Then("I can see that the collision originated a System")
    val expectedResult = Some(System(Pair(0.0,0.0),Star(Pair(0.0,0.0),2.0,50.0),List(Planet(Pair(1.0,1.0),0.1,10.0), Planet(Pair(0.5,1.5),0.01,1.0))))
    assert(result equals expectedResult)
  }
