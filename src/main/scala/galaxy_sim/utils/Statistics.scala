package galaxy_sim.utils

import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation}
import physics.Pair

type Percentage = Int

object Statistics:

  /**
   * Count the number of celestial bodies of the desired type
   * @param celestialBodyType the type of the celestial body you want to count
   * @param celestialBodies the map containing all the celestial bodies
   * @return the number of celestial bodies of the desired type
   */
  def quantityOfThisCelestialBody(celestialBodyType: CelestialBodyType, celestialBodies: Map[CelestialBodyType, Set[CelestialBody]]): Int =
    celestialBodies(celestialBodyType).size

  /**
   * Count the number of celestial bodies for each type
   * @param celestialBodies the map containing all the celestial bodies
   * @return a map containing the number of celestial bodies for each type
   */
  def numberOfCelestialBodiesForEachType(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]]):  Map[CelestialBodyType, Int] =
    celestialBodies.map((bodyType, set) => (bodyType, set.size))

  /**
   * Percentage of celestial bodies for each type
   * @param celestialBodies the map containing all the celestial bodies
   * @return a map containing the percentage of celestial bodies for each type
   */
  def percentageOfCelestialBodiesForEachType(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]]): Map[CelestialBodyType, Percentage] =
    numberOfCelestialBodiesForEachType(celestialBodies).map(toPercentage(celestialBodies.flatMap((_,v) => v).size))

  private def toPercentage(totalBodies: Int): ((CelestialBodyType, Int)) => (CelestialBodyType, Percentage) =
    element => (element._1, (element._2 * 100.0 / totalBodies).round.toInt)

