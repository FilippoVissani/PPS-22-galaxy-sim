package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import physics.{Pair, Position}
import physics.collisions.CollisionMockups.*
import physics.collisions.CollisionMockups.given
import physics.collisions.Collider.*

class TestCollisionSolving extends AnyFlatSpec:
  "A CollisionSolver" should "produce the result of a collision between entities" in {
    val star = Star(Pair(0, 0), 2, 100)
    val nebula = Nebula(Pair(1, 2), 10, 1)
    val collidingStar = Collider(star)
    val result = collidingStar >< nebula
    val expectedResult = Some(System(
      Pair(0.0, 0.0),
      Star(Pair(0.0, 0.0), 2.0, 50.0),
      List(Planet(Pair(1.0, 1.0), 0.1, 10.0), Planet(Pair(0.5, 1.5), 0.01, 1.0))
    ))
    assert(result equals expectedResult)
  }
