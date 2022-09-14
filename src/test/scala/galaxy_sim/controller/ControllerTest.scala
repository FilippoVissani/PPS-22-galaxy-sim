package galaxy_sim.controller

import galaxy_sim.Main
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ControllerTest extends AnyFlatSpec with should.Matchers:

  "Controller" should "throw NotImplementedError" in {
    val controller = Main.controller
    assertThrows[NotImplementedError](controller.startSimulation())
    assertThrows[NotImplementedError](controller.stopSimulation())
    assertThrows[NotImplementedError](controller.pauseSimulation())
    assertThrows[NotImplementedError](controller.resumeSimulation())
  }