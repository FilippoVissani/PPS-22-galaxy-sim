package galaxy_sim.model

import galaxy_sim.prolog.EntityIdentifier
import galaxy_sim.model.SimulationConfig
import galaxy_sim.model.Lifecycle
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class LifecycleTest extends AnyFlatSpec with should.Matchers:

  "entity with mass 10 and temperature 1010" should "be a 'Massive Star'" in {
    EntityIdentifier.checkEntityType(10, 1010) shouldBe CelestialBodyType.MassiveStar
  }

  "entity with mass 1e11 and temperature 1010" should "be a 'redSuperGiant'" in {
    EntityIdentifier.checkEntityType(1e11, 1010) shouldBe CelestialBodyType.RedSuperGiant
  }

  "entity with mass 1e21 and temperature 1010" should "be a 'supernova'" in {
    EntityIdentifier.checkEntityType(1e21, 1010) shouldBe CelestialBodyType.Supernova
  }

  "entity with mass 1e41 and temperature 1010" should "be a 'blackHole'" in {
    EntityIdentifier.checkEntityType(1e41, 1010) shouldBe CelestialBodyType.BlackHole
  }

  "entity with mass 10 and temperature 101" should "be a 'InterstellarCloud'" in {
    EntityIdentifier.checkEntityType(10, 101) shouldBe CelestialBodyType.InterstellarCloud
  }

  "entity with mass 10 and temperature 55" should "be a 'planet'" in {
    EntityIdentifier.checkEntityType(10, 55) shouldBe CelestialBodyType.Planet
  }

  "entity with mass 10 and temperature 0" should "be a 'asteroid'" in {
    EntityIdentifier.checkEntityType(10, 0) shouldBe CelestialBodyType.Asteroid
  }

  "minimum mass for BlackHole" should "be 1e40" in{
    EntityIdentifier.minMassFor(CelestialBodyType.BlackHole) shouldBe 1e40
  }