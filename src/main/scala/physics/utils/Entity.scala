package physics.utils

val gravitationalCostant: Double = 6.67e-11

case class Position(x: Int, y: Int)

class Entity(var mass: Float, var force: Float, var gravityForce: Float, var speed: Float = 0, var position: Position = Position(0,0), var accelleration: Float = 0)

object Entity:
  def apply(): Entity = new Entity(2, 2, 2)
  def apply(mass: Float, force: Float, gForce: Float): Entity = new Entity(mass, force, gForce)
  def apply(mass: Float, force: Float, gForce: Float, speed: Float, position: Position, accelleration: Float): Entity =
    new Entity(mass, force, gForce, speed, position, accelleration)

  def changeMass(entity: Entity, newMass: Float): Entity =
    Entity(newMass, entity.force, entity.gravityForce, entity.speed, entity.position, entity.accelleration)

  def changeForce(entity: Entity, newForce: Float): Entity =
    Entity(entity.mass, newForce, entity.gravityForce, entity.speed, entity.position, entity.accelleration)

  def changeGravityForce(entity: Entity, newGForce: Float): Entity =
    Entity(entity.mass, entity.force, newGForce, entity.speed, entity.position, entity.accelleration)

  def changespeed(entity: Entity, newspeed: Float): Entity =
    Entity(entity.mass, entity.force, entity.gravityForce, newspeed, entity.position, entity.accelleration)

  def changePosition(entity: Entity, newPosition: Position): Entity =
    Entity(entity.mass, entity.force, entity.gravityForce, entity.speed, newPosition, entity.accelleration)

  def changeAccelleration(entity: Entity, newValue: Float): Entity =
    Entity(entity.mass, entity.force, entity.gravityForce, entity.speed, entity.position, newValue)
