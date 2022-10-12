package galaxy_sim.model

import galaxy_sim.model.SimulationConfig.{blackHole, bounds}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.compile
import org.scalatest.matchers.should.Matchers.should

class SimulationTest extends AnyFunSuite:
  test("Simulation correctly defines constructors"){
    "Simulation(celestialBodies = Set(blackHole), bounds = bounds, deltaTime = 1000)" should compile
  }
