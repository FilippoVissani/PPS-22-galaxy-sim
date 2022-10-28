package physics.dynamics

import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.funsuite.AnyFunSuite
import physics.*
import GravitationLaws.*

import scala.math.{cbrt, pow, sqrt}
import Utils.*
import physics.dynamics.PhysicsFormulas.*


class TestGravitationLaws extends AnyFeatureSpec with GivenWhenThen:
  info("As a programmer")
  info("Considering some entities in the space")
  info("I want to move entities around others")

  Feature("Gravitational law calculations"){
    var earthFinal = earth

    Scenario("The entity moves around a bigger one after some time"){
      Given("Two entities at their initial position - the Earth and the Sun")
      val earthGravConst = entitiesGravitationalConstant(earth.mass, sun.mass)
      val sunEarthDistance = distanceBetweenTwoEntities(earth, sun)
      val module = moduleOfDistance(sunEarthDistance)
      val shouldGForce = Pair(-earthGravConst * sunEarthDistance.x / module, -earthGravConst * sunEarthDistance.y / module)
      earthFinal = earth.copy(gForceVector = gravitationalForceOnEntity(earth, sun))
      assert(earthFinal.gForceVector == shouldGForce)
      
      When("Pass some time")
      val shouldSpeedVector = Pair(earthFinal.speedVector.x + (earthFinal.gForceVector.x * deltaTime / earthFinal.mass), earthFinal.speedVector.y + (earthFinal.gForceVector.y * deltaTime / earthFinal.mass))
      earthFinal = earthFinal.copy(speedVector = speedVectorAfterTime(earthFinal, deltaTime))
      assert(earthFinal.speedVector == shouldSpeedVector)

      val newPos = Pair(earthFinal.position.x + (earthFinal.speedVector.x * deltaTime), earthFinal.position.y + (earthFinal.speedVector.y * deltaTime))
      earthFinal = earthFinal.copy(position = vectorChangeOfDisplacement(earthFinal, deltaTime))
      assert(earthFinal.position == newPos)

      Then("I can see the entity in its new position, changing speed and gravity force on it")
      val shouldEarth = PhysicalEntityImpl(earthMass, newPos, earth.aphelionSpeed, shouldSpeedVector, shouldGForce)
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