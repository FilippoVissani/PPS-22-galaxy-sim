package galaxy_sim.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StarLifecycleTest extends AnyFlatSpec with should.Matchers:

  val name: String = "Star1"
  val mass: Mass = 100
  val volume: Volume = 23.7
  val speed: Vector2D = Pair(2, 2)
  val position: Point2D = Pair(1, 1)
  val star1: Star = Star(Body(name, mass, volume, speed, position))

  "star1" should "have correct parameters" in {
    star1.body.name shouldBe name
    star1.body.mass shouldBe mass
    star1.body.volume shouldBe volume
    star1.body.speed shouldBe speed
    star1.body.position shouldBe position
  }

  "'Massive Star'" should "become 'Red Supergiant'"

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"

  "'Low Mass Star'" should "become 'Red Giant'"

  "'Red Giant'" should "become 'Planetary Nebula'"

  "'Planetary Nebula'" should "become 'White Dwarf'"

  "'White Dwarf'" should "become 'Black Dwarf'"



