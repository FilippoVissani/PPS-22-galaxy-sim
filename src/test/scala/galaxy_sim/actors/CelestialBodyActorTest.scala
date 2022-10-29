package galaxy_sim.actors

import akka.actor.testkit.typed.Effect
import akka.actor.testkit.typed.Effect.Stopped
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import akka.actor.typed.ActorRef
import akka.actor.typed.javadsl.Behaviors
import galaxy_sim.actors.CelestialBodyActor.MoveToNextPosition
import galaxy_sim.actors.SimulationManagerActor.*
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.emptyGalaxy
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import galaxy_sim.actors.CelestialBodyActor.SolveCollisions
import galaxy_sim.actors.LoggerActor.LoggerActorCommand

class CelestialBodyActorTest extends AnyFunSuite:
  test("GetCelestialBodyState"){
    val loggerActor = BehaviorTestKit(LoggerActor())
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, loggerActor.ref))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.GetCelestialBodyState(inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("MoveToNextPosition"){
    val loggerActor = BehaviorTestKit(LoggerActor())
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, loggerActor.ref))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(MoveToNextPosition(emptyGalaxy, inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("SolveCollisions"){
    val loggerActor = BehaviorTestKit(LoggerActor())
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, loggerActor.ref))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(SolveCollisions(emptyGalaxy, inbox.ref))
    inbox.expectMessage(CelestialBodyState(body01, MassiveStar))
  }

  test("Kill"){
    val loggerActor = BehaviorTestKit(LoggerActor())
    val testKit = BehaviorTestKit(CelestialBodyActor(body01, MassiveStar, bounds, deltaTime, loggerActor.ref))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.Kill)
    testKit.returnedBehavior shouldBe Behaviors.stopped
  }
