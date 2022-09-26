package galaxy_sim.model

import physics.Position
import physics.dynamics.PhysicalEntity.changePosition
import SimulationOperations.*
import CelestialBodyOperations.*

object ModelModule:
  trait Model:
    def simulation: Simulation
    def removeCelestialBody(celestialBody: CelestialBody): Unit
    def addCelestialBody(celestialBody: CelestialBody): Unit
    def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      var simulations: Seq[Simulation] = Seq(Simulation(
        celestialBodies = (0 to 9) map (_ => CelestialBodyGenerator.generateRandomCelestialBody(150)),
        bounds = Boundary(100, 100, 100, 100)))

      override def simulation: Simulation = simulations.head

      override def addCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(c => celestialBody +: c) +: simulations

      override def removeCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(c => c.filter(b => b != celestialBody)) +: simulations

      override def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(c => c.map(b => if b == celestialBody then f(b) else b)) +: simulations

  trait Interface extends Provider with Component
