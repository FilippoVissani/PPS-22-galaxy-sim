package physics.dynamics

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import physics.*

class TestPhysicalEntity extends AnyFunSuite with BeforeAndAfterAll: //Matchers
  import PhysicalEntity.*

  val mass1: Mass = 9.2
  val mass2: Mass = 3.4
  val pos1: Position = Pair(2,3)
  val pos2: Position = Pair(5,6)
  val speed1: Speed = 100.5
  val speed2: Speed = 90.5
  var entity1: PhysicalEntity = PhysicalEntity()
  var entity2: PhysicalEntity = PhysicalEntity()

  test("creation of a general entity"){
    entity1 = PhysicalEntity(mass1, pos1, speed1)
    assert(entity1.mass === mass1)
    assert(entity1.position === pos1)
    assert(entity1.aphelionSpeed === speed1)
  }

  test("change mass of second entity"){
    entity2 = changeMass(entity2, mass2)
    assert(entity2.mass === mass2)
  }

  test("change position of second entity"){
    entity2 = changePosition(entity2, pos2)
    assert(entity2.position === pos2)
  }

  test("change aphelion speed of second entity"){
    entity2 = changeAphelionSpeed(entity2, speed2)
    assert(entity2.aphelionSpeed === speed2)

  }

  test("assign new position to both entities"){
    val pos1: Position = Pair(2,2)
    val pos2: Position= Pair(5,5)
    entity1 = changePosition(entity1, pos1)
    entity2 = changePosition(entity2, pos2)
    assert(entity1.position === pos1)
    assert(entity2.position === pos2)
  }

  test("assign speed vector to both entities"){
    val sVec1: SpeedVector = Pair(10,20)
    val sVec2: SpeedVector = Pair(20,30)
    entity1 = changeSpeedVector(entity1, sVec1)
    entity2 = changeSpeedVector(entity2, sVec2)
    assert(entity1.speedVector == sVec1)
    assert(entity2.speedVector == sVec2)
  }

  test("assign gravity force vector to both entities"){
    val gForceVec1: GravityForceVector = Pair(10,20)
    val gForceVec2: GravityForceVector = Pair(40,30)
    entity1 = changeGForceVector(entity1, gForceVec1)
    entity2 = changeGForceVector(entity2, gForceVec2)
    assert(entity1.gForceVector == gForceVec1)
    assert(entity2.gForceVector == gForceVec2)
  }