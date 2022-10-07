package galaxy_sim.view

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.Boundary

case class Envelope(celestialBodies: Set[CelestialBody], bounds: Boundary, virtualTime: Double)
