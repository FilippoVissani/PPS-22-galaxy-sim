package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import galaxy_sim.model.SimulationConfig.body01
import galaxy_sim.utils.OperationsOnCelestialBody.{updateMass, updateTemperature}
import galaxy_sim.utils.OperationsOnCelestialBody.CelestialBodyBounds.*

class OperationsOnCelestialBodyTest extends AnyFlatSpec with should.Matchers:

  val celestialBody: CelestialBody = body01

  "the mass after the update to a mass > maxMass" should "equals to maxMass * 0.9" in {
    body01.updateMass(_ => maxMass + maxMass).mass shouldBe maxMass * 0.9
  }

  "the mass after the update to a mass < minMass" should "equals to minMass * 1.1" in {
    body01.updateMass(_ => minMass - minMass).mass shouldBe minMass * 1.1
  }

  "the temperature after the update to a temperature < minTemp" should "equals to minTemp * 1.1" in {
    body01.updateTemperature(_ => minTemp - minTemp).temperature shouldBe minTemp * 1.1
  }

  "the temperature after the update to a temperature > maxTemp" should "equals to maxTemp * 0.9" in {
    body01.updateTemperature(_ => maxTemp + maxTemp).temperature shouldBe maxTemp * 0.9
  }