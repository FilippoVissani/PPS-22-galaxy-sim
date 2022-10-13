package galaxy_sim.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import galaxy_sim.model.CelestialBody
import galaxy_sim.model.Simulation
import galaxy_sim.actors.ControllerActor.ControllerActorCommand
import akka.util.Timeout
import concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success
import akka.pattern.StatusReply
import galaxy_sim.actors.CelestialBodyActor.*
import galaxy_sim.model.CelestialBodyType

object SimulationManagerActor:

  sealed trait SimulationManagerActorCommand
  case object StartSimulation extends SimulationManagerActorCommand
  case object StopSimulation extends SimulationManagerActorCommand
  case object IterationStep extends SimulationManagerActorCommand
  case class CelestialBodyState(celestialBody: CelestialBody, celestialBodyType: CelestialBodyType) extends SimulationManagerActorCommand
  case class AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) extends SimulationManagerActorCommand

  case class SimulationStateResponse(simulation: Simulation)

  sealed trait IterationState
  case object Start extends IterationState
  case object StateAsked extends IterationState
  case object PositionsUpdated extends IterationState

  def apply(celestialBodyActors: Map[CelestialBodyType, Set[ActorRef[CelestialBodyActorCommand]]],
            actualSimulation: Simulation,
            iterationState: Seq[IterationState] = Seq(Start, StateAsked, PositionsUpdated),
            tmpCelestialBodies: Map[CelestialBodyType, Set[CelestialBody]] = Map()): Behavior[SimulationManagerActorCommand] =
      Behaviors.setup[SimulationManagerActorCommand](ctx =>
        Behaviors.receiveMessage[SimulationManagerActorCommand](msg => msg match
          case StartSimulation => {
            ctx.log.debug("Start simulation")
            ctx.self ! IterationStep
            Behaviors.same
          }
          case StopSimulation => Behaviors.same
          case IterationStep => {
            ctx.log.debug(s"Iteration step ${iterationState.head}")
            iterationState.head match
              case Start => celestialBodyActors.foreach((_, v) => v.foreach(x => x ! GetCelestialBodyState(ctx.self)))
              case StateAsked => celestialBodyActors.foreach((_, v) => v.foreach(x => x ! MoveToNextPosition(tmpCelestialBodies, ctx.self)))
              case PositionsUpdated => celestialBodyActors.foreach((_, v) => v.foreach(x => x ! CheckCollisions(tmpCelestialBodies, ctx.self)))
            SimulationManagerActor(celestialBodyActors, actualSimulation.copy(celestialBodies = tmpCelestialBodies), iterationState.tail :+ iterationState.head)
          }
          case CelestialBodyState(celestialBody: CelestialBody, celestialBodyType: CelestialBodyType) => {
            val newCelestialBodies: Map[CelestialBodyType, Set[CelestialBody]] = tmpCelestialBodies.map((k, v) => if k == celestialBodyType then (k, v + celestialBody) else (k, v))
            if newCelestialBodies.values.map(x => x.size).sum == celestialBodyActors.values.map(x => x.size).sum then ctx.self ! IterationStep
            SimulationManagerActor(celestialBodyActors, actualSimulation, iterationState, newCelestialBodies)
          }
          case AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) => {
            ctx.log.debug("Received AskSimulationState")
            replyTo ! SimulationStateResponse(actualSimulation)
            Behaviors.same
          }
        )
      )
