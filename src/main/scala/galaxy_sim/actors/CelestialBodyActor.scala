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
import galaxy_sim.utils.EntityReferenceDetector.*
import galaxy_sim.utils.EntityReferenceDetectors.given
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.MockOrbit.*
import physics.collisions.Collider.*
import physics.collisions.CollisionDetection.CollisionBoxes.CircleCollisionBox
import physics.collisions.CollisionDetection.CollisionCheckers.CircleToCircleChecker
import galaxy_sim.utils.SimulationGivens.given

object CelestialBodyActor:
  sealed trait CelestialBodyActorCommand
  case class GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class CheckCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case object Kill extends CelestialBodyActorCommand

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
          /* val reference = computeEntityReference(celestialBody, celestialBodies.values.flatten.toSet)
          val newCelestialBody = if reference.isEmpty then celestialBody else computeNextPosition(celestialBody, reference.get, deltaTime) */
          val ref = getReference(celestialBody, celestialBodies.values.flatten.toSet)
          var newCelestialBody = celestialBody.copy()
          if ref.name != celestialBody.name then
            newCelestialBody = newCelestialBody.copy(gForceVector = gravitationalForceOnEntity(newCelestialBody, ref))
            newCelestialBody = newCelestialBody.copy(speedVector = speedVectorAfterTime(newCelestialBody, deltaTime))
            newCelestialBody = newCelestialBody.copy(position = vectorChangeOfDisplacement(newCelestialBody, deltaTime))
          replyTo ! CelestialBodyState(newCelestialBody, celestialBodyType)
          CelestialBodyActor(newCelestialBody, celestialBodyType, bounds, deltaTime)
        }
        case CheckCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
/*           ctx.log.debug(celestialBodies
          .values
          .flatten
          .filter(x => x != celestialBody)
          .map(x => Collider(celestialBody) % x).toString())
          val newCelestialBody = if celestialBodies
          .values
          .flatten
          .filter(x => x != celestialBody)
          .map(x => Collider(celestialBody) % x)
          .count(x => x != Collider.None()) > 0 then celestialBody.copy(name = "Collided") else celestialBody */
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          CelestialBodyActor(celestialBody,celestialBodyType,bounds,deltaTime)
        }
        case Kill => {
          ctx.log.debug("Kill")
          Behaviors.stopped
        }
      )
    )
