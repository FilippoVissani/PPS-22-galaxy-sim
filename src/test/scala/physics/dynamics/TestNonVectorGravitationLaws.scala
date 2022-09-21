package physics.dynamics

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import math.{pow, tanh}

class TestNonVectorGravitationLaws extends AnyFunSuite with BeforeAndAfterAll:
  import GravitationLaws.*

  val earthPos: Position = Position(106, 106)
  val sunPos: Position = Position(0, 0)
  val time: Double = 1 * daySec
  val earthSpeed: Double = 29290
  val earthMass: Double = 5.972e24 //earth
  val sunMass: Double = 2.0e30 //sun
  val sunSpeed: Double = 0.0

  var earth: PhysicalEntity = PhysicalEntity(earthMass, earthPos, earthSpeed)
  var sun: PhysicalEntity = PhysicalEntity(sunMass, sunPos, sunSpeed)

  var dist: Double = 0.0
  var acceleration: Double = 0.0
  var force: Double = 0.0
  var speed: Double = 0.0
  var changeDisplacement: Double = 0.0

  test("Distance between two points"){
    dist = distanceBetweenTwoEntities(earth.position, sun.position)
    val dist2 = distanceBetweenTwoEntities(earthPos, sunPos)
    println("sun-earth distance " + dist)
    assert(dist == dist2)
  }

  test("Accelleration between two entities"){
    acceleration = accelerationBetweenTwoEntities(earth, sun)
    val shouldAcc = GravitationLaws.gravityCostant * (sun.mass / pow(dist, 2))
    println("sun-earth accelleration " + acceleration)
    assert(acceleration == shouldAcc)
  }

  test("force between two entities"){
    force = forceBetweenTwoEntities(earth, sun)
    val shouldForce = acceleration * earth.mass
    println("sun-earth force " + force)
    assert(force == shouldForce)
  }

  test("calculate speed of the entity after delta time"){
    val shouldSpeed = acceleration * time
    speed = speedAfterDeltaTime(acceleration, time)
    println(s"speed of earth after $time seconds is $speed")
    assert(speed == shouldSpeed)
  }

  test("calculate change of displacement of entity"){
    changeDisplacement = changeOfDisplacement(earth.aphelionSpeed, speed, time)
    val shouldPos = (earth.aphelionSpeed + speed) * time
    assert(changeDisplacement == shouldPos)
  }

  test("calculate entity's new position"){
    entityNewPosition(changeDisplacement, earth)
    assert(false)
  }
