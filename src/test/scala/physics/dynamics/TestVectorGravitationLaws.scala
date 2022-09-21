package physics.dynamics

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import scala.math.{pow, tan, tanh}

class TestVectorGravitationLaws extends AnyFunSuite with BeforeAndAfterAll:
  import GravitationLaws.*
  import PhysicalEntity.*

  val earthPos: Position = Position(106, 106)
  val sunPos: Position = Position(0, 0)
  val time: Double = 1 * daySec //one day
  val earthSpeed: Double = 29290
  val earthMass: Double = 5.972e24 //earth
  val sunMass: Double = 2.0e30 //sun
  val sunSpeed: Double = 0.0

  var earth: PhysicalEntity = PhysicalEntity(earthMass, earthPos, earthSpeed)
  var sun: PhysicalEntity = PhysicalEntity(sunMass, sunPos, sunSpeed)
  var gravConstEarth: Double = 0.0
  var sunEarthPos: Position = Position(0,0)
  var module: Double = 0.0
  var gForceEarth: GravityForceVector = GravityForceVector(0,0)
  var earthSpeedVector: SpeedVector = SpeedVector(0,0)
  var changeDisplacement: Position = Position(0,0)

  override protected def beforeAll(): Unit =
    earth = changePosition(earth, Position(astronomicUnit, 0))
    earth = changeSpeedVector(earth, SpeedVector(0,earth.aphelionSpeed))
  
  test("calculate position between two entities"){
    val shouldPos = Position(earth.position.x - sun.position.x, earth.position.y - sun.position.y)
    sunEarthPos = posBetweenTwoEntities(earth, sun)
    println(s"sun-earth distance is ${sunEarthPos.toString}")
    assert(sunEarthPos === shouldPos)
  }
  
  test("calculate module of sun-earth distance"){
    val shouldMod = pow(pow(sunEarthPos.x, 2) + pow(sunEarthPos.y, 2), moduleConstant)
    module = moduleOfDistance(sunEarthPos)
    println(s"module is $module")
    assert(module === shouldMod)
  }
  
  test("calculate gravity constant between earth and sun"){
    val shouldGravConst = gravityCostant * earth.mass * sun.mass
    gravConstEarth = entitiesGravitationalConstant(earth, sun)
    println(s"sun-earth gravity constant is $gravConstEarth")
    assert(gravConstEarth === shouldGravConst)
  }
  
  test("calculate the gravity force put on the earth's direction"){
    val shouldGForce = GravityForceVector(-gravConstEarth * sunEarthPos.x / module, -gravConstEarth * sunEarthPos.y / module)
    gForceEarth = gravitationalForceOnEntity(earth, sun)
    earth = changeGForceVector(earth, gForceEarth)
    println(s"earth gforce ${earth.gForceVector.toString}")
    assert(earth.gForceVector === shouldGForce)
  }
  
  test("calculate speed vector after some time"){
    val speedVector = calculateSpeedVectorAfterTime(earth, time)
    val shouldSpeedVector = SpeedVector(earth.gForceVector.x * time / earth.mass, earth.gForceVector.y * time / earth.mass)
    assert(speedVector === shouldSpeedVector)
  }
  
  test("calculate earth new speed vector after some time "){
    val shouldSpeedVector = SpeedVector(earth.speedVector.x + (earth.gForceVector.x * time / earth.mass), earth.speedVector.y + (earth.gForceVector.y * time / earth.mass))
    println(s"earth mass ${earth.mass}")
    earthSpeedVector = speedVectorAfterTime(earth, time)
    earth = changeSpeedVector(earth, earthSpeedVector)
    println(s"earth speed vector ${earth.speedVector.toString}")
    assert(earth.speedVector === shouldSpeedVector)
  }
  
  test("calculate change of displacement"){
    val displacement = calculateChangeOfDisplacement(earth, time)
    val shouldDisplacement = Position(earth.speedVector.x * time, earth.speedVector.y * time)
    assert(displacement === shouldDisplacement)
  }
  
  test("calculate vector change of displacement"){
    val shouldChangeDisplacement = Position(earth.position.x + (earth.speedVector.x * time), earth.position.y + (earth.speedVector.y * time))
    println(s"earth was at pos ${earth.position.toString}")
    changeDisplacement = vectorChangeOfDisplacement(earth, time)
    earth = changePosition(earth, changeDisplacement)
    println(s"earth now is at pos ${earth.position.toString}")
    assert(changeDisplacement === shouldChangeDisplacement)
  }