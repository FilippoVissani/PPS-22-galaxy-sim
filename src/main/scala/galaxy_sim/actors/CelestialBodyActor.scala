package galaxy_sim.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Lifecycle}
import galaxy_sim.model.MockOrbit.*
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.utils.EntityReferenceDetector.*
import galaxy_sim.utils.EntityReferenceDetectors.given
import galaxy_sim.utils.SimulationGivens.given
import physics.collisions.Collider.*
import physics.collisions.CollisionDetection.CollisionBoxes.CircleCollisionBox
import physics.collisions.CollisionDetection.CollisionCheckers.CircleToCircleChecker
import physics.dynamics.GravitationLaws.*

object CelestialBodyActor:
  sealed trait CelestialBodyActorCommand
  case class GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class UpdateCelestialBodyType(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  case class SolveCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
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
        case UpdateCelestialBodyType(replyTo: ActorRef[SimulationManagerActorCommand]) => {
/*           val result = Lifecycle.entityOneStep(celestialBody, celestialBodyType)
          replyTo ! CelestialBodyState(result._1, result._2)
          CelestialBodyActor(result._1, result._2, bounds, deltaTime) */
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          Behaviors.same
        }
        case MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          val reference = computeEntityReference(celestialBody, celestialBodies.values.flatten.toSet)
          val newCelestialBody = if reference.isEmpty then celestialBody else computeNextPosition(celestialBody, reference.get, deltaTime)
          /* val ref = getReference(celestialBody, celestialBodies.values.flatten.toSet)
          var newCelestialBody = celestialBody.copy()
          if ref.name != celestialBody.name then
            newCelestialBody = newCelestialBody.copy(gForceVector = gravitationalForceOnEntity(newCelestialBody, ref))
            newCelestialBody = newCelestialBody.copy(speedVector = speedVectorAfterTime(newCelestialBody, deltaTime))
            newCelestialBody = newCelestialBody.copy(position = vectorChangeOfDisplacement(newCelestialBody, deltaTime)) */
          replyTo ! CelestialBodyState(newCelestialBody, celestialBodyType)
          CelestialBodyActor(newCelestialBody, celestialBodyType, bounds, deltaTime)
        }
        case SolveCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
/*           ctx.log.debug(celestialBody.name + " " + celestialBodies
          .values
          .flatten
          .filter(x => x != celestialBody)
          .map(x => (x.radius + celestialBody.radius, x.position <-> celestialBody.position)).toString())
          val newCelestialBody = celestialBodies
          .values
          .flatten
          .filter(x => x != celestialBody)
          .map(x => Collider(celestialBody) >< x)
          .filter(x => x != Collider.None)
          .count(x => x != Collider.None()) > 0 then celestialBody.copy(name = "Collided") else celestialBody */
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          CelestialBodyActor(celestialBody, celestialBodyType, bounds, deltaTime)
        }
        case Kill => {
          ctx.log.debug("Kill")
          Behaviors.stopped
        }
      )
    )
