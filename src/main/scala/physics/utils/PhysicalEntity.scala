package physics.utils

case class Position(x: Double, y: Double)
case class SpeedVector(x: Double, y: Double, z: Double)
case class GravitationalForceVector(x: Double, y: Double)

class PhysicalEntity(val mass: Double = 1000,
                     val position: Position = Position(0,0),
                     val aphelionSpeed: Double = 10000,
                     val speedVector: SpeedVector = SpeedVector(0, 0, 0),
                     val gForceVector: GravitationalForceVector = GravitationalForceVector(0, 0))

object PhysicalEntity:
  def apply(): PhysicalEntity = new PhysicalEntity() //with default values
  def apply(mass: Double, pos: Position, aphelionSpeed: Double): PhysicalEntity = new PhysicalEntity(mass, pos, aphelionSpeed)
  def apply(mass: Double, pos: Position, aphelionSpeed: Double, speedVector: SpeedVector, gForceVector: GravitationalForceVector): PhysicalEntity =
    new PhysicalEntity(mass, pos, aphelionSpeed, speedVector, gForceVector)

  def changeMass(entity: PhysicalEntity, newMass: Double): PhysicalEntity =
    PhysicalEntity(newMass, entity.position, entity.aphelionSpeed, entity.speedVector, entity.gForceVector)

  def changePosition(entity: PhysicalEntity, newPosition: Position): PhysicalEntity =
    PhysicalEntity(entity.mass, newPosition, entity.aphelionSpeed, entity.speedVector, entity.gForceVector)

  def changeAphelionSpeed(entity: PhysicalEntity, newspeed: Double): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, newspeed, entity.speedVector, entity.gForceVector)

  def changeSpeedVector(entity: PhysicalEntity, newSpeedVector: SpeedVector): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, entity.aphelionSpeed, newSpeedVector, entity.gForceVector)

  def changeGForceVector(entity: PhysicalEntity, newForceVector: GravitationalForceVector): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, entity.aphelionSpeed, entity.speedVector, newForceVector)

