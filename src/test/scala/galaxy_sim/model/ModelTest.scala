package galaxy_sim.model

import galaxy_sim.Main
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.Predef.Set

class ModelTest extends AnyFlatSpec with should.Matchers:

  "Model" should "throw return an empty Set of entities" in {
    Main.model.entities() shouldBe Set()
  }
