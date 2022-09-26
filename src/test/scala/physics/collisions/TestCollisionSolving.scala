package physics.collisions

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import physics.{GravityForceVector, Mass, Pair, Position, Speed, SpeedVector}
import physics.collisions.Collisions.Colliders.CircleCollider
import physics.collisions.Collisions.CollisionDetectors.given
import physics.collisions.Collisions.CollisionSolvers.given
import physics.collisions.Collisions.CollisionManager.process
import physics.collisions.Collisions.{P2d, RigidBody}

class TestCollisionSolving extends AnyFeatureSpec with GivenWhenThen:

  class SphericalEntity(override val mass: Mass,
                        override val position: Position,
                        override val aphelionSpeed: Speed,
                        override val speedVector: SpeedVector,
                        override val gForceVector: GravityForceVector,
                        radius: Double) extends RigidBody[CircleCollider]:
    override def collider: CircleCollider = CircleCollider(P2d(position.x, position.y), radius)

  Feature("As a user, I want to see the effect of collisions between entities") {
    Given("Two entities that collided")
    val e1 = SphericalEntity(1000, Pair(0,0), 0, Pair(0,0), Pair(0,0), 2)
    val e2 = SphericalEntity(10, Pair(1,1), 0, Pair(0,0), Pair(0,0), 0.5)
    When("I solve the collision effect between them")
    val col = process(e1, e2)(using CircleToCircleDetector)(using StarToGasNebulaSolver)
    Then("I can see the effect")
    assertResult(List(e1))(col.get)
  }
