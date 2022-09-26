package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.compile
import org.scalatest.matchers.should.Matchers.*

class BoundaryTest extends AnyFunSuite:
  test("Boundary correctly defines constructors"){
    "Boundary(150, 150, 150, 150)" should compile
  }
