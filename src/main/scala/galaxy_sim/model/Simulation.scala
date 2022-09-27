package galaxy_sim.model

trait Simulation:
  def celestialBodies: Set[CelestialBody]
  def bounds: Boundary
  val clock: Clock
  export clock.*

object Simulation:
  def apply(celestialBodies: Set[CelestialBody] = Set(), bounds: Boundary, clock: Clock = Clock()): Simulation =
    SimulationImpl(celestialBodies, bounds, clock)

  private case class SimulationImpl(override val celestialBodies: Set[CelestialBody],
                                    override val bounds: Boundary,
                                    override val clock: Clock) extends Simulation

object SimulationOperations:
  extension (s: Simulation)
    def updateCelestialBodies(f: Set[CelestialBody] => Set[CelestialBody]): Simulation =
      Simulation(f(s.celestialBodies), s.bounds, s.clock)
