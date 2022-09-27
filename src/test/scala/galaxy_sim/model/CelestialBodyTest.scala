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
import galaxy_sim.model.BodyOperations.*
import galaxy_sim.model.SimulationConfig.blackHole

class CelestialBodyTest extends AnyFunSuite:
  val value = 150
  val delta = 0.2
  val physicalEntity: PhysicalEntity = PhysicalEntity(
    gForceVector = Pair(value, value),
    speedVector = Pair(value, value),
    mass = value,
    pos = Pair(value, value),
    aphelionSpeed = value)
  val body: Body = Body(physicalEntity, value, value)
  val celestialBody: CelestialBody = CelestialBody("Star1", value, body)

  test("Body operations") {
    body.updatePhysicalEntity(b => changeMass(b, b.mass + delta)).mass shouldBe value + delta
    body.updateRadius(r => r + delta).radius shouldBe value + delta
    body.updateTemperature(t => t + delta).temperature shouldBe value + delta
  }

  test("CelestialBody operations"){
    celestialBody.position shouldBe Pair(value, value)
    celestialBody.gForceVector shouldBe Pair(value, value)
    celestialBody.speedVector shouldBe Pair(value, value)
    celestialBody.mass shouldBe value
    celestialBody.aphelionSpeed shouldBe value
    celestialBody.updateName("Test1").name shouldBe "Test1"
    celestialBody.updateBirthTime(b => b + delta).birthTime shouldBe value + delta
    celestialBody.updateBody(b => b.updateRadius(r => r + delta)).radius shouldBe value + delta
    blackHole.typeOf shouldBe CelestialBodyType.BlackHole
  }
