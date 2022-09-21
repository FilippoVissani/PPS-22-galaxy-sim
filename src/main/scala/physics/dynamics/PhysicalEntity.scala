package physics.dynamics

case class Position(x: Double, y: Double)
case class SpeedVector(x: Double, y: Double)
case class GravityForceVector(x: Double, y: Double)

class PhysicalEntity(val mass: Double = 1000,
                     val position: Position = Position(0,0),
                     val aphelionSpeed: Double = 10000,
                     val speedVector: SpeedVector = SpeedVector(0, 0),
                     val gForceVector: GravityForceVector = GravityForceVector(0, 0))

object PhysicalEntity:
  def apply(): PhysicalEntity = new PhysicalEntity() //with default values
  def apply(mass: Double, pos: Position, aphelionSpeed: Double): PhysicalEntity = new PhysicalEntity(mass, pos, aphelionSpeed)
  def apply(mass: Double, pos: Position, aphelionSpeed: Double, speedVector: SpeedVector, gForceVector: GravityForceVector): PhysicalEntity =
    new PhysicalEntity(mass, pos, aphelionSpeed, speedVector, gForceVector)

  /**
   * update the mass of an entity
   * @param entity  type PhysicalEntity
   * @param newMass Double
   * @return a new type PhysicalEntity updated
   */
  def changeMass(entity: PhysicalEntity, newMass: Double): PhysicalEntity =
    PhysicalEntity(newMass, entity.position, entity.aphelionSpeed, entity.speedVector, entity.gForceVector)

  /**
   * update the position of an entity
   * @param entity PhysicalEntity
   * @param newPosition type Position
   * @return a new type PhysicalEntity updated
   */
  def changePosition(entity: PhysicalEntity, newPosition: Position): PhysicalEntity =
    PhysicalEntity(entity.mass, newPosition, entity.aphelionSpeed, entity.speedVector, entity.gForceVector)

  /**
   * update the aphelion speed of an entity
   * @param entity type  PhysicalEntity
   * @param newspeed type Double 
   * @return a new type PhysicalEntity updated
   */
  def changeAphelionSpeed(entity: PhysicalEntity, newspeed: Double): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, newspeed, entity.speedVector, entity.gForceVector)

  /**
   * update the speed vector of an entity
   * @param entity type PhysicalEntity
   * @param newSpeedVector type SpeedVector
   * @return a new type PhysicalEntity updated
   */
  def changeSpeedVector(entity: PhysicalEntity, newSpeedVector: SpeedVector): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, entity.aphelionSpeed, newSpeedVector, entity.gForceVector)

  /**
   * update the Gravity Force Vector of an entity
   * @param entity type PhysicalEntity
   * @param newForceVector type GravityForceVector
   * @return a new type PhysicalEntity updated
   */
  def changeGForceVector(entity: PhysicalEntity, newForceVector: GravityForceVector): PhysicalEntity =
    PhysicalEntity(entity.mass, entity.position, entity.aphelionSpeed, entity.speedVector, newForceVector)

