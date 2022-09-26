package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import galaxy_sim.model.SimulationOperations.*

class SimulationTest  extends AnyFunSuite:
  test("Simulation operations"){
    val clock: Clock = Clock()
    val bounds: Boundary = Boundary(10, 10, 10, 10)
    Simulation(bounds = bounds).celestialBodies.size shouldBe 0
    val simulation = Simulation((0 to 9) map (_ => CelestialBodyGenerator.generateRandomCelestialBody(150)), bounds, clock)
    simulation.deltaTime shouldBe 0.1
    simulation.virtualTime shouldBe 0
    simulation.celestialBodies.size shouldBe 10
    simulation.updateCelestialBodies(c => c.filter(b => b.name != "Star1")).celestialBodies.size shouldBe 0
    simulation.incrementVirtualTime().virtualTime shouldBe 0.1
  }
