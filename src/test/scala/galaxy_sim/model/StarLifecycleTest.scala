package galaxy_sim.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StarLifecycleTest extends AnyFlatSpec with should.Matchers:

  val name: String = "Star1"
  val mass: Double = 100
  val volume: Double = 23.7
  val speed: Pair[Double, Double] = Pair(1, 1)
  val position: Pair[Double, Double] = Pair(1, 1)
  val star: Entity = Entity(name, mass, volume, speed, position)

  "star" should "have correct parameters" in {
    star.name shouldBe name
    star.mass shouldBe mass
    star.volume shouldBe volume
    star.speed shouldBe Pair(1, 1)
    star.position shouldBe position
  }

  "'Massive Star'" should "become 'Red Supergiant'"

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"

  "'Low Mass Star'" should "become 'Red Giant'"

  "'Red Giant'" should "become 'Planetary Nebula'"

  "'Planetary Nebula'" should "become 'White Dwarf'"

  "'White Dwarf'" should "become 'Black Dwarf'"



