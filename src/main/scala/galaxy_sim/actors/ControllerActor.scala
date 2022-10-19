package galaxy_sim.actors

import akka.actor.{Kill, PoisonPill}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.pattern.StatusReply
import akka.util.Timeout
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.actors.ViewActor.*
import galaxy_sim.model.{CelestialBody, Simulation}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object ControllerActor:
  val frameRate = 33

  sealed trait ControllerActorCommand
  case object Start extends ControllerActorCommand
  case object Stop extends ControllerActorCommand
  case class SetView(viewActor: ActorRef[ViewActorCommand]) extends ControllerActorCommand
  case class SimulationStateAdaptedResponse(simulation: Option[Simulation]) extends ControllerActorCommand
  case object Tick extends ControllerActorCommand

  def apply(
    viewActor: Option[ActorRef[ViewActorCommand]],
    simulationManagerActor: ActorRef[SimulationManagerActorCommand]): Behavior[ControllerActorCommand] =
      Behaviors.setup[ControllerActorCommand](ctx =>
        Behaviors.withTimers(timers =>
          implicit val timeout: Timeout = 1.seconds
          Behaviors.receiveMessage[ControllerActorCommand](msg => msg match
            case Start => {
              ctx.log.debug("Received Start")
              simulationManagerActor ! StartSimulation
              timers.startTimerAtFixedRate(Tick, frameRate.milliseconds)
              Behaviors.same
            }
            case Stop => {
              ctx.log.debug("Received Stop")
              simulationManagerActor ! StopSimulation
              Behaviors.stopped
            }
            case SetView(viewActor: ActorRef[ViewActorCommand]) => {
              ctx.log.debug("Received SetView")
              ControllerActor(Option(viewActor), simulationManagerActor)
            }
            case SimulationStateAdaptedResponse(simulation: Option[Simulation]) => {
              ctx.log.debug("Received SimulationStateAdaptedResponse")
              if viewActor.isDefined && simulation.isDefined then
                viewActor.foreach(x => simulation.foreach(y => x ! Display(y)))
              Behaviors.same
            }
            case Tick => {
              ctx.log.debug("Received Tick")
              ctx.ask(simulationManagerActor, AskSimulationState.apply){
                case Success(SimulationStateResponse(simulation)) => SimulationStateAdaptedResponse(Option(simulation))
                case Failure(_) => SimulationStateAdaptedResponse(Option.empty)
              }
              Behaviors.same
            }
          )
        )
      )
