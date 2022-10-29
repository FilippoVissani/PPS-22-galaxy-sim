package galaxy_sim

import akka.actor.ActorRef
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.LoggerActor.*
import galaxy_sim.actors.ViewActor.LoggerMessage
import galaxy_sim.actors.{CelestialBodyActor, ControllerActor, LoggerActor, SimulationManagerActor, ViewActor}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation, emptyGalaxy}

import scala.language.postfixOps

object Main extends App:
  ActorSystem(RootActor(), "root")
object RootActor:
  sealed trait RootActorCommand

  def apply(): Behavior[RootActorCommand] =
    Behaviors.setup[RootActorCommand](ctx =>
/*
MassiveStar -> Set(body01),
*/
      val galaxy = emptyGalaxy ++ Map(
        BlackHole -> Set(blackHole),
        Planet -> Set(body02, body03, body04, body05, body06, body07, body08, body09, body10),
        InterstellarCloud -> groupOFInterstellarClouds(100)
      )
      //todo spawn logger actor
      val loggerActor = ctx.spawn(LoggerActor(), "logger")
      val celestialBodyActors = galaxy
      .map((k, v) => (k, v.map(x => ctx.spawnAnonymous(CelestialBodyActor(x, k, bounds, deltaTime, loggerActor))))) //todo pass logger actor
      .values
      .flatten
      .toSet
      celestialBodyActors.foreach( x => loggerActor ! SpawnedBody(x))
      val simulationManagerActor = ctx.spawn(SimulationManagerActor(celestialBodyActors, Simulation(galaxy = galaxy, bounds, 0, deltaTime)), "simulationManager")
      val controllerActor = ctx.spawn(ControllerActor(Option.empty, simulationManagerActor), "controller")
      val viewActor = ctx.spawn(ViewActor(controllerActor, loggerActor), "view") //todo pass logger actor
      controllerActor ! SetView(viewActor, galaxy)
      loggerActor ! SetViewActor(viewActor)
      Behaviors.receive[RootActorCommand]((ctx, msg) => msg match
        case _ => {
          Behaviors.stopped
        }
      ).receiveSignal { case (ctx, Terminated(_)) =>
        Behaviors.stopped
      }
    )
