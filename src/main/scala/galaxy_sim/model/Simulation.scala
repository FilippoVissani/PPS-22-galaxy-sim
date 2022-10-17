package galaxy_sim.model

import galaxy_sim.model.SimulationAliases.Time
import CelestialBodyType.*

object SimulationAliases:
  type Time = Double

case class Simulation(galaxy: Map[CelestialBodyType, Set[CelestialBody]], bounds: Boundary, virtualTime: Time = 0, deltaTime: Time)

val emptyGalaxy: Map[CelestialBodyType, Set[CelestialBody]] =
  Map(
    MassiveStar -> Set(),
    RedSuperGiant -> Set(),
    Supernova -> Set(),
    BlackHole -> Set(),
    Planet -> Set(),
    Asteroid -> Set(),
    InterstellarCloud -> Set(),
    )