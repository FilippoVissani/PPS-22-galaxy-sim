package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.compile
import org.scalatest.matchers.should.Matchers.*
import physics.Pair

class BoundaryTest extends AnyFunSuite:
  test("Boundary correctly defines constructors"){
    "Boundary(150, 150, 150, 150)" should compile
  }

  test("Toroidal bounds"){
    val position = Pair(-1d, 151d)
    val bounds = Boundary(0d, 150d, 0d, 150d)
    bounds.toToroidal(position) shouldBe Pair(150d, 0d)
  }
