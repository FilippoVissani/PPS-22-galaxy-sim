package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyAliases.Temperature
import physics.Mass

object OperationsOnCelestialBody:
  extension (celestialBody: CelestialBody)
    /**
     * Update the mass applying the given function
     */
    def updateMass(f: Mass => Mass): CelestialBody = 
      celestialBody.copy(mass = f(celestialBody.mass))

    /**
     * Update the temperature applying the given function
     */
    def updateTemperature(f: Temperature => Temperature): CelestialBody = 
      celestialBody.copy(temperature = f(celestialBody.temperature))