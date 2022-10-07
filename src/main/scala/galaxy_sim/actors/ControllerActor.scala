package galaxy_sim.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.model.CelestialBody
import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.ViewActorCommand
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.SimulationManagerActorCommand
import galaxy_sim.actors.SimulationManagerActor.SimulationManagerActorCommand.*
import galaxy_sim.actors.SimulationManagerActor.SimulationStateResponse
import galaxy_sim.model.Simulation
import galaxy_sim.view.Envelope
import galaxy_sim.actors.ViewActor.ViewActorCommand.*
import concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success
import akka.pattern.StatusReply
import akka.util.Timeout

object ControllerActor:

  enum ControllerActorCommand:
    case Start
    case Stop
    case SetView(viewActor: ActorRef[ViewActorCommand])
    case SimulationStateAdaptedResponse(simulation: Option[Simulation])
    case Tick

  export ControllerActorCommand.*

  def apply(
    viewActor: Option[ActorRef[ViewActorCommand]],
    simulationManagerActor: ActorRef[SimulationManagerActorCommand]): Behavior[ControllerActorCommand] =
      Behaviors.setup[ControllerActorCommand](ctx =>
        Behaviors.withTimers(timers =>
          ctx.log.debug("Controller")
          implicit val timeout: Timeout = 1.seconds
          Behaviors.receiveMessage[ControllerActorCommand](msg => msg match
            case Start => {
              simulationManagerActor ! StartIteration
              timers.startTimerAtFixedRate(Tick, 100.milliseconds)
              Behaviors.same
            }
            case Stop => {
              simulationManagerActor ! StopSimulation
              Behaviors.same
            }
            case SetView(viewActor: ActorRef[ViewActorCommand]) => {
              ControllerActor(Option(viewActor), simulationManagerActor)
            }
            case SimulationStateAdaptedResponse(simulation: Option[Simulation]) => {
              if viewActor.isDefined && simulation.isDefined then
                viewActor.get ! Display(Envelope(simulation.get.celestialBodies, simulation.get.bounds, simulation.get.virtualTime))
              Behaviors.same
            }
            case Tick => {
              ctx.ask(simulationManagerActor, AskSimulationState.apply){
                case Success(SimulationStateResponse(simulation)) => SimulationStateAdaptedResponse(Option(simulation))
                case Failure(_) => SimulationStateAdaptedResponse(Option.empty)
              }
              Behaviors.same
            }
          )
        )
      )
