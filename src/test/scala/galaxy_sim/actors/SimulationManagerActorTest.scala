package galaxy_sim.actors

import org.scalatest.funsuite.AnyFunSuite
import galaxy_sim.model.SimulationConfig.*
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import galaxy_sim.actors.SimulationManagerActor.*
import akka.actor.testkit.typed.scaladsl.TestInbox
import galaxy_sim.model.Simulation
import akka.actor.testkit.typed.CapturedLogEvent
import org.slf4j.event.Level
import org.scalatest.matchers.should.Matchers.shouldBe
import galaxy_sim.actors.CelestialBodyActor.GetCelestialBodyState
import galaxy_sim.actors.CelestialBodyActor.MoveToNextPosition
import galaxy_sim.actors.CelestialBodyActor.SolveCollisions
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.actors.CelestialBodyActor.Kill
import akka.actor.typed.javadsl.Behaviors

class SimulationManagerActorTest extends AnyFunSuite:
  test("StartSimulation"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val testKit = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    testKit.run(StartSimulation)
    testKit.selfInbox().expectMessage(IterationStep)
  }

  test("StopSimulation"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val testKit = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    testKit.run(StopSimulation)
    celestialBody.selfInbox().expectMessage(Kill)
    testKit.returnedBehavior shouldBe Behaviors.stopped
  }

  test("IterationStep"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val testKit = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    testKit.run(IterationStep)
    celestialBody.selfInbox().expectMessage(GetCelestialBodyState(testKit.ref))
  }

  test("CelestialBodyState"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val testKit = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    testKit.run(CelestialBodyState(sun, MassiveStar))
    testKit.selfInbox().expectMessage(IterationStep)
  }

  test("AskSimulationState"){
    val celestialBody = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val testKit = BehaviorTestKit(SimulationManagerActor(Set(celestialBody.ref), Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
    val inbox = TestInbox[SimulationStateResponse]()
    testKit.run(AskSimulationState(inbox.ref))
    inbox.expectMessage(SimulationStateResponse(Simulation(galaxy = Map(MassiveStar -> Set(sun)), bounds, 0, deltaTime)))
  }