package galaxy_sim.actors

import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.javadsl.Behaviors
import galaxy_sim.actors.CelestialBodyActor.*
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.Simulation
import galaxy_sim.model.SimulationConfig.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import org.slf4j.event.Level

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
    assert(testKit.returnedBehavior == testKit.currentBehavior)
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