package galaxy_sim.model

import galaxy_sim.prolog.EntityIdentifierProlog
import galaxy_sim.model.SimulationConfig
import galaxy_sim.model.Lifecycle
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class LifecycleTest extends AnyFlatSpec with should.Matchers:

  val entityIdentifierProlog: EntityIdentifierProlog = EntityIdentifierProlog()

  "entity with mass 10 and temperature 1010" should "be a 'Massive Star'" in {
      entityIdentifierProlog.checkEntityType(10, 1010) shouldBe CelestialBodyType.MassiveStar
    }

  "entity with mass 10e11 and temperature 1010" should "be a 'redSuperGiant'" in {
    entityIdentifierProlog.checkEntityType(10e11, 1010) shouldBe CelestialBodyType.RedSuperGiant
  }

  "entity with mass 10e21 and temperature 1010" should "be a 'supernova'" in {
    entityIdentifierProlog.checkEntityType(10e21, 1010) shouldBe CelestialBodyType.Supernova
  }

  "entity with mass 10e41 and temperature 1010" should "be a 'blackHole'" in {
    entityIdentifierProlog.checkEntityType(10e41, 1010) shouldBe CelestialBodyType.BlackHole
  }

  "entity with mass 10 and temperature 101" should "be a 'InterstellarCloud'" in {
    entityIdentifierProlog.checkEntityType(10, 101) shouldBe CelestialBodyType.InterstellarCloud
  }

  "entity with mass 10 and temperature 10" should "be a 'planet'" in {
    entityIdentifierProlog.checkEntityType(10, 10) shouldBe CelestialBodyType.Planet
  }

  "entity with mass 10 and temperature 0" should "be a 'asteroid'" in {
    entityIdentifierProlog.checkEntityType(10, 0) shouldBe CelestialBodyType.Asteroid
  }

  "'Massive Star'" should "become 'Red Supergiant'"

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"

  "'Black Hole'" should "increase his mass by 1 after a Step" in {
    val celestialBody = SimulationConfig.blackHole
    Lifecycle.entityOneStep(celestialBody, CelestialBodyType.BlackHole)._1.mass shouldBe SimulationConfig.blackHole.mass * 1.1
  }
