package galaxy_sim.actors

import org.scalatest.funsuite.AnyFunSuite
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.Simulation
import galaxy_sim.actors.ControllerActor.Start
import galaxy_sim.actors.SimulationManagerActor.StartSimulation
import galaxy_sim.actors.ControllerActor.SimulationStateAdaptedResponse
import galaxy_sim.actors.ViewActor.ViewActorCommand
import akka.actor.testkit.typed.scaladsl.TestInbox
import galaxy_sim.actors.ControllerActor.SetView
import galaxy_sim.actors.ViewActor.Display
import galaxy_sim.actors.ControllerActor.Tick
import galaxy_sim.actors.SimulationManagerActor.AskSimulationState
import akka.util.Timeout
import concurrent.duration.DurationInt
import galaxy_sim.model.CelestialBodyType.*

class ControllerActorTest extends AnyFunSuite:
  test("Start"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Map(MassiveStar -> Set(celestialBody.ref)), Simulation(celestialBodies = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref))
    testKit.run(Start)
    simulationManager.selfInbox().expectMessage(StartSimulation)
  }

  test("Stop"){
    fail()
  }

  test("SimulationStateAdaptedResponse"){
    val simulation = Simulation(celestialBodies = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Map(MassiveStar -> Set(celestialBody.ref)), simulation))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref))
    val inbox = TestInbox[ViewActorCommand]()
    testKit.run(SetView(inbox.ref))
    testKit.run(SimulationStateAdaptedResponse(Option(simulation)))
    inbox.expectMessage(Display(simulation))
  }
