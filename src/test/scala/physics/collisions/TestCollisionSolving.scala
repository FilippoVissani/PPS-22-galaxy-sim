package physics.collisions

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import physics.{GravityForceVector, Mass, Pair, Position, Speed, SpeedVector}
import physics.collisions.CollisionDetection.Colliders.{CircleCollider, Collider}
import physics.collisions.CollisionDetection.CollisionDetectors.given
import physics.collisions.CollisionSolving.CollisionSolvers.given
import physics.collisions.CollisionDetection.{CollisionDetector, P2d}
import physics.collisions.CollisionEngine.{RigidBody, process}
import physics.collisions.CollisionSolving.{CollisionSolver, SphericalEntity}
import physics.dynamics.PhysicalEntity

class TestCollisionSolving extends AnyFeatureSpec with GivenWhenThen:

  Feature("As a user, I want to see the effect of collisions between entities") {
    Given("Two entities that collided")
    val e1 = SphericalEntity(1000, Pair(0,0), 0, Pair(0,0), Pair(0,0), 2)
    val e2 = SphericalEntity(10, Pair(1,1), 0, Pair(0,0), Pair(0,0), 0.5)
    When("I solve the collision effect between them")
    val col = process(e1, e2)
    Then("I can see the effect")
    assertResult(List(e1))(col)
  }
