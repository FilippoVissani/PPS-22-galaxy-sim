package galaxy_sim.utils

//import galaxy_sim.model.SimulationConfig.{sun, *}
import galaxy_sim.model.CelestialBody
import galaxy_sim.utils.{EntityReferenceDetector, EntityReferenceDetectors}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import physics.Pair
import physics.dynamics.PhysicsFormulas.*
//import physics.dynamics.Constants.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.Utils
//import physics.dynamics.Utils.PhysicalEntityImpl

class TestEntityReferenceDetection extends AnyFeatureSpec with GivenWhenThen:
  import EntityReferenceDetector.*
  import EntityReferenceDetectors.given
  import Utils.*

  info("As a programmer")
  info("Considering some entities in the space")
  info("In order to do gravitational calculations")
  info("I have to know which one is the entity reference of another one")

  Feature("Understand which one is the entity reference of another one"){
    val blackHoleCB: CelestialBody = CelestialBody(mass = solarMass * 5, gForceVector = Pair(0, 0), speedVector = Pair(0, 0),  position = Pair(0, 0), name = "BlackHole", radius = 0, temperature = 0)
    val sunCB: CelestialBody = CelestialBody(mass = solarMass, gForceVector = Pair(0, 0), speedVector = Pair(0, 50000), position = Pair(blackHole.position.x + astronomicUnit * 5, blackHole.position.y + astronomicUnit * 5), name = "Sun", radius = 0, temperature = 0)
    val earthCB: CelestialBody = CelestialBody(mass = earthMass, gForceVector = Pair(0, 0), speedVector = Pair(0, 29290), position = Pair(sun.position.x + astronomicUnit, sun.position.y + astronomicUnit), name = "Earth", radius = 0, temperature = 0)
    val moonCB: CelestialBody = CelestialBody(mass = earthMass * 0.0123, gForceVector = Pair(0, 0), speedVector = Pair(0, 3683), position = Pair(earth.position.x + 384400, earth.position.y + 384400), name = "Moon", radius = 0, temperature = 0)

    Scenario("Two or more smaller entities") {
      Given("A set of entities")
      val entities: Set[CelestialBody] = Set(blackHoleCB, sunCB, earthCB, moonCB)

      When("I want to know which one is the entity reference of another")
      val earthRef = getReference(earthCB, entities).get
      val sunRef = getReference(sunCB, entities).get
      val moonRef = getReference(moonCB, entities).get

      Then("I get the correct entity reference")
      assert(earthRef.name == sunCB.name)
      assert(sunRef.name == blackHoleCB.name)
      assert(moonRef.name == earthCB.name)
    }

    Scenario("The biggest entity"){
      Given("The same set of entities")
      val entities: Set[CelestialBody] = Set(blackHoleCB, sunCB, earthCB, moonCB)

      When("I have to get the reference of the biggest one")
      val blackHoleRef = getReference(blackHoleCB, entities)

      Then("The entity is his reference")
      assert(blackHoleRef.isEmpty)
    }

  }

