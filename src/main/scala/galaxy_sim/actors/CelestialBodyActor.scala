package galaxy_sim.actors

import galaxy_sim.model.CelestialBody
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.model.Boundary
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.SimulationManagerActorCommand
import galaxy_sim.actors.SimulationManagerActor.SimulationManagerActorCommand.*
import physics.dynamics.GravitationLaws.*
import galaxy_sim.model.SimulationConfig.*
import akka.actor.typed.ActorRef

object CelestialBodyActor:
  enum CelestialBodyActorCommand:
    case AskMoveToNextPosition(replyTo: ActorRef[MoveToNextPositionResponse])
    case CheckCollisions(otherBodies: Set[CelestialBody])

  case class MoveToNextPositionResponse(celestialBody: CelestialBody)

  export CelestialBodyActorCommand.*

  def apply(celestialBody: CelestialBody, bounds: Boundary, deltaTime: Double): Behavior[CelestialBodyActorCommand] =
    Behaviors.setup[CelestialBodyActorCommand](ctx =>
      Behaviors.receiveMessage[CelestialBodyActorCommand](msg => msg match
        case AskMoveToNextPosition(replyTo: ActorRef[MoveToNextPositionResponse]) => {
          //todo: in this message is needed the set of celestial bodies
          ctx.log.debug("Received AskMoveToNextPosition")
          val newCelestialBody = if celestialBody.name.contains("Cloud") then celestialBody.copy(gForceVector = gravitationalForceOnEntity(celestialBody, blackHole), speedVector = speedVectorAfterTime(celestialBody, deltaTime), position = vectorChangeOfDisplacement(celestialBody, deltaTime)) else celestialBody
          replyTo ! MoveToNextPositionResponse(newCelestialBody)
          CelestialBodyActor(newCelestialBody, bounds, deltaTime)
        }
        case CheckCollisions(otherBodies: Set[CelestialBody]) => {
          ctx.log.debug("Received CheckCollisions")
          Behaviors.same
        }
      )
    )
