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
import galaxy_sim.model.CelestialBody
import galaxy_sim.model.*
import akka.actor.ActorRef

object Main extends App:
  ActorSystem(RootActor(), "root")
object RootActor:
  sealed trait RootActorCommand

  def apply(): Behavior[RootActorCommand] =
    Behaviors.setup[RootActorCommand](ctx =>
      val celestialBodies: Map[CelestialBodyType, Set[CelestialBody]] = Map(
        MassiveStar -> Set(sun),
        Planet -> Set(earth, moon),
        BlackHole -> Set(blackHole),
      )
      val celestialBodyActors = celestialBodies.map((k, v) => (k, v.map(x => ctx.spawnAnonymous(CelestialBodyActor(x, k, bounds, deltaTime)))))
      val simulationManagerActor = ctx.spawn(SimulationManagerActor(celestialBodyActors, Simulation(celestialBodies = celestialBodies, bounds, 0, deltaTime)), "simulationManager")
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
