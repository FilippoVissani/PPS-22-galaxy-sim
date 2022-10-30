package galaxy_sim.actors

import akka.actor.testkit.typed.Effect
import akka.actor.testkit.typed.Effect.Stopped
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.javadsl.Behaviors
import galaxy_sim.actors.CelestialBodyActor.MoveToNextPosition
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.emptyGalaxy
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import galaxy_sim.actors.CelestialBodyActor.SolveCollisions

class CelestialBodyActorTest extends AnyFunSuite:
  test("GetCelestialBodyState"){
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.GetCelestialBodyState(inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("UpdateCelestialBodyType") {
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.UpdateCelestialBodyType(inbox.ref))
    testKit.returnedBehavior shouldBe testKit.currentBehavior
  }

  test("MoveToNextPosition"){
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(MoveToNextPosition(emptyGalaxy, inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("SolveCollisions"){
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(SolveCollisions(emptyGalaxy, inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("Kill"){
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime))
    testKit.run(CelestialBodyActor.Kill)
    testKit.returnedBehavior shouldBe Behaviors.stopped
  }
