package physics.utils

case class Position(x: Int, y: Int)

class Entity(var mass: Float, var force: Float, var gravityForce: Float, var velocity: Float = 0, var position: Position = Position(0,0), var accelleration: Float = 0)

object Entity:
  def apply(): Entity = new Entity(2, 2, 2)
  def apply(mass: Float, force: Float, gForce: Float): Entity = new Entity(mass, force, gForce)
  def apply(mass: Float, force: Float, gForce: Float, velocity: Float, position: Position, accelleration: Float): Entity =
    new Entity(mass, force, gForce, velocity, position, accelleration)

