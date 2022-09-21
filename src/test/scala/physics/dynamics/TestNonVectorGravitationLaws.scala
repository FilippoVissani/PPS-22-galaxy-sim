package physics.dynamics

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import physics.*

import math.{Pi, pow, sqrt, tanh}

class TestNonVectorGravitationLaws extends AnyFunSuite with BeforeAndAfterAll:
  import GravitationLaws.*

  val earthPos: Position = Pair(106, 106)
  val sunPos: Position = Pair(0, 0)
  val time: Double = 1 * daySec //one day
  val earthMass: Mass = 5.972e24 //earth
  val earthSpeed: Speed = 29290
  val sunMass: Mass = 2.0e30 //sun
  val sunSpeed: Speed = 0.0

  var earth: PhysicalEntity = PhysicalEntity(earthMass, earthPos, earthSpeed)
  var sun: PhysicalEntity = PhysicalEntity(sunMass, sunPos, sunSpeed)

  var dist: Double = 0.0
  var acceleration: Speed = 0.0
  var force: GForce = 0.0
  var speed: Speed = 0.0
  var changeDisplacement: Double = 0.0

  test("Distance between two points"){
    dist = distanceBetweenTwoEntities(earth, sun)
    val dist2: Double = sqrt(pow(earthPos.x - sunPos.x, 2) + pow(earthPos.y - sunPos.y, 2))
    println("sun-earth distance " + dist)
    assert(dist === dist2)
  }

  test("Accelleration between two entities"){
    acceleration = accelerationBetweenTwoEntities(earth, sun)
    val shouldAcc: Speed = GravitationLaws.gravityConstant * (sun.mass / pow(dist, 2))
    println("sun-earth accelleration " + acceleration)
    assert(acceleration === shouldAcc)
  }

  test("force between two entities"){
    force = forceBetweenTwoEntities(earth, sun)
    val shouldForce: Speed = acceleration * earth.mass
    println("sun-earth force " + force)
    assert(force === shouldForce)
  }

  test("calculate speed of the entity after delta time"){
    val shouldSpeed: Speed = acceleration * time
    speed = speedAfterDeltaTime(acceleration, time)
    println(s"speed of earth after $time seconds is $speed")
    assert(speed === shouldSpeed)
  }

  test("calculate change of displacement of entity"){
    changeDisplacement = changeOfDisplacement(earth.aphelionSpeed, speed, time)
    val shouldPos = (earth.aphelionSpeed + speed) * time
    assert(changeDisplacement === shouldPos)
  }
