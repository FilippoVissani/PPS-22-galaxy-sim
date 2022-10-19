package physics.dynamics

import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.funsuite.AnyFunSuite
import physics.*

import scala.math.{pow, sqrt}

case class PhysicalEntityImpl(override val mass: Mass = 1000,
                              override val position: Position = Pair(0, 0),
                              override val aphelionSpeed: Speed = 10000,
                              override val speedVector: SpeedVector = Pair(0, 0),
                              override val gForceVector: GravityForceVector = Pair(0, 0)) extends PhysicalEntity

class TestGravitationLaws extends AnyFeatureSpec with GivenWhenThen:
  import GravitationLaws.*

  info("As a programmer")
  info("Considering some entities in the space")
  info("I want to move entities around others")

  Feature("Gravitational law calculations"){
    val earth = PhysicalEntityImpl(5.972e24, Pair(astronomicUnit * 10167, 0), 29290, Pair(0, 29290), Pair(0, 0))
    val sun = PhysicalEntityImpl(2.0e30)
    val deltaTime = daySec * 1 //one day
    var earthFinal = earth

    Scenario("The entity moves around a bigger one after some time"){
      Given("Two entities at their initial position - the Earth and the Sun")

      When("Pass some time")

      val shouldDistance = Pair(earth.position.x - sun.position.x, earth.position.y - sun.position.y)
      val sunEarthDistance = distanceBetweenTwoEntities(earth, sun)
      assert(sunEarthDistance == shouldDistance)

      val shouldMod = pow(pow(sunEarthDistance.x, 2) + pow(sunEarthDistance.y, 2), moduleConstant)
      val module = moduleOfDistance(sunEarthDistance)
      assert(module == shouldMod)

      val shouldGravConst = gravityConstant * earth.mass * sun.mass
      val earthGravConst = entitiesGravitationalConstant(earth.mass, sun.mass)
      assert(earthGravConst == shouldGravConst)

      val shouldGForce = Pair(- earthGravConst * sunEarthDistance.x / module, - earthGravConst * sunEarthDistance.y / module)
      earthFinal = earth.copy(gForceVector = gravitationalForceOnEntity(earth, sun))
      assert(earthFinal.gForceVector == shouldGForce)

      val shouldSpeedVector = Pair(earthFinal.speedVector.x + (earthFinal.gForceVector.x * deltaTime / earthFinal.mass), earthFinal.speedVector.y +(earthFinal.gForceVector.y * deltaTime / earthFinal.mass))
      earthFinal = earthFinal.copy(speedVector = speedVectorAfterTime(earthFinal, deltaTime))
      assert(earthFinal.speedVector == shouldSpeedVector)

      val newPos = Pair(earthFinal.position.x + (earthFinal.speedVector.x * deltaTime), earthFinal.position.y + (earthFinal.speedVector.y * deltaTime))
      earthFinal = earthFinal.copy(position = vectorChangeOfDisplacement(earthFinal, deltaTime))
      assert(earthFinal.position == newPos)

      Then("I can see the entity in its new position, changing speed and gravity force on it")
      val shouldEarth = PhysicalEntityImpl(5.972e24, newPos, 29290, shouldSpeedVector, shouldGForce)
      assert(earthFinal === shouldEarth)
    }

    Scenario("The bigger entity moves due to the influence of the others"){
      Given("Two entities - the Sun and the Earth")
      var shouldSun = sun.copy()
      val entities = Set(earthFinal)

      When("When pass some time")
      var sunFinal = sun.copy(speedVector = entityReferenceSpeedVectorAfterTime(sun, Set(earthFinal), deltaTime))
      sunFinal = sunFinal.copy(position = vectorChangeOfDisplacement(sunFinal, deltaTime))

      Then("I can also see the entity move reference into another point")
      shouldSun = shouldSun.copy(speedVector = Pair(sun.speedVector.x + (-entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / sun.mass), sun.speedVector.y + (-entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / sun.mass)))
      shouldSun = shouldSun.copy(position = Pair(sun.position.x + (shouldSun.speedVector.x * deltaTime), sun.position.y + (shouldSun.speedVector.y * deltaTime)))
      assert(sunFinal == shouldSun)
    }
  }
