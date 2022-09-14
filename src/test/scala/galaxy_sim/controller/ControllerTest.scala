package galaxy_sim.controller

import galaxy_sim.MVCAssembler
import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ControllerTest extends AnyFlatSpec with should.Matchers:

  val mvcAssembler: MVCAssembler = MVCAssembler()

  "Controller" should "throw NotImplementedError" in {
    val controller = mvcAssembler.controller
    assertThrows[NotImplementedError](controller.startSimulation())
    assertThrows[NotImplementedError](controller.stopSimulation())
    assertThrows[NotImplementedError](controller.pauseSimulation())
    assertThrows[NotImplementedError](controller.resumeSimulation())
  }