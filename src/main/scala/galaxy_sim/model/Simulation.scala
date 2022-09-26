package galaxy_sim.model

trait Simulation:
  def celestialBodies: Seq[CelestialBody]
  def bounds: Boundary
  val clock: Clock
  export clock.*

object Simulation:
  def apply(celestialBodies: Seq[CelestialBody] = Seq(), bounds: Boundary, clock: Clock = Clock()): Simulation =
    SimulationImpl(celestialBodies, bounds, clock)

  private case class SimulationImpl(override val celestialBodies: Seq[CelestialBody],
                                    override val bounds: Boundary,
                                    override val clock: Clock) extends Simulation

object SimulationOperations:
  extension (s: Simulation)
    def updateCelestialBodies(f: Seq[CelestialBody] => Seq[CelestialBody]): Simulation =
      Simulation(f(s.celestialBodies), s.bounds, s.clock)
