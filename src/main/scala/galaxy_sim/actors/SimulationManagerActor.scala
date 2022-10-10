package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import galaxy_sim.model.CelestialBody
import galaxy_sim.model.Simulation
import galaxy_sim.actors.ControllerActor.ControllerActorCommand
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand.*
import galaxy_sim.actors.CelestialBodyActor.CelestialBodyActorCommand
import akka.util.Timeout
import concurrent.duration.DurationInt
import galaxy_sim.actors.CelestialBodyActor.MoveToNextPositionResponse
import scala.util.Failure
import scala.util.Success
import akka.pattern.StatusReply

object SimulationManagerActor:

  enum SimulationManagerActorCommand:
    case StartIteration
    case StopSimulation
    case CollisionChecked(celestialBody: Option[CelestialBody])
    case MoveToNextPositionAdaptedResponse(celestialBody: Option[CelestialBody])
    case AskSimulationState(replyTo: ActorRef[SimulationStateResponse])

  case class SimulationStateResponse(simulation: Simulation)

  export SimulationManagerActorCommand.*

  def apply(celestialBodyActors: Set[ActorRef[CelestialBodyActorCommand]],
            actualSimulation: Simulation,
            tmpCelestialBodies: Set[CelestialBody] = Set()): Behavior[SimulationManagerActorCommand] =
      Behaviors.setup[SimulationManagerActorCommand](ctx =>
        implicit val timeout: Timeout = 1.seconds
        Behaviors.receiveMessage[SimulationManagerActorCommand](msg => msg match
          case StartIteration => {
            ctx.log.debug("Received StartIteration")
            celestialBodyActors.foreach(x => {
              ctx.ask(x, AskMoveToNextPosition.apply){
                case Success(MoveToNextPositionResponse(celestialBody)) => MoveToNextPositionAdaptedResponse(Option(celestialBody))
                case Failure(_) => MoveToNextPositionAdaptedResponse(Option.empty)
              }
            })
            SimulationManagerActor(celestialBodyActors, actualSimulation.copy(virtualTime = actualSimulation.virtualTime + actualSimulation.deltaTime))
          }
          case StopSimulation => Behaviors.same
          case CollisionChecked(celestialBody: Option[CelestialBody]) => Behaviors.same
          case MoveToNextPositionAdaptedResponse(celestialBody: Option[CelestialBody]) => {
            ctx.log.debug(s"Received MoveToNextPositionAdaptedResponse, ${tmpCelestialBodies.size}")
            val tmp = tmpCelestialBodies + celestialBody.get
            if celestialBodyActors.size == tmp.size then
              ctx.self ! StartIteration
              SimulationManagerActor(celestialBodyActors, actualSimulation.copy(celestialBodies = tmp))
            else
              SimulationManagerActor(celestialBodyActors, actualSimulation, tmp)
          }
          case AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) => {
            ctx.log.debug("Received AskSimulationState")
            replyTo ! SimulationStateResponse(actualSimulation)
            Behaviors.same
          }
        )
      )
