package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{blackHole, body01, body02, body03, bounds}
import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.Statistics
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatisticsTest extends AnyFlatSpec with should.Matchers:

  val celestialBodiesMap: Map[CelestialBodyType, Set[CelestialBody]] = Map(
    MassiveStar -> Set(body01),
    Planet -> Set(body02, body03),
  )

  "planet quantity" should "be 2 (body02 and body03)" in {
    val simulation = Simulation(galaxy = celestialBodiesMap, bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val planetQuantity = Statistics.quantityOfThisCelestialBody(CelestialBodyType.Planet, simulation.galaxy)
    planetQuantity shouldBe 2
  }

  "the simulation" should "contain 2 planets and 1 MassiveStar" in {
    val simulation = Simulation(galaxy = celestialBodiesMap, bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val celestialBodiesQuantity = Statistics.numberOfCelestialBodiesForEachType(simulation.galaxy)
    celestialBodiesQuantity(CelestialBodyType.Planet) shouldBe 2
    celestialBodiesQuantity(CelestialBodyType.MassiveStar) shouldBe 1
  }

  "the simulation" should "contain 67% planet and 33% MassiveStar" in {
    val simulation = Simulation(galaxy = celestialBodiesMap, bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val celestialBodiesPercentage = Statistics.percentageOfCelestialBodiesForEachType(simulation.galaxy)
    celestialBodiesPercentage(CelestialBodyType.Planet) shouldBe 67
    celestialBodiesPercentage(CelestialBodyType.MassiveStar) shouldBe 33
  }

  "the simulation" should "contain 3 bodies" in {
    val simulation = Simulation(galaxy = celestialBodiesMap, bounds = bounds, virtualTime = 0, deltaTime = 1000)
    val quantityOfTotalBodies = Statistics.quantityOfTotalBodies(simulation.galaxy)
    quantityOfTotalBodies shouldBe 3

  }