package galaxy_sim.view

import galaxy_sim.Main
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ViewTest extends AnyFlatSpec with should.Matchers:

  "View" should "throw NotImplementedError" in {
    val view = Main.view
    assertThrows[NotImplementedError](view.start())
    assertThrows[NotImplementedError](view.stop())
    assertThrows[NotImplementedError](view.pause())
    assertThrows[NotImplementedError](view.resume())
  }
