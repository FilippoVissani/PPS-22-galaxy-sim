package galaxy_sim.actors

import akka.actor.typed.SpawnProtocol.Spawn
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.actors.CelestialBodyActor.{AskBodyInfo, AskBodyName, CelestialBodyActorCommand}
import galaxy_sim.actors.ViewActor.{LoggerMessage, SetBodyInfo, ViewActorCommand}
import galaxy_sim.model.CelestialBody
import galaxy_sim.actors.LoggerActions

import java.awt.Component.{BOTTOM_ALIGNMENT, BaselineResizeBehavior}
import scala.util.{Failure, Success}
import scala.language.postfixOps


enum LoggerActions:
//  case Spawn
  case Collided
  case Died

object LoggerActor:
  sealed trait LoggerActorCommand
  case class UpdateLogger(bodies: (CelestialBody, Option[CelestialBody]), description: LoggerActions) extends LoggerActorCommand
  case class UpdateBody(celestialBody: CelestialBody) extends LoggerActorCommand
  case class BodyInfo(bodyName: String) extends LoggerActorCommand
  case class BodiesNames() extends LoggerActorCommand
  case class SpawnedBody(celestialBody: ActorRef[CelestialBodyActorCommand]) extends LoggerActorCommand
  case class SetViewActor(viewActor: ActorRef[ViewActorCommand]) extends LoggerActorCommand
  case class BodyInfoResponse(info: CelestialBody) extends LoggerActorCommand

  case object Ask extends LoggerActorCommand

  def apply(
      viewActorRef: Option[ActorRef[ViewActorCommand]] = Option.empty,
      celestialBodyActorRefs: Option[Set[ActorRef[CelestialBodyActorCommand]]] = Option.empty
           ): Behavior[LoggerActorCommand] =
    Behaviors.setup(ctx =>
      Behaviors.receiveMessage(msg => msg match
        case SpawnedBody(celestialBody) => {
          if celestialBodyActorRefs.isDefined then LoggerActor(viewActorRef, Option(celestialBodyActorRefs.get + celestialBody))
          else LoggerActor(viewActorRef, Option(Set(celestialBody)))
        }
        case SetViewActor(viewActor) => LoggerActor(Option(viewActor), celestialBodyActorRefs)
        case UpdateLogger(bodies: (CelestialBody, Option[CelestialBody]), description: LoggerActions) => {
          if viewActorRef.isDefined then
            viewActorRef.get ! LoggerMessage(bodies, description)
          Behaviors.same
        }
        case BodyInfoResponse(info) => {
          viewActorRef.get ! SetBodyInfo(info)
          Behaviors.same
        }
        case BodyInfo(bodyName) => {
          if celestialBodyActorRefs.isDefined then
            celestialBodyActorRefs.get.foreach(b =>
              b ! AskBodyInfo(bodyName)
            )
          Behaviors.same
        }
        case BodiesNames() => {
          if celestialBodyActorRefs.isDefined then
            celestialBodyActorRefs.get.foreach(b =>
              b ! AskBodyName(viewActorRef.get)
            )
          Behaviors.same
        }
      )
    )