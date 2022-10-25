package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.actors.ViewActor.{LoggerMessage, ViewActorCommand}
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Lifecycle}
import galaxy_sim.utils.EntityReferenceDetector.*
import galaxy_sim.utils.EntityReferenceDetectors.given
import physics.rigidbody.CollisionBoxes.CircleCollisionBox
import physics.dynamics.GravitationLaws.*
import galaxy_sim.utils.SimulationGivens.given
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.LoggerActions.*
import physics.collisions.collision.CollisionEngine

/** In this object is defined the behaviour of celestial body actor.
 *
 * A CelestialBodyActor manages the behaviour of a single CelestialBody.
 * This actor is designed to communicate only with SimulationManagerActor.
 */
object CelestialBodyActor:

  /** Defines the messages that can be sent to CelestialBodyActor. */
  sealed trait CelestialBodyActorCommand

  /** Requests the current CelestialBody state.
   * 
   *  @param replyTo the SimulationManagerActor reference to reply.
  */
  case class GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  
  /** Updates CelestialBody stats and type.
   * 
   *  @param replyTo the SimulationManagerActor reference to reply.
  */
  case class UpdateCelestialBodyType(replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  
  /** Moves the CelestialBody to the next position.
   * 
   *  @param celestialBodies current state of the other celestial bodies.
   *  @param replyTo the SimulationManagerActor reference to reply.
  */
  case class MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand
  
  /** Solve collisions with other celestial bodies.
   * 
   *  @param celestialBodies current state of the other celestial bodies.
   *  @param replyTo the SimulationManagerActor reference to reply.
  */
  case class SolveCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) extends CelestialBodyActorCommand

  /**
   * Message to pass the reference to viewActor to the celestial body actor
   * @param viewActor reference of the ViewActor
   */
  case class GreetFromView(viewActor: ActorRef[ViewActorCommand]) extends CelestialBodyActorCommand
  /** Kills this actor. */
  case object Kill extends CelestialBodyActorCommand

  /** Creates a CelestialBodyActor.
   *
   *  @param celestialBody current state of the CelestialBody.
   *  @param celestialBodyType current type of the CelestialBody.
   *  @param bounds bounds that define the limits of the movement of the body.
   *  @param deltaTime used to compute next position every time.
   */
  def apply(
    celestialBody: CelestialBody,
    celestialBodyType: CelestialBodyType,
    bounds: Boundary,
    deltaTime: Double,
    ): Behavior[CelestialBodyActorCommand] =
    Behaviors.setup[CelestialBodyActorCommand](ctx =>
      var viewActorRef: Option[ActorRef[ViewActorCommand]] = Option.empty
      Behaviors.receiveMessage[CelestialBodyActorCommand](msg => msg match
        case GetCelestialBodyState(replyTo: ActorRef[SimulationManagerActorCommand]) => {
          replyTo ! CelestialBodyState(celestialBody, celestialBodyType)
          Behaviors.same
        }
        case UpdateCelestialBodyType(replyTo: ActorRef[SimulationManagerActorCommand]) => {
          val result = Lifecycle.entityOneStep(celestialBody, celestialBodyType)
          replyTo ! CelestialBodyState(result._1, result._2)
          CelestialBodyActor(result._1, result._2, bounds, deltaTime)
        }
        case MoveToNextPosition(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          val reference = getReference(celestialBody, celestialBodies.values.flatten.toSet)
          val newCelestialBody = if reference.isEmpty then celestialBody else Seq(celestialBody)
            .filter(x => x != reference.get)
            .map(x => x.copy(gForceVector = gravitationalForceOnEntity(x, reference.get)))
            .map(x => x.copy(speedVector = speedVectorAfterTime(x, deltaTime)))
            .map(x => x.copy(position = vectorChangeOfDisplacement(x, deltaTime)))
            .map(x => x.copy(position = bounds.toToroidal(x.position)))
            .head
            replyTo ! CelestialBodyState(newCelestialBody, celestialBodyType)
            CelestialBodyActor(newCelestialBody, celestialBodyType, bounds, deltaTime)
        }
        case GreetFromView(viewActor) => {
          viewActorRef = Option(viewActor)
          if viewActorRef.nonEmpty then
            viewActorRef.get ! LoggerMessage((celestialBody, Option.empty), Spawn)
          Behaviors.same
        }
        case SolveCollisions(celestialBodies: Map[CelestialBodyType, Set[CelestialBody]], replyTo: ActorRef[SimulationManagerActorCommand]) => {
          val others = celestialBodies.values.flatten.filter(x => x != celestialBody)
          val newCelestialBody = CollisionEngine.impactMany(celestialBody, others.toSeq)
          if viewActorRef.nonEmpty then
            viewActorRef.get ! LoggerMessage((celestialBody, Option.empty), Collided) //todo
          replyTo ! CelestialBodyState(newCelestialBody, celestialBodyType)
          CelestialBodyActor(newCelestialBody, celestialBodyType, bounds, deltaTime)
        }
        case Kill => {
          if viewActorRef.nonEmpty then
            viewActorRef.get ! LoggerMessage((celestialBody, Option.empty), Died)
          Behaviors.stopped
        }
      )
    )
