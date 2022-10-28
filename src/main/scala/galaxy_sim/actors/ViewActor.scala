package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.SimulationManagerActor.GreetCelestialBody
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Simulation}
import galaxy_sim.utils.LoggerActions
import galaxy_sim.view.{SwingGUI, View}

/** In this object is defined the behaviour of view actor.
 *
 * A ViewActor manages the view.
 * This actor is designed to communicate only with ControllerActor.
 */
object ViewActor:

  /** Defines the messages that can be sent to ViewActor. */
  sealed trait ViewActorCommand

   /** Displays the current state of the simulation.
    *
    * This message should be sent from ControllerActor.
    *
    *  @param simulation current state of the simulation.
    */
  case class Display(simulation: Simulation) extends ViewActorCommand

  /**
   * Call to update the galaxy infos
   *
   * @param galaxy current state of the galaxy
   */
  case class SetGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]) extends ViewActorCommand

  /**
   * Call to send the logger a new event
   *
   * @param body pair of Celestial Bodies involved, the second one is Option
   * @param description    enum to describe what happened
   */
  case class LoggerMessage(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions) extends ViewActorCommand

  //todo
  case class UpdateBody(celestialBody: CelestialBody) extends ViewActorCommand
  /** Sent from the view when start button is pressed.
    *
    * This message should be sent from the view.
    */
  case object StartPressed extends ViewActorCommand

  /** Sent from the view when stop button is pressed.
    *
    * This message should be sent from the view.
    */
  case object StopPressed extends ViewActorCommand

  /** Creates a ViewActor.
   *
   *  @param controllerActor to communicate with.
   */
  def apply(controllerActor: ActorRef[ControllerActorCommand]): Behavior[ViewActorCommand] =
    val percentSize = 90
    Behaviors.setup[ViewActorCommand](ctx =>
      val view = View(ctx.self, percentSize, percentSize)
      Behaviors.receiveMessage[ViewActorCommand](msg => msg match
        case Display(simulation: Simulation) => {
          view.display(simulation)
          Behaviors.same
        }
        case LoggerMessage(bodiesInvolved, description) => {
          view.sendLogger(bodiesInvolved, description)
          Behaviors.same
        }
        case SetGalaxy(galaxy) => {
          view.setGalaxy(galaxy)
          Behaviors.same
        }
        case UpdateBody(celestialBody) => {
          view.updateBody(celestialBody)
          Behaviors.same
        }
        case StartPressed => {
          view.updateInfos()
          controllerActor ! Start
          Behaviors.same
        }
        case StopPressed => {
          controllerActor ! Stop
          Behaviors.same
        }
      )
    )