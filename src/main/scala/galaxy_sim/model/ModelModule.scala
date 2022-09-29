package galaxy_sim.model

import physics.Position
import physics.dynamics.PhysicalEntity.changePosition
import galaxy_sim.model.SimulationConfig.{blackHole, bounds, interstellarCloud}
import physics.dynamics.GravitationLaws.{gravitationalForceOnEntity, vectorChangeOfDisplacement}
import physics.dynamics.PhysicalEntity
import physics.Pair.PairOperations.given

object ModelModule:
  trait Model:
    def simulation: Simulation
    def removeCelestialBody(celestialBody: CelestialBody): Unit
    def addCelestialBody(celestialBody: CelestialBody): Unit
    def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit
    def moveCelestialBodiesToNextPosition(): Unit
    def incrementVirtualTime(): Unit

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      var simulations: Seq[Simulation] = Seq(
        Simulation(celestialBodies = Set(blackHole, interstellarCloud),
          bounds = bounds))

      override def simulation: Simulation = simulations.head

      override def addCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulation.copy(celestialBodies = simulation.celestialBodies + celestialBody) +: simulations

      override def removeCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulation.copy(celestialBodies = simulation.celestialBodies.filter(x => x != celestialBody)) +: simulations

      override def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit =
        simulations = simulation.copy(celestialBodies = simulation.celestialBodies.map(x => if x == celestialBody then f(x) else x)) +: simulations

      override def moveCelestialBodiesToNextPosition(): Unit =
        simulations = simulation.copy(celestialBodies = simulation.celestialBodies.map(x => x.copy(position = vectorChangeOfDisplacement(x, simulation.deltaTime)))) +: simulations

      override def incrementVirtualTime(): Unit =
        simulations = simulation.copy(virtualTime = simulation.virtualTime + simulation.deltaTime) +: simulations

  trait Interface extends Provider with Component
