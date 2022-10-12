package galaxy_sim.actors

import org.scalatest.funsuite.AnyFunSuite
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import akka.actor.testkit.typed.scaladsl.TestInbox
import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.actors.SimulationManagerActor.*

class CelestialBodyActorTest extends AnyFunSuite:
  test("GetCelestialBodyState"){
    val testKit = BehaviorTestKit(CelestialBodyActor(sun, bounds, deltaTime))
    val inbox = TestInbox[SimulationManagerActorCommand]()
    testKit.run(CelestialBodyActor.GetCelestialBodyState(inbox.ref))
    inbox.expectMessage(CelestialBodyState(sun))
  }

  test("MoveToNextPosition"){
    fail()
  }

  test("CheckCollisions"){
    fail()
  }
