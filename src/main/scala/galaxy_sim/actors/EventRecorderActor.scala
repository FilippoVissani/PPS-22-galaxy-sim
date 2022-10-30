package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.model.{CelestialBody, CelestialBodyType}

object EventRecorderActor:
  sealed trait EventRecorderActorCommand

  case class RecordCollision(celestialBody1: CelestialBody, celestialBody2: CelestialBody) extends  EventRecorderActorCommand

  case class RecordSpawn(celestialBody: CelestialBody) extends EventRecorderActorCommand

  case class RecordDeath(celestialBody: CelestialBody) extends EventRecorderActorCommand

  case class AskRecordedEvents(replyTo: ActorRef[RecordedEventsResponse]) extends EventRecorderActorCommand

  case class RecordedEventsResponse(recordedEvents: String)

  def apply(recordedEvents: String = ""): Behavior[EventRecorderActorCommand] =
    Behaviors.setup[EventRecorderActorCommand](_ =>
      Behaviors.receiveMessage[EventRecorderActorCommand](msg => msg match
        case RecordCollision(celestialBody1: CelestialBody, celestialBody2: CelestialBody) => {
          EventRecorderActor(recordedEvents + s"${celestialBody1.name} collided with ${celestialBody2.name}\n")
        }
        case RecordSpawn(celestialBody: CelestialBody) => {
          EventRecorderActor(recordedEvents + s"${celestialBody.name} spawned\n")
        }
        case RecordDeath(celestialBody: CelestialBody) => {
          EventRecorderActor(recordedEvents + s"${celestialBody.name} died\n")
        }
        case AskRecordedEvents(replyTo: ActorRef[RecordedEventsResponse]) => {
          replyTo ! RecordedEventsResponse(recordedEvents)
          Behaviors.same
        }
      )
    )