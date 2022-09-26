package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe

class ClockTest extends AnyFunSuite:
  test("Increment clock virtual time") {
    val c = Clock(0, 0.4)
    c.incrementVirtualTime().incrementVirtualTime().virtualTime shouldBe 0.8
  }
