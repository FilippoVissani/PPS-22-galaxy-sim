package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{sun, earth, bounds, moon}
import galaxy_sim.model.{CelestialBodyType, Simulation}
import galaxy_sim.utils.Statistics
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatisticsTest extends AnyFlatSpec with should.Matchers:

  "planet quantity" should "be 2 (moon and earth)" in {
    val simulation = Simulation(celestialBodies = Set(sun, moon, earth), bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val planetQuantity = Statistics.quantityOfThisCelestialBody(CelestialBodyType.Planet, simulation.celestialBodies)
    planetQuantity shouldBe 2
  }

  "the simulation" should "contains 2 planets and 1 supernova" in {
    val simulation = Simulation(celestialBodies = Set(sun, moon, earth), bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val celestialBodiesQuantity = Statistics.numberOfCelestialBodiesForEachType(simulation.celestialBodies)
    //celestialBodiesQuantity.foreach((k, v) => println(s"tipo $k, Quantità $v"))
    celestialBodiesQuantity(CelestialBodyType.Planet) shouldBe 2
    celestialBodiesQuantity(CelestialBodyType.Supernova) shouldBe 1
  }

  "the simulation" should "contains 67% planet and 33% supernova" in {
    val simulation = Simulation(celestialBodies = Set(sun, moon, earth), bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val celestialBodiesPercentage = Statistics.percentageOfCelestialBodiesForEachType(simulation.celestialBodies)
    //celestialBodiesPercentage.foreach((k, v) => println(s"tipo $k, Quantità $v"))
    celestialBodiesPercentage(CelestialBodyType.Planet) shouldBe 67
    celestialBodiesPercentage(CelestialBodyType.Supernova) shouldBe 33
  }
