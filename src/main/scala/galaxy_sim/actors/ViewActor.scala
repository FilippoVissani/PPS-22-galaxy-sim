package galaxy_sim.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.model.{Boundary, CelestialBody, Simulation}
import galaxy_sim.view.{SwingGUI, View}

object ViewActor:

  sealed trait ViewActorCommand
  case class Display(simulation: Simulation) extends ViewActorCommand
  case object StartPressed extends ViewActorCommand
  case object StopPressed extends ViewActorCommand

  def apply(controllerActor: ActorRef[ControllerActorCommand]): Behavior[ViewActorCommand] =
    val percentSize = 90
    Behaviors.setup[ViewActorCommand](ctx =>
      ctx.log.debug("View")
      val view = View(ctx.self, percentSize, percentSize)

      Behaviors.receiveMessage[ViewActorCommand](msg => msg match
        case Display(simulation: Simulation) => {
          ctx.log.debug("Received Display")
          view.display(simulation)
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
