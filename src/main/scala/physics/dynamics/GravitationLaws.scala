package physics.dynamics

import org.w3c.dom.EntityReference

import math.{pow, sqrt}

/**
 * Trait with different constants that can be useful
 */
trait Constants:
  val gravityCostant: Double = 6.67e-11
  val daySec: Double = 24.0 * 60 * 60
  val deltaYear: Double = daySec * 365
  val moduleConstant: Double = 1.5
  val astronomicUnit: Double = 1.5e11 //equals as earth-sun distance

object GravitationLaws extends Constants:

  /**
   *  NON VECTOR CALCULATIONS
   */

  /**
   * Calculate the gravity force between two entities
   * @param entitySubject is the entity you want to move
   * @param entityReference is the reference entity, usually is bigger than the subject, we consider it as still
   * @return double that represent the gravity force impact of the entity reference on the entity subject
   */
  def forceBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    accelerationBetweenTwoEntities(entitySubject, entityReference) * entitySubject.mass

  /**
   * Calculate the acceleration of the entity subject
   * @param entitySubject is the entity you want to move
   * @param entityReference is the reference entity, usually is bigger than the subject, we consider it as still
   * @return double that represent the acceleration of the entity subject
   */
  def accelerationBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    val distance: Double = distanceBetweenTwoEntities(entitySubject.position, entityReference.position)
    gravityCostant * (entityReference.mass / pow(distance, 2))

  /**
   * Calculate the speed of an entity after some time
   * @param accelleration is only related to the mass of the entity subject and the distance between it and the entity reference
   * @param deltaTime
   * @return double of the speed
   */
  def speedAfterDeltaTime(accelleration: Double, deltaTime: Double): Double =
    accelleration * deltaTime

  /**
   * Calculate the change of displacement of an entity
   * @param aphelionSpeed of the entity
   * @param deltaSpeed
   * @param deltaTime
   * @return
   */
  def changeOfDisplacement(aphelionSpeed: Double, deltaSpeed: Double, deltaTime: Double): Double =
    (aphelionSpeed + deltaSpeed) * deltaTime

  /**
   *
   * @param pos1
   * @param pos2
   * @return
   */
  def distanceBetweenTwoEntities(pos1: Position, pos2: Position): Double =
    sqrt(pow(pos1.x - pos2.x, 2) + pow(pos1.y - pos2.y, 2))

  /**
   *
   * @param changeOfDisplacement
   * @param entity
   * @return
   */
  def entityNewPosition(changeOfDisplacement: Double, entity: PhysicalEntity): Position = ???


  /**
   * VECTOR CALCULATIONS
   */

  /**
   * Calculate the gravity constant between two entities
   * @param entitySubject is the entity that orbits around another one
   * @param entityReference is the entity that has other entities that orbit around it
   * @return the gravity constant between two entities
   */
  def entitiesGravitationalConstant(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    gravityCostant * entitySubject.mass * entityReference.mass

  /**
   * Calculate the distance between two entities
   * @param entitySubject   is the entity that orbits around another one
   * @param entityReference is the entity that has other entities that orbit around it
   * @return the distance in a bi-dimensional space
   */
  def posBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Position =
    Position(entitySubject.position.x - entityReference.position.x, entitySubject.position.y - entityReference.position.y)

  /**
   * Calculate the module of the distance between two entities
   * @param pos
   * @return double
   */
  def moduleOfDistance(pos: Position): Double =
   pow(pow(pos.x, 2) + pow(pos.y, 2), moduleConstant)

  /**
   * Calculate the gravity force put on entity subject direction
   * @param entitySubject is the entity that orbits around another one
   * @param entityReference is the entity that has other entities that orbit around it
   * @return a vector that represent the gravity force of the entity subject in a bi-dimensional space
   */
  def gravityForceOnEntity(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): GravityForceVector =
    val distance = posBetweenTwoEntities(entitySubject, entityReference)
    val mod = moduleOfDistance(distance)
    val gravConstEntitySubj = negateGravConst(entitiesGravitationalConstant(entitySubject, entityReference))
    val gForce = GravityForceVector(gravConstEntitySubj * distance.x / mod, gravConstEntitySubj * distance.y / mod)
    gForce //will be saved into entity

  /**
   * calculate entity's new velocity vector after some time
   * @param entity is the entity subject that you want to move
   * @param deltaTime
   * @return
   */
  def speedVectorAfterTime(entity: PhysicalEntity, deltaTime: Double): SpeedVector =
    SpeedVector((entity.gForceVector.x * deltaTime / entity.mass) + entity.speedVector.x, (entity.gForceVector.y * deltaTime / entity.mass) + entity.speedVector.y)
  
  /**
   * calculate entity's new displacement after some time
   * @param entity is the entity subject that you want to move
   * @param deltaTime
   * @return Position as new displacement
   */
  def vectorChangeOfDisplacement(entity: PhysicalEntity, deltaTime: Double): Position =
    Position(entity.position.x + (entity.speedVector.x * deltaTime), entity.position.y + (entity.speedVector.y * deltaTime))

  private def negateGravConst(gravityConstant: Double): Double = -gravityConstant