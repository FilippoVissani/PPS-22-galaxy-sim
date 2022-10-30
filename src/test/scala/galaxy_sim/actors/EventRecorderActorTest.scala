package galaxy_sim.actors

import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import akka.actor.typed.scaladsl.Behaviors
import galaxy_sim.actors.EventRecorderActor.{AskRecordedEvents, RecordCollision, RecordDeath, RecordSpawn, RecordedEventsResponse}
import galaxy_sim.model.SimulationConfig.{body01, body02}
import org.scalatest.funsuite.AnyFunSuite

class EventRecorderActorTest extends AnyFunSuite:
  test("RecordCollision"){
    val testKit = BehaviorTestKit(EventRecorderActor())
    testKit.run(RecordCollision(body01, body02))
    assert(testKit.returnedBehavior != Behaviors.same)
  }

  test("RecordSpawn") {
    val testKit = BehaviorTestKit(EventRecorderActor())
    testKit.run(RecordSpawn(body01))
    assert(testKit.returnedBehavior != Behaviors.same)
  }

  test("RecordDeath") {
    val testKit = BehaviorTestKit(EventRecorderActor())
    testKit.run(RecordDeath(body01))
    assert(testKit.returnedBehavior != Behaviors.same)
  }
