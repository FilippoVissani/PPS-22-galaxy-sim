package galaxy_sim

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Terminated
import galaxy_sim.actors.ControllerActor
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.CelestialBodyActor
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.actors.ViewActor
import galaxy_sim.actors.SimulationManagerActor
import galaxy_sim.model.Simulation

object Main extends App:
  ActorSystem(RootActor(), "root")
object RootActor:
  sealed trait RootActorCommand

  def apply(): Behavior[RootActorCommand] =
    Behaviors.setup[RootActorCommand](ctx =>
      var count = 0
      val celestialBodies = Set(blackHole) ++ groupOFInterstellarClouds(1000)
      val celestialBodyActors = celestialBodies.map(x => {
        count = count + 1
        ctx.spawn(CelestialBodyActor(x, bounds, deltaTime), s"celestialBody$count")
      })
      val simulationManagerActor = ctx.spawn(SimulationManagerActor(celestialBodyActors, Simulation(celestialBodies = celestialBodies, bounds, 0, 1000)), "simulationManager")
      val controllerActor = ctx.spawn(ControllerActor(Option.empty, simulationManagerActor), "controller")
      val viewActor = ctx.spawn(ViewActor(controllerActor), "view")
      controllerActor ! SetView(viewActor)
      Behaviors.receive[RootActorCommand]((ctx, msg) => msg match
        case _ => {
          Behaviors.stopped
        }
      ).receiveSignal { case (ctx, Terminated(_)) =>
        ctx.log.debug("Stopping actor system")
        Behaviors.stopped
      }
    )
