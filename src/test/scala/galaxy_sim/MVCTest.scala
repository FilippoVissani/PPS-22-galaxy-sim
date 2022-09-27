package galaxy_sim

import org.scalatest.funsuite.AnyFunSuite
import galaxy_sim.model.{CelestialBody, CelestialBodyGenerator, Clock, ModelModule}
import galaxy_sim.view.ViewModule
import galaxy_sim.controller.ControllerModule
import org.scalatest.matchers.should.Matchers.*
import galaxy_sim.model.CelestialBodyOperations.*

class MVCTest extends AnyFunSuite:
  test("MVC correctly defines constructors"){
    "MVCAssembler()" should compile
  }

  test("Model operations") {
    val mvc = MVCAssembler()
    val celestialBody: CelestialBody = CelestialBodyGenerator.generateRandomCelestialBody(150)

    mvc.model.simulation.clock shouldBe Clock()
    mvc.model.simulation.celestialBodies.size shouldBe 2
    mvc.model.addCelestialBody(celestialBody)
    mvc.model.simulation.celestialBodies.size shouldBe 3
    mvc.model.removeCelestialBody(celestialBody)
    mvc.model.simulation.celestialBodies.size shouldBe 2
    val body = mvc.model.simulation.celestialBodies.head
    mvc.model.updateCelestialBody(body)(b => b.updateName("Test"))
    mvc.model.simulation.celestialBodies.count(b => b.name == "Test") shouldBe 1
    mvc.model.simulation.celestialBodies.size shouldBe 2
  }

class MVCAssembler extends ModelModule.Interface
with ViewModule.Interface
with ControllerModule.Interface:
  override val model: ModelModule.Model = ModelImpl()
  override val view: ViewModule.View = TextualView()
  override val controller: ControllerModule.Controller = ControllerImpl()
