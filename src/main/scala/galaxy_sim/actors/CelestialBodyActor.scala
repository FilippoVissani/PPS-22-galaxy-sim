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
import galaxy_sim.model.EntityReferenceDetector.*
import galaxy_sim.model.EntityReferenceDetectors.given
import galaxy_sim.model.CelestialBodyType

object CelestialBodyActor:
  sealed trait CelestialBodyActorCommand
  case class GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class CheckCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand

  def apply(
    celestialBody: CelestialBody,
    celestialBodyType: CelestialBodyType,
    bounds: Boundary,
    deltaTime: Double,
    ): Behavior[CelestialBodyActorCommand] =
    Behaviors.setup[CelestialBodyActorCommand](ctx =>
      Behaviors.receiveMessage[CelestialBodyActorCommand](msg => msg match
        case GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) => {
          //ctx.log.debug("Received GetCelestialBodyState")
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          Behaviors.same
        }
        case MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          val ref = getReference(celestialBody, celestialBodies.values.flatMap(x => x).toSet)
          var newCelestialBody = celestialBody.copy()
          newCelestialBody = newCelestialBody.copy(gForceVector = gravitationalForceOnEntity(celestialBody, ref))
          newCelestialBody = newCelestialBody.copy(speedVector = speedVectorAfterTime(newCelestialBody, deltaTime))
          newCelestialBody = newCelestialBody.copy(position = vectorChangeOfDisplacement(newCelestialBody, deltaTime))
          replyTo ! CelestialBodyState(newCelestialBody, celestialBodyType)
          CelestialBodyActor(newCelestialBody, celestialBodyType, bounds, deltaTime)
        }
        case CheckCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          //ctx.log.debug("Received CheckCollisions")
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          Behaviors.same
        }
      )
    )
