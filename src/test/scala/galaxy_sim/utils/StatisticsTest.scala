package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{blackHole, bounds, interstellarCloud}
import galaxy_sim.model.{CelestialBodyType, Simulation}
import galaxy_sim.utils.Statistics
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatisticsTest extends AnyFlatSpec with should.Matchers:

  //TODO set the proper properties of celestial bodies to match the correct type
  "the simulation" should "contains 50% of blackHoles and 50% of interstellarClouds" in {
    val simulation = Simulation(celestialBodies = Set(blackHole, interstellarCloud), bounds = bounds)
    val celestialBodiesQuantity = Statistics.numberOfCelestialBodiesForEachType(simulation.celestialBodies)
    //celestialBodiesQuantity.foreach((k, v) => println(s"tipo $k, Quantità $v"))
    celestialBodiesQuantity(CelestialBodyType.Supernova) shouldBe 1
    celestialBodiesQuantity(CelestialBodyType.InterstellarCloud) shouldBe 1
  }
