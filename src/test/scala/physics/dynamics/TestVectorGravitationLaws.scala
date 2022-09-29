package physics.dynamics

import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.funsuite.AnyFunSuite
import physics.*

import scala.math.{Pi, pow, sqrt, tan, tanh}

case class PhysicalEntityImpl(override val mass: Mass = 1000,
                              override val position: Position = Pair(0, 0),
                              override val aphelionSpeed: Speed = 10000,
                              override val speedVector: SpeedVector = Pair(0, 0),
                              override val gForceVector: GravityForceVector = Pair(0, 0)) extends PhysicalEntity

class TestGravitationLaws extends AnyFeatureSpec with GivenWhenThen:
  import GravitationLaws.*

  Feature("The user can see an entity that moves around another one"){
    info("As a programmer")
    info("I want to check that the entity moves based on specific calculations")
    val earth = PhysicalEntityImpl(5.972e24, Pair(astronomicUnit * 10167, 0), 29290, Pair(0, 29290), Pair(0, 0))
    val sun = PhysicalEntityImpl(2.0e30)
    val deltaTime = daySec * 1 //one day
    var earthFinal = PhysicalEntityImpl()

    Scenario("The entity moves after some time"){
      Given("Two entities - the Earth and the Sun")

      When("I want to see the movement of the earth after some time")

      val shouldDistance = Pair(earth.position.x - sun.position.x, earth.position.y - sun.position.y)
      val sunEarthDistance = posBetweenTwoEntities(earth, sun)
      assert(sunEarthDistance == shouldDistance)

      val shouldMod = pow(pow(sunEarthDistance.x, 2) + pow(sunEarthDistance.y, 2), moduleConstant)
      val module = moduleOfDistance(sunEarthDistance)
      assert(module == shouldMod)

      val shouldGravConst = gravityConstant * earth.mass * sun.mass
      val earthGravConst = entitiesGravitationalConstant(earth, sun)
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

      Then("I'm able to move the entity in its new position, changing speed and gravity force on it")
      val shouldEarth = PhysicalEntityImpl(5.972e24, newPos, 29290, shouldSpeedVector, shouldGForce)
      assert(earthFinal === shouldEarth)

      Then("I'm also able to eventually move the entity reference into another point")
      var sunFinal = sun.copy(speedVector = entityReferenceSpeedVectorAfterTime(sun, Set(earthFinal), deltaTime))
      sunFinal = sunFinal.copy(position = vectorChangeOfDisplacement(sunFinal, deltaTime))
      var shouldSun = sun.copy()
      val entities = Set(earthFinal)
      shouldSun = shouldSun.copy(speedVector = Pair(sun.speedVector.x + (- entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / sun.mass), sun.speedVector.y + (- entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / sun.mass)))
      shouldSun = shouldSun.copy(position = Pair(sun.position.x + (shouldSun.speedVector.x * deltaTime), sun.position.y + (shouldSun.speedVector.y * deltaTime)))
      assert(sunFinal == shouldSun)
    }
  }
