package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.model.{Boundary, CelestialBody, Simulation}
import galaxy_sim.view.{SwingGUI, View}
import galaxy_sim.model.SimulationConfig.windowSize

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
  case class DisplaySimulation(simulation: Simulation) extends ViewActorCommand

  /** Displays the events on the View.
   *
   * This message should be sent from ControllerActor.
   *
   * @param events events to display.
   */
  case class DisplayEvents(events: List[String]) extends ViewActorCommand

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
    Behaviors.setup[ViewActorCommand](ctx =>
      val view = View(ctx.self, windowSize, windowSize)

      Behaviors.receiveMessage[ViewActorCommand](msg => msg match
        case DisplaySimulation(simulation: Simulation) => {
          view.displaySimulation(simulation)
          Behaviors.same
        }
        case DisplayEvents(events: List[String]) => {
          view.displayEvents(events)
          Behaviors.same
        }
        case StartPressed => {
          controllerActor ! Start
          Behaviors.same
        }
        case StopPressed => {
          controllerActor ! Stop
          Behaviors.same
        }
      )
    )
