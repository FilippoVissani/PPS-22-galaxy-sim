package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{sun, earth, bounds}
import galaxy_sim.model.{CelestialBodyType, Simulation}
import galaxy_sim.utils.Statistics
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatisticsTest extends AnyFlatSpec with should.Matchers:

  //TODO set the proper properties of celestial bodies to match the correct type
  "the simulation" should "contains 1 planet and 1 supernova" in {
    val simulation = Simulation(celestialBodies = Set(sun, earth), bounds = bounds)
    val celestialBodiesQuantity = Statistics.numberOfCelestialBodiesForEachType(simulation.celestialBodies)
    //celestialBodiesQuantity.foreach((k, v) => println(s"tipo $k, Quantità $v"))
    celestialBodiesQuantity(CelestialBodyType.Planet) shouldBe 1
    celestialBodiesQuantity(CelestialBodyType.Supernova) shouldBe 1
  }
