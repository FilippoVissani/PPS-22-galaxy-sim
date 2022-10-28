package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyAliases.Temperature
import physics.Mass

object OperationsOnCelestialBody:
  object CelestialBodyBounds:
    val minTemp: Double = 1
    val maxTemp: Double = 1e40
    val minMass: Double = 1
    val maxMass: Double = 1e80

  import CelestialBodyBounds.*

  extension (celestialBody: CelestialBody)
    /**
     * Update the mass applying the given function
     */
    def updateMass(f: Mass => Mass): CelestialBody =
      val normalizer = normalize(minMass, maxMass)
      celestialBody.copy(mass = normalizer(f(celestialBody.mass)))

    /**
     * Update the temperature applying the given function
     */
    def updateTemperature(f: Temperature => Temperature): CelestialBody =
      val normalizer = normalize(minTemp, maxTemp)
      celestialBody.copy(temperature = normalizer(f(celestialBody.temperature)))

  /**
   * If the value is over the maxBound return a value = maxBound - 10%
   * If the value is under the minBound return a value = minBound + 10%
   */
  private def normalize(minBound: Double, maxBound: Double)(value: Double): Double = value match
    case value if value > maxBound => maxBound * 0.9
    case value if value < minBound => minBound * 1.1
    case _ => value