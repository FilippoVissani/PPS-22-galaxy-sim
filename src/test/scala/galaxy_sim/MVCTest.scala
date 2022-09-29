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
    val initialCelestialBodiesSize = mvc.model.simulation.celestialBodies.size
    mvc.model.addCelestialBody(celestialBody)
    mvc.model.simulation.celestialBodies.size shouldBe initialCelestialBodiesSize + 1
    mvc.model.removeCelestialBody(celestialBody)
    mvc.model.simulation.celestialBodies.size shouldBe initialCelestialBodiesSize
    val actualVirtualTime = mvc.model.simulation.virtualTime
    mvc.model.incrementVirtualTime()
    mvc.model.simulation.virtualTime shouldBe actualVirtualTime + mvc.model.simulation.deltaTime
    val interstellarCloud = mvc.model.simulation.celestialBodies.filter(x => x.name == "Interstellar Cloud").head
    mvc.model.moveCelestialBodiesToNextPosition()
    mvc.model.simulation.celestialBodies.count(x => x == interstellarCloud) shouldBe 0
  }

class MVCAssembler extends ModelModule.Interface
with ViewModule.Interface
with ControllerModule.Interface:
  override val model: ModelModule.Model = ModelImpl()
  override val view: ViewModule.View = TextualView()
  override val controller: ControllerModule.Controller = ControllerImpl()
