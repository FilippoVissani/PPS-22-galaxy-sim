package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import physics.{Pair, Position}
import physics.collisions.CollisionMockups.*
import physics.collisions.CollisionMockups.given
import physics.collisions.Collider.*
import physics.ref.CollisionSyntax.*

class TestCollisionSolving extends AnyFlatSpec:
  "A CollisionSolver" should "produce the result of a collision between entities" in {
    val star = Star(Pair(0, 0), 2, 100)
    val nebula = Nebula(Pair(1, 2), 10, 1)
    val res = star impactWith nebula
    assert(res == Star(Pair(0.0,0.0),2.0,100.66666666666667))
  }
