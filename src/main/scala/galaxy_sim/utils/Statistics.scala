package galaxy_sim.utils

import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation}
import physics.Pair

object Statistics:

  /**
   * Count the number of celestial bodies of the desired type
   * @param celestialBodyType the type of the celestial body you want to count
   * @param celestialBodies the set containing all the celestial bodies
   * @return the number of celestial bodies of the desired type in the set
   */
  def quantityOfThisCelestialBody(celestialBodyType: CelestialBodyType, celestialBodies: Set[CelestialBody]): Int =
    celestialBodies.map(body => body.typeOf).count(bType => bType.equals(celestialBodyType))

  /**
   * Count the number of celestial bodies for each type
   * @param celestialBodies the set containing all the celestial bodies
   * @return a map containing the number of celestial bodies for each type
   */
  def numberOfCelestialBodiesForEachType(celestialBodies: Set[CelestialBody]):  Map[CelestialBodyType, Int] =
    CelestialBodyType.values.collect(bType => bType -> quantityOfThisCelestialBody(bType, celestialBodies)).toMap
    