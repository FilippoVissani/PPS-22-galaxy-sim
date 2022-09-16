package physics.dynamics

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import physics.utils.{PhysicalEntity, Position}
import math.{ pow }


class TestGravitationLaws extends AnyFunSuite with BeforeAndAfterAll:
  import GravitationLaws.*
  var earth: PhysicalEntity = PhysicalEntity()
  var sun: PhysicalEntity = PhysicalEntity()
  val earthPos: Position = Position(106, 106)
  val sunPos: Position = Position(0, 0)
  var dist: Double = 0.0
  var acc: Double = 0.0
  var force: Double = 0.0

  override protected def beforeAll(): Unit =
    val earthMass: Double = 5.972e24 //earth
    val earthSpeed: Double = 50.4
    earth = PhysicalEntity(earthMass, earthPos, earthSpeed)
    val sunMass: Double =  2.0e30 //sun
    val sunSpeed: Double = 77.4
    sun = PhysicalEntity(sunMass, sunPos, sunSpeed)


  test("Distance between two points"){
    dist = distanceBetweenTwoEntities(earth.position, sun.position)
    val dist2 = distanceBetweenTwoEntities(earthPos, sunPos)
    println("sun-earth distance " + dist)
    assert(dist == dist2)
  }

  test("Accelleration between two entities"){
    acc = accellerationBetweenTwoEntities(earth, sun)
    val shouldAcc = GravitationLaws.gravitationalCostant * (sun.mass / pow(dist, 2))
    println("sun-earth accelleration " + acc)
    assert(acc == shouldAcc)
  }

  test("force between two entities"){
    force = forceBetweenTwoEntities(earth, sun)
    val shouldForce = acc * earth.mass
    println("sun-earth force " + force)
    assert(force == shouldForce)
  }

  /*test("calculate speed of the entity after delta time"){
    speedAfterDeltaTime(???,???)
    assert(false)
  }

  test("calculate entity's new position"){
    calculateNewPosition()
    assert(false)
  }*/

