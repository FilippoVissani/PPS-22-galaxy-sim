package physics.utils

case class Position(x: Int, y: Int)
case class Vector(x: Double, y: Double, z: Double)

class PhysicalEntity(var mass: Double, var position: Position = Position(0,0), var aphelionSpeed: Double = 100)

object PhysicalEntity:
  def apply(): PhysicalEntity = new PhysicalEntity(5)
  def apply(mass: Double, pos: Position): PhysicalEntity = new PhysicalEntity(mass, pos)
  def apply(mass: Double, pos: Position, speed: Double): PhysicalEntity = new PhysicalEntity(mass, pos, speed)

  def changeMass(entity: PhysicalEntity, newMass: Double): PhysicalEntity =
    PhysicalEntity(newMass, entity.position, entity.aphelionSpeed)

  def changePosition(entity: PhysicalEntity, newPosition: Position): PhysicalEntity =
    PhysicalEntity(entity.mass, newPosition, entity.aphelionSpeed)

  def changeSpeed(entity: PhysicalEntity, newspeed: Double): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, newspeed)
