package galaxy_sim.model

import physics.Position
import galaxy_sim.model.SimulationConfig.{blackHole, bounds, interstellarCloud}
import physics.dynamics.GravitationLaws.{astronomicUnit, daySec, gravitationalForceOnEntity, speedVectorAfterTime, vectorChangeOfDisplacement}
import physics.dynamics.PhysicalEntity
import physics.Pair.PairOperations.given

object ModelModule:
  trait Model:
    def simulation: Simulation
    def removeCelestialBody(celestialBody: CelestialBody): Unit
    def addCelestialBody(celestialBody: CelestialBody): Unit
    def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit
    def moveCelestialBodiesToNextPosition(): Unit
    def resolveCollisions(): Unit
    def incrementVirtualTime(): Unit

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      var actualSimulation: Simulation = Simulation(celestialBodies = Set(blackHole, interstellarCloud), bounds = bounds, deltaTime = 1000)

      override def simulation: Simulation = actualSimulation

      override def addCelestialBody(celestialBody: CelestialBody): Unit =
        actualSimulation = actualSimulation.copy(celestialBodies = simulation.celestialBodies + celestialBody)

      override def removeCelestialBody(celestialBody: CelestialBody): Unit =
        actualSimulation = actualSimulation.copy(celestialBodies = simulation.celestialBodies.filter(x => x != celestialBody))

      override def updateCelestialBody(celestialBody: CelestialBody)(f: CelestialBody => CelestialBody): Unit =
        actualSimulation = actualSimulation.copy(celestialBodies = simulation.celestialBodies.map(x => if x == celestialBody then f(x) else x))

      override def moveCelestialBodiesToNextPosition(): Unit =
        val bh = simulation.celestialBodies.filter(x => x.name == blackHole.name).head
        actualSimulation = actualSimulation.copy(celestialBodies = simulation.celestialBodies.map(x => {
          var test = x
          if x.name == interstellarCloud.name then
            test = x.copy(gForceVector = gravitationalForceOnEntity(x, bh))
            test = test.copy(speedVector = speedVectorAfterTime(x, simulation.deltaTime))
            test = test.copy(position = vectorChangeOfDisplacement(x, simulation.deltaTime))
          test
        }))

      override def resolveCollisions(): Unit = ???

      override def incrementVirtualTime(): Unit =
        actualSimulation = actualSimulation.copy(virtualTime = simulation.virtualTime + simulation.deltaTime)

  trait Interface extends Provider with Component
