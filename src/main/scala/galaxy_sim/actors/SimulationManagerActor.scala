package galaxy_sim.actors

import akka.actor.PoisonPill
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.pattern.StatusReply
import akka.util.Timeout
import galaxy_sim.actors.CelestialBodyActor.*
import galaxy_sim.actors.ControllerActor.ControllerActorCommand
import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation, emptyGalaxy}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

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
  case object TypeUpdated extends IterationState
  case object PositionsUpdated extends IterationState

  def apply(celestialBodyActors: Set[ActorRef[CelestialBodyActorCommand]],
            actualSimulation: Simulation,
            iterationState: Seq[IterationState] = Seq(Start, TypeUpdated, PositionsUpdated),
            tmpCelestialBodies: Map[CelestialBodyType, Set[CelestialBody]] = emptyGalaxy): Behavior[SimulationManagerActorCommand] =
      Behaviors.setup[SimulationManagerActorCommand](ctx =>
        Behaviors.receiveMessage[SimulationManagerActorCommand](msg => msg match
          case StartSimulation => {
            ctx.log.debug("Start simulation")
            ctx.self ! IterationStep
            Behaviors.same
          }
          case StopSimulation => {
            celestialBodyActors.foreach(x => x ! Kill)
            Behaviors.stopped
          }
          case IterationStep => {
            ctx.log.debug(s"Iteration step ${iterationState.head}")
            iterationState.head match
              case Start => celestialBodyActors.foreach(x => x ! UpdateCelestialBodyType(ctx.self))
              case TypeUpdated => celestialBodyActors.foreach(x => x ! MoveToNextPosition(tmpCelestialBodies, ctx.self))
              case PositionsUpdated => celestialBodyActors.foreach(x => x ! SolveCollisions(tmpCelestialBodies, ctx.self))
            val newVirtualTime = iterationState.head match
              case Start => actualSimulation.virtualTime + actualSimulation.deltaTime
              case _ => actualSimulation.virtualTime
            SimulationManagerActor(
              celestialBodyActors,
              actualSimulation.copy(galaxy = tmpCelestialBodies, virtualTime = newVirtualTime),
              iterationState.tail :+ iterationState.head,
              )
          }
          case CelestialBodyState(celestialBody: CelestialBody, celestialBodyType: CelestialBodyType) => {
            val newCelestialBodies = tmpCelestialBodies.map((k, v) => if k == celestialBodyType then (k, v + celestialBody) else (k, v))
            if newCelestialBodies.values.map(x => x.size).sum == celestialBodyActors.size then ctx.self ! IterationStep
            SimulationManagerActor(celestialBodyActors, actualSimulation, iterationState, newCelestialBodies)
          }
          case AskSimulationState(replyTo: ActorRef[SimulationStateResponse]) => {
            ctx.log.debug("Received AskSimulationState")
            replyTo ! SimulationStateResponse(actualSimulation)
            Behaviors.same
          }
        )
      )
