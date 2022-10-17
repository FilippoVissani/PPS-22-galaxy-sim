package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{sun, *}
import galaxy_sim.utils.{EntityReferenceDetector, EntityReferenceDetectors}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec

class TestEntityReferenceDetection extends AnyFeatureSpec with GivenWhenThen:
  import EntityReferenceDetector.*
  import EntityReferenceDetectors.given

  Feature("To calculate the new position of an entity, I have to calculate the force of it's entity reference on it"){
    Scenario("I want to know which one is the entity reference of another") {
      Given("A set of entities")
      val entities = Set(sun, earth, moon, blackHole)

      When("I want to know which one is the entity reference of another")

      val earthRef = getReference(earth, entities)
      val sunRef = getReference(sun, entities)
      val moonRef = getReference(moon, entities)
      val blackHoleRef = getReference(blackHole, entities)

      println(s"EarthRef is ${earthRef.name}")
      println(s"sunRef is ${sunRef.name}")
      println(s"moonRef is ${moonRef.name}")
      println(s"blackHoleRef is ${blackHoleRef.name}")
      Then("")
      assert(earthRef.name == sun.name)
      assert(sunRef.name == blackHole.name)
      assert(moonRef.name == earth.name)
    }
  }

