package galaxy_sim

import akka.actor.ActorRef
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.{CelestialBodyActor, ControllerActor, EventRecorderActor, SimulationManagerActor, ViewActor}
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
        BlackHole -> Set(blackHole),
        Planet -> Set(body02, body03, body04, body05, body06, body07, body08, body09, body10),
        InterstellarCloud -> groupOFInterstellarClouds(100)
      )
      val eventRecorderActor = ctx.spawn(EventRecorderActor(), "EventRecorderActor")
      val celestialBodyActors = galaxy
      .map((k, v) => (k, v.map(x => ctx.spawnAnonymous(CelestialBodyActor(x, k, bounds, deltaTime, eventRecorderActor)))))
      .values
      .flatten
      .toSet
      val simulationManagerActor = ctx.spawn(SimulationManagerActor(celestialBodyActors, Simulation(galaxy = galaxy, bounds, 0, deltaTime)), "SimulationManagerActor")
      val controllerActor = ctx.spawn(ControllerActor(Option.empty, simulationManagerActor, eventRecorderActor), "ControllerActor")
      val viewActor = ctx.spawn(ViewActor(controllerActor), "ViewActor")
      controllerActor ! SetView(viewActor)
      Behaviors.receive[RootActorCommand]((_, msg) => msg match
        case _ => {
          Behaviors.stopped
        }
      ).receiveSignal { case (_, Terminated(_)) =>
        Behaviors.stopped
      }
    )
