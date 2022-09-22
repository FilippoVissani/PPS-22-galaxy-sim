package galaxy_sim.model

import galaxy_sim.model.CelestialBody.*
import physics.dynamics.PhysicalEntity.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import physics.GravityForceVector
import physics.Pair

class CelestialBodyTest extends AnyFunSuite:
    test("CelestialBody operations"){
      val celestialBody: CelestialBody = CelestialBodyGenerator.generateRandomCelestialBody(150)
      updateName(celestialBody, "Test1").name shouldBe "Test1"
      updateRadius(celestialBody)(r => r * 0.1).radius shouldBe celestialBody.radius * 0.1
      updateBirthTime(celestialBody)(b => b * 0.1).birthTime shouldBe celestialBody.birthTime * 0.1
      updatePhysicalEntity(celestialBody)(b => changeMass(b, 5)).body shouldBe changeMass(celestialBody.body, 5)
    }
