package galaxy_sim.model

import galaxy_sim.model.SimulationAliases.Time

object SimulationAliases:
  type Time = Double

case class Simulation(celestialBodies: Set[CelestialBody], bounds: Boundary, virtualTime: Time = 0, deltaTime: Time = 0.1)
