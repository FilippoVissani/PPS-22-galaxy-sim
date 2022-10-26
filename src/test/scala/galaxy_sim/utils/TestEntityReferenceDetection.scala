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
      println("ONE")
      val earthRef = getReference(earth, entities).get
      println(s"earth got ${earthRef.name}")
      println("TWO")
      val sunRef = getReference(sun, entities).get
      println(s"sun got ${sunRef.name}")
      println("THREE")
      val moonRef = getReference(moon, entities).get
      println(s"moon got ${moonRef.name}")

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

      Then("The entity reference is empty because it has no reference")
      assert(blackHoleRef.isEmpty)
    }

  }

