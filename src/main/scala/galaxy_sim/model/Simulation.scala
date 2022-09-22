package galaxy_sim.model

trait Simulation:
  def virtualTime: Double
  def deltaTime: Double
  def celestialBodies: Seq[CelestialBody]
  def incrementVirtualTime(): Simulation
  def updateCelestialBodies(celestialBodies: Seq[CelestialBody]): Simulation

object Simulation:
  def apply(celestialBodies: Seq[CelestialBody], virtualTime: Double, deltaTime: Double): Simulation =
    SimulationImpl(celestialBodies, virtualTime, deltaTime)
  
  private case class SimulationImpl(override val celestialBodies: Seq[CelestialBody],
                                    override val virtualTime: Double,
                                    override val deltaTime: Double) extends Simulation:

    override def incrementVirtualTime(): Simulation =
      Simulation(celestialBodies, virtualTime + deltaTime, deltaTime)

    override def updateCelestialBodies(celestialBodies: Seq[CelestialBody]): Simulation =
      Simulation(celestialBodies, virtualTime, deltaTime)
