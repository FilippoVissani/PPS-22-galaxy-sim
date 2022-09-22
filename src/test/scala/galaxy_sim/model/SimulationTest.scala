package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe

class SimulationTest  extends AnyFunSuite:
  test("Simulation operations"){
    val simulation = Simulation((0 to 9) map (_ => CelestialBodyGenerator.generateRandomCelestialBody(150)), 0, 0.1)
    simulation.deltaTime shouldBe 0.1
    simulation.virtualTime shouldBe 0
    simulation.celestialBodies.size shouldBe 10
    simulation.updateCelestialBodies(simulation.celestialBodies.filter(b => b.name != "Star1")).celestialBodies.size shouldBe 0
    simulation.incrementVirtualTime().virtualTime shouldBe 0.1
  }
