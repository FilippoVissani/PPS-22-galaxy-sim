package galaxy_sim.model

import galaxy_sim.MVCAssembler
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.Predef.Set

class ModelTest extends AnyFlatSpec with should.Matchers:

  val mvcAssembler: MVCAssembler = MVCAssembler()

  "Model" should "throw return an empty Set of entities" in {
    mvcAssembler.model.entities shouldBe Set()
  }
