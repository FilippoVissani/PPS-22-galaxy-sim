package galaxy_sim.model

import galaxy_sim.model.CelestialBody.*
import galaxy_sim.model.SimulationConfig.blackHole
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import physics.{GravityForceVector, Pair}
import physics.dynamics.PhysicalEntity

class CelestialBodyTest extends AnyFunSuite:

  test("Body operations") {
    """CelestialBody(mass = 750,
      |    aphelionSpeed = 20,
      |    gForceVector = Pair(0, 0.98),
      |    speedVector = Pair(0, 0),
      |    position = Pair(500, 500),
      |    name = "Black Hole",
      |    radius = 20,
      |    temperature = 1100)""".stripMargin should compile
  }

