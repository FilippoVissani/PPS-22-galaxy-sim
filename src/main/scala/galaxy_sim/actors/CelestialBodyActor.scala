package galaxy_sim.actors

import galaxy_sim.model.CelestialBody
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.model.Boundary
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import physics.dynamics.GravitationLaws.*
import galaxy_sim.model.SimulationConfig.*
import akka.actor.typed.ActorRef
import galaxy_sim.actors.SimulationManagerActor.*

object CelestialBodyActor:
  sealed trait CelestialBodyActorCommand
  case class GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class MoveToNextPosition(celestialBodies: Set[CelestialBody], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class CheckCollisions(celestialBodies: Set[CelestialBody], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand

  def apply(celestialBody: CelestialBody, bounds: Boundary, deltaTime: Double): Behavior[CelestialBodyActorCommand] =
    Behaviors.setup[CelestialBodyActorCommand](ctx =>
      Behaviors.receiveMessage[CelestialBodyActorCommand](msg => msg match
        case GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) => {
          //ctx.log.debug("Received GetCelestialBodyState")
          replyTo ! CelestialBodyState(celestialBody)
          Behaviors.same
        }
        case MoveToNextPosition(celestialBodies: Set[CelestialBody], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          //ctx.log.debug("Received AskMoveToNextPosition")
          val newCelestialBody = if celestialBody.name.contains("Cloud") then celestialBody.copy(gForceVector = gravitationalForceOnEntity(celestialBody, blackHole), speedVector = speedVectorAfterTime(celestialBody, deltaTime), position = vectorChangeOfDisplacement(celestialBody, deltaTime)) else celestialBody
          replyTo ! CelestialBodyState(newCelestialBody)
          CelestialBodyActor(newCelestialBody, bounds, deltaTime)
        }
        case CheckCollisions(celestialBodies: Set[CelestialBody], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          //ctx.log.debug("Received CheckCollisions")
          replyTo ! CelestialBodyState(celestialBody)
          Behaviors.same
        }
      )
    )
