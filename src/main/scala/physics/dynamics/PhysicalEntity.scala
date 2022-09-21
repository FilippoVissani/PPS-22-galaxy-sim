package physics.dynamics

import physics.*

trait PhysicalEntity:
  def mass: Mass
  def position: Position
  def aphelionSpeed: Speed
  def speedVector: SpeedVector
  def gForceVector: GravityForceVector


object PhysicalEntity:
  private case class PhysicalEntityImpl(override val mass: Mass = 1000,
                                        override val position: Position = Pair(0, 0),
                                        override val aphelionSpeed: Speed = 10000,
                                        override val speedVector: SpeedVector = Pair(0, 0),
                                        override val gForceVector: GravityForceVector = Pair(0, 0)) extends PhysicalEntity

  def apply(): PhysicalEntity = PhysicalEntityImpl() //with default values
  def apply(mass: Mass, pos: Position, aphelionSpeed: Speed): PhysicalEntity = PhysicalEntityImpl(mass, pos, aphelionSpeed)
  def apply(mass: Mass, pos: Position, aphelionSpeed: Speed, speedVector: SpeedVector, gForceVector: GravityForceVector): PhysicalEntity =
    PhysicalEntityImpl(mass, pos, aphelionSpeed, speedVector, gForceVector)

  /**
   * update the mass of an entity
   * @param entity  type PhysicalEntity
   * @param newMass Double
   * @return a new type PhysicalEntity updated
   */
  def changeMass(entity: PhysicalEntity, newMass: Mass): PhysicalEntity =
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
  def changeAphelionSpeed(entity: PhysicalEntity, newspeed: Speed): PhysicalEntity =
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

