package galaxy_sim.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import StarType.*

class StarLifecycleTest extends AnyFlatSpec with should.Matchers:

  val name: String = "Star1"
  val mass: Mass = 100
  val volume: Volume = 23.7
  val speed: Vector2D = Pair(2, 2)
  val position: Point2D = Pair(1, 1)

  "star1" should "have correct parameters" in {
    val star: Star = Star(Body(name, mass, volume, speed, position), MassiveStar)
    star.body.name shouldBe name
    star.body.mass shouldBe mass
    star.body.volume shouldBe volume
    star.body.speed shouldBe speed
    star.body.position shouldBe position
    star.starType shouldBe MassiveStar
  }

  "'Massive Star'" should "become 'Red Supergiant'" in {
    val star: Star = Star(Body(name, mass, volume, speed, position), MassiveStar)
    star.starType shouldBe MassiveStar
    star.oneYearOlder.starType shouldBe RedSuperGiant
  }

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"

  "'Low Mass Star'" should "become 'Red Giant'"

  "'Red Giant'" should "become 'Planetary Nebula'"

  "'Planetary Nebula'" should "become 'White Dwarf'"

  "'White Dwarf'" should "become 'Black Dwarf'"



