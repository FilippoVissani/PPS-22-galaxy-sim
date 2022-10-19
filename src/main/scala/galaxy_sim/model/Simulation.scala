package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationAliases.Time

/** Defines type aliases used in Simulation. */
object SimulationAliases:
  type Time = Double

/** Defines a simulation state.
 * 
 *  @param galaxy current state of the galaxy
 *  @param bounds bounds of the simulation
 *  @param virtualTime virtual time
 *  @param deltaTime delta time used to increment virtual time
 */
case class Simulation(
  galaxy: Map[CelestialBodyType, Set[CelestialBody]],
  bounds: Boundary,
  virtualTime: Time = 0,
  deltaTime: Time,
  )

/** Defines the state of an empty galaxy.*/
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