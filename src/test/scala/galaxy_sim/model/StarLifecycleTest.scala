package galaxy_sim.model

import galaxy_sim.prolog.EntityIdentifierProlog
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StarLifecycleTest extends AnyFlatSpec with should.Matchers:

  "entity with mass 10 and temperature 1010" should "be a blackDwarf" in {
    val entityIdentifierProlog = EntityIdentifierProlog()
    entityIdentifierProlog.checkEntityType(10, 1010) shouldBe CelestialBodyType.BlackDwarf
  }

  "'Massive Star'" should "become 'Red Supergiant'"

  "'Red Supergiant'" should "become 'Supernova'"

  "'Supernova'" should "become 'Black Hole'"




