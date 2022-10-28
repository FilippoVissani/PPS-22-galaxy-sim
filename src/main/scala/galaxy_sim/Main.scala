package galaxy_sim

import akka.actor.ActorRef
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.{CelestialBodyActor, ControllerActor, SimulationManagerActor, ViewActor}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation, emptyGalaxy}

object Main extends App:
  ActorSystem(RootActor(), "root")
object RootActor:
  sealed trait RootActorCommand

  def apply(): Behavior[RootActorCommand] =
    Behaviors.setup[RootActorCommand](ctx =>
      val galaxy = emptyGalaxy ++ Map(
        MassiveStar -> Set(sun),
        Planet -> Set(earth, earth2),
      )
      val celestialBodyActors = galaxy
      .map((k, v) => (k, v.map(x => ctx.spawnAnonymous(CelestialBodyActor(x, k, bounds, deltaTime)))))
      .values
      .flatten
      .toSet
      val simulationManagerActor = ctx.spawn(SimulationManagerActor(celestialBodyActors, Simulation(galaxy = galaxy, bounds, 0, deltaTime)), "simulationManager")
      val controllerActor = ctx.spawn(ControllerActor(Option.empty, simulationManagerActor), "controller")
      val viewActor = ctx.spawn(ViewActor(controllerActor), "view")
      controllerActor ! SetView(viewActor)
      Behaviors.receive[RootActorCommand]((_, msg) => msg match
        case _ => {
          Behaviors.stopped
        }
      ).receiveSignal { case (_, Terminated(_)) =>
        Behaviors.stopped
      }
    )
