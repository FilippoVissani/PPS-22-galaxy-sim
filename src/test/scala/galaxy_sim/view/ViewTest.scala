package galaxy_sim.view

import galaxy_sim.MVCAssembler
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.awt.{Dimension, Toolkit}

class ViewTest extends AnyFlatSpec with should.Matchers:

  val mvcAssembler: MVCAssembler = MVCAssembler()

  "View" should "throw NotImplementedError" in {
    val view = mvcAssembler.view
    assertThrows[NotImplementedError](view.start())
    assertThrows[NotImplementedError](view.stop())
    assertThrows[NotImplementedError](view.pause())
    assertThrows[NotImplementedError](view.resume())
    assertThrows[NotImplementedError](view.update())
  }
