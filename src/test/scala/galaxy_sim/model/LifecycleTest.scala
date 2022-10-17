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

  "'Massive Star'" should "become 'Red Supergiant'"

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"

  "'Black Hole'" should "increase his mass by 1 after a Step" in {
    val celestialBody = SimulationConfig.blackHole
    Lifecycle.entityOneStep(celestialBody, CelestialBodyType.BlackHole)._1.mass shouldBe SimulationConfig.blackHole.mass * 1.1
  }
