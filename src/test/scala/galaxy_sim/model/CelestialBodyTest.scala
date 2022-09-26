package galaxy_sim.model

import galaxy_sim.model.CelestialBody.*
import galaxy_sim.model.CelestialBodyGenerator.random
import galaxy_sim.model.CelestialBodyOperations.*
import physics.dynamics.PhysicalEntity.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import physics.GravityForceVector
import physics.Pair
import physics.dynamics.PhysicalEntity

class CelestialBodyTest extends AnyFunSuite:
    test("CelestialBody operations"){
      val value = 150
      val delta = 0.2
      val physicalEntity = PhysicalEntity(
        gForceVector = Pair(value, value),
        speedVector = Pair(value, value),
        mass = value,
        pos = Pair(value, value),
        aphelionSpeed = value)
      val body: Body = Body(physicalEntity, value, value)
      val c: CelestialBody = CelestialBody("Star1", value, body)
      c.position shouldBe Pair(value, value)
      c.gForceVector shouldBe Pair(value, value)
      c.speedVector shouldBe Pair(value, value)
      c.mass shouldBe value
      c.aphelionSpeed shouldBe value
      c.updateName("Test1").name shouldBe "Test1"
      c.updateRadius(r => r + delta).radius shouldBe c.radius + delta
      c.updateBirthTime(b => b + delta).birthTime shouldBe c.birthTime + delta
      c.updatePhysicalEntity(b => changeMass(b, c.mass + delta)).mass shouldBe c.mass + delta
    }
