package galaxy_sim

import org.scalatest.funsuite.AnyFunSuite
import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule
import galaxy_sim.controller.ControllerModule
import org.scalatest.matchers.should.Matchers.*

class MVCTest extends AnyFunSuite:
  test("MVC correctly defines constructors"){
    "MVCAssembler()" should compile
  }

class MVCAssembler extends ModelModule.Interface
with ViewModule.Interface
with ControllerModule.Interface:
  override val model: ModelModule.Model = ModelImpl()
  override val view: ViewModule.View = TextualView()
  override val controller: ControllerModule.Controller = ControllerImpl()
