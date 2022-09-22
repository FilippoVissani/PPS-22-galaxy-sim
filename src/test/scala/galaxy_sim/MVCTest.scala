package galaxy_sim

import org.scalatest.funsuite.AnyFunSuite
import galaxy_sim.model.{CelestialBody, CelestialBodyGenerator, ModelModule}
import galaxy_sim.view.ViewModule
import galaxy_sim.controller.ControllerModule
import org.scalatest.matchers.should.Matchers.*

class MVCTest extends AnyFunSuite:
  test("MVC correctly defines constructors"){
    "MVCAssembler()" should compile
  }

  test("Model operations") {
    val mvc = MVCAssembler()
    val celestialBody: CelestialBody = CelestialBodyGenerator.generateRandomCelestialBody(150)

    mvc.model.virtualTime shouldBe 0
    mvc.model.celestialBodies.size shouldBe 10
    mvc.model.addCelestialBody(celestialBody)
    mvc.model.celestialBodies.size shouldBe 11
    mvc.model.removeCelestialBody(celestialBody)
    mvc.model.celestialBodies.size shouldBe 10
  }

class MVCAssembler extends ModelModule.Interface
with ViewModule.Interface
with ControllerModule.Interface:
  override val model: ModelModule.Model = ModelImpl()
  override val view: ViewModule.View = TextualView()
  override val controller: ControllerModule.Controller = ControllerImpl()
