package galaxy_sim.model

import physics.Position
import physics.dynamics.PhysicalEntity.changePosition
import CelestialBodyOperations.*
import BodyOperations.*
import galaxy_sim.model.SimulationConfig.{blackHole, bounds, interstellarCloud}
import galaxy_sim.model.SimulationOperations.updateCelestialBodies
import physics.dynamics.GravitationLaws.vectorChangeOfDisplacement
import galaxy_sim.model.SimulationOperations.updateClock

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
      var simulations: Seq[Simulation] = Seq(Simulation(celestialBodies = Set(blackHole, interstellarCloud), bounds = bounds))

      override def simulation: Simulation = simulations.head

      override def addCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(x => x + celestialBody) +: simulations

      override def removeCelestialBody(celestialBody: CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(x => x.filter(y => y != celestialBody)) +: simulations

      override def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit =
        simulations = simulations.head.updateCelestialBodies(x => x.map(y => if y == celestialBody then f(y) else y)) +: simulations

      override def moveCelestialBodiesToNextPosition(): Unit =
        simulations = simulations.head.updateCelestialBodies(x => x.map(y => y.updateBody(z => z.updatePhysicalEntity(w => changePosition(w, vectorChangeOfDisplacement(w, simulations.head.deltaTime)))))) +: simulations

      override def incrementVirtualTime(): Unit =
        simulations = simulations.head.updateClock(x => x.incrementVirtualTime()) +: simulations

  trait Interface extends Provider with Component
