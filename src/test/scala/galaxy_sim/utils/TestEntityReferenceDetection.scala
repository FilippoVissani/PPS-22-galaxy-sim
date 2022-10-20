package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.{sun, *}
import galaxy_sim.utils.{EntityReferenceDetector, EntityReferenceDetectors}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec

class TestEntityReferenceDetection extends AnyFeatureSpec with GivenWhenThen:
  import EntityReferenceDetector.*
  import EntityReferenceDetectors.given

  info("As a programmer")
  info("Considering some entities in the space")
  info("In order to do gravitational calculations")
  info("I have to know which one is the entity reference of another one")

  Feature("Understand which one is the entity reference of another one"){

    Scenario("Two or more smaller entities") {
      Given("A set of entities")
      val entities = Set(sun, earth, moon, blackHole)

      When("I want to know which one is the entity reference of another")
      val earthRef = getReference(earth, entities)
      val sunRef = getReference(sun, entities)
      val moonRef = getReference(moon, entities)

      Then("I get the correct entity reference")
      assert(earthRef.name == sun.name)
      assert(sunRef.name == blackHole.name)
      assert(moonRef.name == earth.name)
    }

    Scenario("The biggest entity"){
      Given("The same set of entities")
      val entities = Set(sun, earth, moon, blackHole)

      When("I have to get the reference of the biggest one")
      val blackHoleRef = getReference(blackHole, entities)

      Then("The entity is his reference")
      assert(blackHoleRef.name == blackHole.name)
    }

  }

