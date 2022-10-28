package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{body01, body04, bounds, body05, blackHole}
import galaxy_sim.model.{CelestialBodyType, Simulation, CelestialBody}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.Statistics
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatisticsTest extends AnyFlatSpec with should.Matchers:

  val celestialBodiesMap: Map[CelestialBodyType, Set[CelestialBody]] = Map(
    MassiveStar -> Set(body01),
    Planet -> Set(body04, body05),
  )

  "planet quantity" should "be 2 (moon and earth)" in {
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