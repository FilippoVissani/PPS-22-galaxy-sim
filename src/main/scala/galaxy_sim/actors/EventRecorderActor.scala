package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.model.{CelestialBody, CelestialBodyType}

object EventRecorderActor:

  /** Defines the messages that can be sent to EventRecorderActor. */
  sealed trait EventRecorderActorCommand

  /** Records a collision between two celestial bodies
   *
   * @param celestialBody1 first celestial body involved in the collision
   * @param celestialBody2 second celestial body involved in the collision
   */
  case class RecordCollision(celestialBody1: CelestialBody, celestialBody2: CelestialBody) extends  EventRecorderActorCommand

  /** Records the spawn of a celestial body
   *
   * @param celestialBody spawned celestial body
   */
  case class RecordSpawn(celestialBody: CelestialBody) extends EventRecorderActorCommand

  /** Records the death of a celestial body
   *
   * @param celestialBody died celestial body
   */
  case class RecordDeath(celestialBody: CelestialBody) extends EventRecorderActorCommand

  /** Ask pattern used to get events
   *
   * @param replyTo response of the question
   */
  case class AskRecordedEvents(replyTo: ActorRef[RecordedEventsResponse]) extends EventRecorderActorCommand

  /** Response of the Ask pattern
   *
   * @param recordedEvents events
   */
  case class RecordedEventsResponse(recordedEvents: List[String])

  /** Creates a EventRecorderActor
   *
   * @param recordedEvents list of recorded events
   */
  def apply(recordedEvents: List[String] = List()): Behavior[EventRecorderActorCommand] =
    Behaviors.setup[EventRecorderActorCommand](_ =>
      Behaviors.receiveMessage[EventRecorderActorCommand](msg => msg match
        case RecordCollision(celestialBody1: CelestialBody, celestialBody2: CelestialBody) => {
          EventRecorderActor(recordedEvents :+ s"${celestialBody1.name} collided with ${celestialBody2.name}")
        }
        case RecordSpawn(celestialBody: CelestialBody) => {
          EventRecorderActor(recordedEvents :+ s"${celestialBody.name} spawned")
        }
        case RecordDeath(celestialBody: CelestialBody) => {
          EventRecorderActor(recordedEvents :+ s"${celestialBody.name} died")
        }
        case AskRecordedEvents(replyTo: ActorRef[RecordedEventsResponse]) => {
          replyTo ! RecordedEventsResponse(recordedEvents)
          Behaviors.same
        }
      )
    )