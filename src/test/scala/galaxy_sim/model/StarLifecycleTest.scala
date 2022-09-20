package galaxy_sim.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StarLifecycleTest extends AnyFlatSpec with should.Matchers:

  val name: String = "Star1"
  val mass: Float = 100
  val volume: Float = 23.7
  val speed: Float = 500000
  val acceleration: Float = 200
  val position: Pair[Float, Float] = Pair(1, 1)
  val star1: Entity = Entity(name, mass, volume, speed, acceleration, position)

  "star1" should "have correct parameters" in {
    star1.name shouldBe name
    star1.mass shouldBe mass
    star1.volume shouldBe volume
    star1.speed shouldBe 500000
    star1.acceleration shouldBe 200
    star1.position shouldBe position
  }
