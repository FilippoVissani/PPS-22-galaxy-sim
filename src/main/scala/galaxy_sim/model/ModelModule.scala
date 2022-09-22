package galaxy_sim.model

import physics.Position
import physics.dynamics.PhysicalEntity.changePosition

object ModelModule:
  trait Model:
    def virtualTime: Double
    def celestialBodies: Seq[CelestialBody]
    def moveCelestialBody(celestialBody: CelestialBody)(f: Position => Position): Unit
    def removeCelestialBody(celestialBody: CelestialBody): Unit
    def addCelestialBody(celestialBody: CelestialBody): Unit

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      var simulation: Simulation = Simulation((0 to 9) map (_ => CelestialBodyGenerator.generateRandomCelestialBody(150)), 0, 0.1)

      override def virtualTime: Double =
        simulation.virtualTime

      override def celestialBodies: Seq[CelestialBody] =
        simulation.celestialBodies

      override def moveCelestialBody(celestialBody: CelestialBody)(f: Position => Position): Unit =
        simulation = Simulation(
          simulation.celestialBodies.map(b => if b == celestialBody then CelestialBody(b.name, b.radius, b.birthTime, changePosition(b.body, f(b.body.position))) else b),
          simulation.virtualTime,
          simulation.deltaTime
        )

      override def addCelestialBody(celestialBody: CelestialBody): Unit =
        simulation = Simulation(simulation.celestialBodies :+ celestialBody, simulation.virtualTime, simulation.deltaTime)

      override def removeCelestialBody(celestialBody: CelestialBody): Unit =
        simulation = Simulation(simulation.celestialBodies.filter(b => b != celestialBody), simulation.virtualTime, simulation.deltaTime)

  trait Interface extends Provider with Component
