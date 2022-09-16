package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite

class EntityTest extends AnyFunSuite:

  val name: String = "Star1"
  val mass: Float = 100
  val volume: Float = 23.7
  val speed: Float = 500000
  val acceleration: Float = 200
  val position: Pair[Float, Float] = Pair(1, 1)
  val entity: Entity = Entity(name, mass, volume, speed, acceleration, position)

  test("Name") {
    assert(entity.updateName("Star2").name == "Star2")
  }

  test("Mass") {
    val delta: Float = -20
    assert(entity.updateMass(delta).mass == mass + delta)
  }

  test("Volume") {
    val delta: Float = 200
    assert(entity.updateVolume(delta).volume == volume + delta)
  }

  test("Speed") {
    val delta: Float = -300
    assert(entity.updateSpeed(delta).speed == speed + delta)
  }

  test("Acceleration") {
    val delta: Float = 40
    assert(entity.updateAcceleration(delta).acceleration == acceleration + delta)
  }

  test("Position") {
    val delta: Float = 10
    assert(entity.updatePosition(delta)(delta).position == Pair(position.x + delta, position.y + delta))
  }


