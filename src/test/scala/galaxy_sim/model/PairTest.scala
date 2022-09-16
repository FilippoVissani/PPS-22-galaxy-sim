package galaxy_sim.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PairTest extends AnyFlatSpec with should.Matchers:

  "Pair(1, 1)" should "have the possibility to be mapped in Pair(0.1f, 0.1f)" in {
    Pair(1, 1).map(_ => 0.1f)(_ => 0.1f) shouldBe Pair(0.1f, 0.1f)
  }