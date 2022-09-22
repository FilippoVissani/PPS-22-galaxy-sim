package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import physics.GravityForceVector
import physics.Pair

class CelestialBodyTest extends AnyFunSuite:
    test("CelestialBody correctly defines constructors"){
        """CelestialBody(name = "Star1",
        |gForceVector = Pair(10,10),
        |speedVector = Pair(10,10),
        |mass = 10,
        |position = Pair(10, 10),
        |aphelionSpeed = 10,
        |radius = 10)""".stripMargin should compile
    }
