package galaxy_sim.actors

import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.javadsl.Behaviors
import akka.util.Timeout
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.SimulationManagerActor.{AskSimulationState, StartSimulation, StopSimulation}
import galaxy_sim.actors.ViewActor.{Display, ViewActorCommand}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.Simulation
import galaxy_sim.model.SimulationConfig.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe

import scala.concurrent.duration.DurationInt

class ControllerActorTest extends AnyFunSuite:
  test("Start"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref))
    testKit.run(Start)
    simulationManager.selfInbox().expectMessage(StartSimulation)
  }

  test("Stop"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref))
    testKit.run(Stop)
    simulationManager.selfInbox().expectMessage(StopSimulation)
    testKit.returnedBehavior shouldBe Behaviors.stopped
  }

  test("SimulationStateAdaptedResponse"){
    val simulation = Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), simulation))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref))
    val inbox = TestInbox[ViewActorCommand]()
    testKit.run(SetView(inbox.ref, simulation.galaxy))
    testKit.run(SimulationStateAdaptedResponse(Option(simulation)))
    inbox.expectMessage(Display(simulation))
  }
