package galaxy_sim.actors

import akka.actor.PoisonPill
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.pattern.StatusReply
import akka.util.Timeout
import galaxy_sim.actors.CelestialBodyActor.*
import galaxy_sim.actors.ControllerActor.ControllerActorCommand
import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation, emptyGalaxy}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}
import galaxy_sim.model.SimulationConfig.frameRate
import galaxy_sim.actors.SimulationManagerActor.IterationState.*
import galaxy_sim.actors.ViewActor.ViewActorCommand

/** In this object is defined the behaviour of simulation manager actor.
 *
 * This actor is designed to communicate only with CelestialBody and ControllerActor.
 */
object SimulationManagerActor:

  /** Defines the messages that can be sent to SimulationManagerActor. */
  sealed trait SimulationManagerActorCommand
  
  /** Starts the simulation. */
  case object StartSimulation extends SimulationManagerActorCommand
  
  /** Stops the simulation. */
  case object StopSimulation extends SimulationManagerActorCommand
  
  /** Executes a step in the current iteration. */
  case object IterationStep extends SimulationManagerActorCommand
  
  /** Receives the state of a celestial body
   *  
   *  @param celestialBody the state of the celestial body
   *  @param celestialBodyType the type of the celestial body
   */
  case class CelestialBodyState(celestialBody: CelestialBody, celestialBodyType: CelestialBodyType) extends SimulationManagerActorCommand
  
  /** Ask pattern called from ControllerActor
   *  
   *  @param replyTo response of the Ask
   */
  case class AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) extends SimulationManagerActorCommand

  //todo
  case class GreetCelestialBody(viewActor: ActorRef[ViewActorCommand]) extends SimulationManagerActorCommand
  /** Response of the ask pattern
   *  
   *  @param simulation current state of the simulation
   */
  case class SimulationStateResponse(simulation: Simulation)

  /** Defines the steps in an iteration. */
  enum IterationState:
    /** Start a new iteration. */
    case Start
    /** Timer is ticked. */
    case TimerTicked
    /** Celestial body stats are updated. */
    case TypeUpdated
    /** The position of the celestial bodies is updated. */
    case PositionsUpdated

  /** Creates a SimulationManagerActor.
   *
   *  @param celestialBodyActors references of all celestial body actors.
   *  @param actualSimulation current state of the simulation.
   *  @param iterationState current state of the iteration.
   *  @param tmpCelestialBodies temporary data structure that contains the partial state of the celestial bodies.
   */
  def apply(celestialBodyActors: Set[ActorRef[CelestialBodyActorCommand]],
            actualSimulation: Simulation,
            iterationState: Seq[IterationState] = IterationState.values.toSeq,
            tmpGalaxy: Map[CelestialBodyType, Set[CelestialBody]] = emptyGalaxy): Behavior[SimulationManagerActorCommand] =
      Behaviors.setup[SimulationManagerActorCommand](ctx =>
        Behaviors.withTimers(timer => 
          Behaviors.receiveMessage[SimulationManagerActorCommand](msg => msg match
            case StartSimulation => {
              ctx.self ! IterationStep
              Behaviors.same
            }
            case StopSimulation => {
              celestialBodyActors.foreach(x => x ! Kill)
              Behaviors.stopped
            }
            case GreetCelestialBody(viewActor: ActorRef[ViewActorCommand]) => {
              celestialBodyActors.foreach( x => x ! GreetFromView(viewActor))
              Behaviors.same
            }
            case IterationStep => {
              iterationState.head match
                case Start => timer.startSingleTimer(IterationStep, frameRate.milliseconds)
                case TimerTicked => celestialBodyActors.foreach(x => x ! UpdateCelestialBodyType(ctx.self))
                case TypeUpdated => celestialBodyActors.foreach(x => x ! MoveToNextPosition(tmpGalaxy, ctx.self))
                case PositionsUpdated => celestialBodyActors.foreach(x => x ! SolveCollisions(tmpGalaxy, ctx.self))
              val newVirtualTime = iterationState.head match
                case Start => actualSimulation.virtualTime + actualSimulation.deltaTime
                case _ => actualSimulation.virtualTime
              val newGalaxy = if tmpGalaxy.values.flatten.isEmpty then actualSimulation.galaxy else tmpGalaxy
              SimulationManagerActor(
                celestialBodyActors,
                actualSimulation.copy(galaxy = newGalaxy, virtualTime = newVirtualTime),
                iterationState.tail :+ iterationState.head,
                )
            }
            case CelestialBodyState(celestialBody: CelestialBody, celestialBodyType: CelestialBodyType) => {
              val newCelestialBodies = for (k, v) <- tmpGalaxy yield(if k == celestialBodyType then (k, v + celestialBody) else (k, v))
              if newCelestialBodies.values.map(x => x.size).sum == celestialBodyActors.size then ctx.self ! IterationStep
              SimulationManagerActor(celestialBodyActors, actualSimulation, iterationState, newCelestialBodies)
            }
            case AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) => {
              replyTo ! SimulationStateResponse(actualSimulation)
              Behaviors.same
            }
          )
        )
      )
