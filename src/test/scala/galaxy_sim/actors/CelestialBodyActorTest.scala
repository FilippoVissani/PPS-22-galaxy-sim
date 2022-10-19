package galaxy_sim.actors

import akka.actor.testkit.typed.Effect
import akka.actor.testkit.typed.Effect.Stopped
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.javadsl.Behaviors
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationConfig.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe

class CelestialBodyActorTest extends AnyFunSuite:
  test("GetCelestialBodyState"){
    val testKit = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.GetCelestialBodyState(inbox.ref))
    inbox.expectMessage(CelestialBodyState(sun, MassiveStar))
  }

  test("MoveToNextPosition"){
    fail()
  }

  test("CheckCollisions"){
    fail()
  }

  test("Kill"){
    val testKit = BehaviorTestKit(CelestialBodyActor(sun, MassiveStar, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.Kill)
    testKit.returnedBehavior shouldBe Behaviors.stopped
  }
