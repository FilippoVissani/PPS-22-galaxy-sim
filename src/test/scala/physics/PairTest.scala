package physics

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import physics.Pair
import physics.Pair.given

class PairTest extends AnyFlatSpec with should.Matchers:

  "Pair(1, 1)" should "have the possibility to be mapped in Pair(0.1f, 0.1f)" in {
    Pair(1, 1).map((_, _) => (0.1f, 0.1f)) shouldBe Pair(0.1f, 0.1f)
  }

  "Pair(1d, 1d) plus Pair(2d, 2d)" should "be Pair(3d, 3d)" in {
    Pair(1d, 1d) + Pair(2d, 2d) shouldBe Pair(3d, 3d)
  }

  "Distance between Pair(1d, 0d) and Pair(10d, 0d)" should "be 9" in {
    Pair(1d, 0d) <-> Pair(10d, 0d) shouldBe 9d
  }

  "Module of Pair(5d, 0d)" should "be 5" in {
    Pair(5d, 0d).|| shouldBe 5d
  }
