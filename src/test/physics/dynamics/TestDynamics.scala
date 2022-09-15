package physics.dynamics

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import physics.utils.{Entity, Position}

class TestDynamics extends AnyFunSuite with BeforeAndAfterAll: //Matchers
  import Entity.*

  val mass1: Float = 9.2
  val mass2: Float = 3.4
  val force1: Float = 4.3
  val force2: Float = 2.5
  val gForce1: Float = 4.7
  val gForce2: Float = 3.2
  var entity1: Entity = Entity()
  var entity2: Entity = Entity()

  test("creation of a general entity"){
    entity1 = Entity(mass1, force1, gForce1)
    assert(entity1.mass === mass1)
    assert(entity1.force === force1)
    assert(entity1.gravityForce === gForce1)
  }

  test("creation of a second entity and modify it's field with methods"){
    var entity2: Entity = Entity()
    entity2 = changeMass(entity2, mass2)
    entity2 = changeForce(entity2, force2)
    entity2 = changeGravityForce(entity2, gForce2)
    assert(entity2.mass === mass2)
    assert(entity2.force === force2)
    assert(entity2.gravityForce === gForce2)
  }

  test("assign position to entities"){
    val pos1 = Position(2,2)
    val pos2 = Position(5,5)
    entity1 = changePosition(entity1, pos1)
    entity2 = changePosition(entity2, pos2)
    assert(entity1.position === pos1)
    assert(entity2.position === pos2)
  }

