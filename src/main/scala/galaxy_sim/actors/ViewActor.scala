package galaxy_sim.actors

import galaxy_sim.model.CelestialBody
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import galaxy_sim.actors.ControllerActor.ControllerActorCommand
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.view.SwingGUI
import galaxy_sim.view.View
import galaxy_sim.model.Boundary
import galaxy_sim.view.Envelope
import galaxy_sim.actors.ControllerActor.ControllerActorCommand.*

object ViewActor:

  enum ViewActorCommand:
    case Display(envelope: Envelope)
    case StartPressed
    case StopPressed

  export ViewActorCommand.*

  def apply(controllerActor: ActorRef[ControllerActorCommand]): Behavior[ViewActorCommand] =
    Behaviors.setup[ViewActorCommand](ctx =>
      ctx.log.debug("View")
      val view = View(ctx.self, 90, 90)

      Behaviors.receiveMessage[ViewActorCommand](msg => msg match
        case Display(envelope: Envelope) => {
          ctx.log.debug("Received Display")
          view.display(envelope)
          Behaviors.same
        }
        case StartPressed => {
          ctx.log.debug("Received StartPressed")
          controllerActor ! Start
          Behaviors.same
        }
        case StopPressed => {
          ctx.log.debug("Received StopPressed")
          controllerActor ! Stop
          Behaviors.same
        }
      )
    )