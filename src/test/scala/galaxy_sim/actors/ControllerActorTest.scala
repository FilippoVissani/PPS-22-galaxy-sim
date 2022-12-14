package galaxy_sim.actors

import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.javadsl.Behaviors
import akka.util.Timeout
import galaxy_sim.actors.ControllerActor.*
import galaxy_sim.actors.SimulationManagerActor.{AskSimulationState, StartSimulation, StopSimulation}
import galaxy_sim.actors.ViewActor.{DisplaySimulation, ViewActorCommand}
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.Simulation
import galaxy_sim.model.SimulationConfig.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import scala.concurrent.duration.DurationInt

class ControllerActorTest extends AnyFunSuite:
  test("Start"){
    val eventRecorderActor = BehaviorTestKit(EventRecorderActor())
    val celestialBody = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, eventRecorderActor.ref))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(body01)), bounds, 0, deltaTime)))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref, eventRecorderActor.ref))
    testKit.run(Start)
    simulationManager.selfInbox().expectMessage(StartSimulation)
  }

  test("Stop"){
    val eventRecorderActor = BehaviorTestKit(EventRecorderActor())
    val celestialBody = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, eventRecorderActor.ref))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(body01)), bounds, 0, deltaTime)))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref, eventRecorderActor.ref))
    testKit.run(Stop)
    simulationManager.selfInbox().expectMessage(StopSimulation)
    testKit.returnedBehavior shouldBe Behaviors.same
  }

  test("SimulationStateAdaptedResponse"){
    val eventRecorderActor = BehaviorTestKit(EventRecorderActor())
    val simulation = Simulation(galaxy = Map(MassiveStar -> Set(body01)), bounds, 0, deltaTime)
    val celestialBody = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, eventRecorderActor.ref))
    val simulationManager = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), simulation))
    val testKit = BehaviorTestKit(ControllerActor(Option.empty, simulationManager.ref, eventRecorderActor.ref))
    val inbox = TestInbox[ViewActorCommand]()
    testKit.run(SetView(inbox.ref))
    testKit.run(SimulationStateAdaptedResponse(Option(simulation)))
    inbox.expectMessage(DisplaySimulation(simulation))
  }
