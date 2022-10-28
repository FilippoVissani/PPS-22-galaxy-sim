package physics.collisions

import org.scalatest.flatspec.AnyFlatSpec
import physics.{Pair, Position}
import physics.collisions.CollisionMockups.*
import physics.collisions.CollisionMockups.given
import physics.collisions.syntax.CollisionSyntax.*

class TestImpact extends AnyFlatSpec:
  "A CollisionSolver" should "produce the result of a collision between entities" in {
    val star = Star(Pair(0, 0), 2, 100)
    val star1 = Star(Pair(1, 2), 10, 1)
    val res = star |*| star1
    assert(res == Star(Pair(0.0,0.0),2.0,100.66666666666667))
  }

  it should "not produce a different value from the first argument if the two aren't colliding" in {
    val star = Star(Pair(0, 0), 2, 100)
    val star1 = Star(Pair(10, 20), 10, 1)
    val res = star |*| star1
    assert(res == star)
  }
