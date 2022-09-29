package physics.dynamics

import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.funsuite.AnyFunSuite
import physics.*

import scala.math.{Pi, pow, tan, tanh}

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

      Scenario("The entity moves after some time"){
        Given("Two entities - the Earth and the Sun")
        val earth = PhysicalEntityImpl(5.972e24, Pair(astronomicUnit, 0), 29290, Pair(0, 29290), Pair(0, 0))
        val sun = PhysicalEntityImpl(2.0e30)
        val time = daySec * 1 //one day

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
        var earthFinal = earth.copy(gForceVector = gravitationalForceOnEntity(earth, sun))
        assert(earthFinal.gForceVector == shouldGForce)

        val shouldSpeedVector = Pair(earthFinal.speedVector.x + (earthFinal.gForceVector.x * time / earthFinal.mass), earthFinal.speedVector.y +(earthFinal.gForceVector.y * time / earthFinal.mass))
        earthFinal = earthFinal.copy(speedVector = speedVectorAfterTime(earthFinal, time))
        assert(earthFinal.speedVector == shouldSpeedVector)

        val newPos = Pair(earthFinal.position.x + (earthFinal.speedVector.x * time), earthFinal.position.y + (earthFinal.speedVector.y * time))
        earthFinal = earthFinal.copy(position = vectorChangeOfDisplacement(earthFinal, time))
        assert(earthFinal.position == newPos)

        Then("I'm able to move the entity in its new position, changing speed and gravity force on it")
        val shouldEarth = PhysicalEntityImpl(5.972e24, newPos, 29290, shouldSpeedVector, shouldGForce)
        assert(earthFinal === shouldEarth)
      }
    }
