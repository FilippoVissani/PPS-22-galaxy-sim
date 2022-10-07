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
            actualSimulation: Simulation): Behavior[SimulationManagerActorCommand] =
      Behaviors.setup[SimulationManagerActorCommand](ctx =>
        ctx.log.debug("simulation manager")
        implicit val timeout: Timeout = 1.seconds
        Behaviors.receiveMessage[SimulationManagerActorCommand](msg => msg match
          case StartIteration => {
            celestialBodyActors.foreach(x => {
              ctx.ask(x, AskMoveToNextPosition.apply){
                case Success(MoveToNextPositionResponse(celestialBody)) => MoveToNextPositionAdaptedResponse(Option(celestialBody))
                case Failure(_) => MoveToNextPositionAdaptedResponse(Option.empty)
              }
            })
            SimulationManagerActor(celestialBodyActors, actualSimulation.copy(celestialBodies = Set(),
                                                                              virtualTime = actualSimulation.virtualTime + actualSimulation.deltaTime))
          }
          case StopSimulation => Behaviors.same
          case CollisionChecked(celestialBody: Option[CelestialBody]) => Behaviors.same
          case MoveToNextPositionAdaptedResponse(celestialBody: Option[CelestialBody]) => {
            val tmp = actualSimulation.copy(celestialBodies = actualSimulation.celestialBodies + celestialBody.get)
            if celestialBodyActors.size == tmp.celestialBodies.size then
              ctx.self ! StartIteration
              SimulationManagerActor(celestialBodyActors, actualSimulation.copy(celestialBodies = Set()))
            else
              SimulationManagerActor(celestialBodyActors, actualSimulation.copy(celestialBodies = tmp.celestialBodies))
          }
          case AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) => {
            replyTo ! SimulationStateResponse(actualSimulation)
            Behaviors.same
          }
        )
      )
