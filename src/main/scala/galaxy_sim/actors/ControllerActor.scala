package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.{Kill, PoisonPill}
import akka.pattern.StatusReply
import akka.util.Timeout
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.actors.ViewActor.*
import galaxy_sim.model.{CelestialBody, Simulation}
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}
import galaxy_sim.model.SimulationConfig.frameRate

/** In this object is defined the behaviour of controller actor.
 *
 * The controller actor accepts messages from ViewActor and SimulationManagerActor and converts the commands for the other.
 */
object ControllerActor:

  /** Defines the messages that can be sent to ControllerActor. */
  sealed trait ControllerActorCommand

  /** Starts the simulation.
   *
   * This message should be sent from ViewActor.
   */
  case object Start extends ControllerActorCommand

  /** Stops the simulation.
   *
   * This message should be sent from ViewActor.
   */
  case object Stop extends ControllerActorCommand

  /** Sets ViewActor to communicate with. 
   * 
   *  @param viewActor the ViewActor reference
  */
  case class SetView(viewActor: ActorRef[ViewActorCommand]) extends ControllerActorCommand

  /** This is an adapter to be called when using Ask pattern with SimulationManagerActor
   * to get the simulation state.
   *
   * This message should only be sent from ControllerActor to itself.
   * 
   *  @param simulation The current state of the simulation
   */
  case class SimulationStateAdaptedResponse(simulation: Option[Simulation]) extends ControllerActorCommand

  /** Used from an inside timer to request the simulation state every frameRate seconds.
   *
   * This message should only be sent from ControllerActor's timer.
   */
  case object Tick extends ControllerActorCommand

  /** Creates a ControllerActor.
   *
   *  @param viewActor the View actor reference.
   *  @param simulationManagerActor the simulation manager actor reference.
   */
  def apply(
    viewActor: Option[ActorRef[ViewActorCommand]],
    simulationManagerActor: ActorRef[SimulationManagerActorCommand]): Behavior[ControllerActorCommand] =
      Behaviors.setup[ControllerActorCommand](ctx =>
        Behaviors.withTimers(timers =>
          implicit val timeout: Timeout = 1.seconds
          Behaviors.receiveMessage[ControllerActorCommand](msg => msg match
            case Start => {
              simulationManagerActor ! StartSimulation
              Behaviors.same
            }
            case Stop => {
              simulationManagerActor ! StopSimulation
              Behaviors.stopped
            }
            case SetView(viewActor: ActorRef[ViewActorCommand]) => {
              timers.startTimerAtFixedRate(Tick, frameRate.milliseconds)
              ControllerActor(Option(viewActor), simulationManagerActor)
            }
            case SimulationStateAdaptedResponse(simulation: Option[Simulation]) => {
              for
                x <- viewActor
                y <- simulation
              yield x ! Display(y)
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
