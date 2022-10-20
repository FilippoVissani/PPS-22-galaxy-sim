package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.model.{Boundary, CelestialBody, Simulation}
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
      ctx.log.debug("View")
      val view = View(ctx.self, percentSize, percentSize)

      Behaviors.receiveMessage[ViewActorCommand](msg => msg match
        case Display(simulation: Simulation) => {
          view.display(simulation)
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
