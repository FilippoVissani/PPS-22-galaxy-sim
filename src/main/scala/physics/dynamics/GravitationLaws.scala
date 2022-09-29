package physics.dynamics

import org.w3c.dom.EntityReference
import physics.*

import math.{pow, sqrt}

/**
 * Trait with different constants that can be useful
 */
trait Constants:
  val gravityConstant: Double = 6.67e-11
  val daySec: Double = 24.0 * 60 * 60
  val deltaYear: Double = daySec * 365
  val moduleConstant: Double = 1.5
  val astronomicUnit: Double = 1.5e11 //equals as earth-sun distance

/**
 * Fields characteristics of an entity in order to calculate gravitation laws
 */
trait PhysicalEntity:
  def mass: Mass
  def position: Position
  def aphelionSpeed: Speed
  def speedVector: SpeedVector
  def gForceVector: GravityForceVector
  
/**
 * Functions to calculate the essential gravitation laws implied in basic object's movements in space.
 * Includes vector and non-vector calculations
 */
object GravitationLaws extends Constants:

  /**
   *  NON VECTOR CALCULATIONS
   */

  /**
   * Calculate the gravity force between two entities
   * @param entitySubject PhysicalEntity, is the entity you want to move
   * @param entityReference PhysicalEntity, is the reference entity, usually is bigger than the subject, we consider it as still
   * @return Double, represent the gravity force impact of the entity reference on the entity subject
   */
  def forceBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    accelerationBetweenTwoEntities(entitySubject, entityReference) * entitySubject.mass

  /**
   * Calculate the acceleration of the entity subject
   * @param entitySubject PhysicalEntity, is the entity you want to move
   * @param entityReference PhysicalEntity, is the reference entity, usually is bigger than the subject, we consider it as still
   * @return Double, represent the acceleration of the entity subject
   */
  def accelerationBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    val distance: Double = distanceBetweenTwoEntities(entitySubject, entityReference)
    gravityConstant * (entityReference.mass / pow(distance, 2))

  /**
   * Calculate the speed of an entity after some time
   * @param accelleration is only related to the mass of the entity subject and the distance between it and the entity reference
   * @param deltaTime Double, time passed
   * @return Double, the speed after time
   */
  def speedAfterDeltaTime(accelleration: Double, deltaTime: Double): Double =
    accelleration * deltaTime

  /**
   * Calculate the change of displacement of an entity
   * @param aphelionSpeed Double, aphelion speed of the entity
   * @param deltaSpeed Double, space covered
   * @param deltaTime Double, time passed
   * @return Double, space to add to previous position
   */
  def changeOfDisplacement(aphelionSpeed: Double, deltaSpeed: Double, deltaTime: Double): Double =
    (aphelionSpeed + deltaSpeed) * deltaTime

  /**
   * Calculate the distance between two points that are the two entities
   * @param entitySubject PhysicalEntity, entity that orbits around another one
   * @param entityReference PhysicalEntity, entity that has other entities that orbit around it
   * @return Double, distance between them
   */
  def distanceBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    sqrt(pow(entitySubject.position.x - entityReference.position.x, 2) + pow(entitySubject.position.y - entityReference.position.y, 2))


  /**
   * VECTOR CALCULATIONS
   */

  /**
   * Calculate the gravity constant between two entities
   * @param entitySubject PhysicalEntity, is the entity that orbits around another one
   * @param entityReference PhysicalEntity, is the entity that has other entities that orbit around it
   * @return Double, the gravity constant between two entities
   */
  def entitiesGravitationalConstant(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    gravityConstant * entitySubject.mass * entityReference.mass

  /**
   * Calculate the distance between two entities
   * @param entitySubject PhysicalEntity, is the entity that orbits around another one
   * @param entityReference PhysicalEntity, is the entity that has other entities that orbit around it
   * @return Position, the distance in a bi-dimensional space
   */
  def posBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Position =
    Pair(entitySubject.position.x - entityReference.position.x, entitySubject.position.y - entityReference.position.y)

  /**
   * Calculate the module of a Position (type) that represent the distance between two entities
   * @param pos Position
   * @return Double
   */
  def moduleOfDistance(pos: Position): Double =
   pow(pow(pos.x, 2) + pow(pos.y, 2), moduleConstant)

  /**
   * Calculate the gravity force put on entity subject direction
   * @param entitySubject PhysicalEntity, is the entity that orbits around another one
   * @param entityReference PhysicalEntity, is the entity that has other entities that orbit around it
   * @return a vector that represent the gravitational force of the entity subject in a bi-dimensional space
   */
  def gravitationalForceOnEntity(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): GravityForceVector =
    val distance = posBetweenTwoEntities(entitySubject, entityReference)
    val mod = moduleOfDistance(distance)
    val gravConstEntitySubj = entitiesGravitationalConstant(entitySubject, entityReference)
    Pair(- gravConstEntitySubj * distance.x / mod, - gravConstEntitySubj * distance.y / mod)

  /**
   * Calculate entity's new velocity vector after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return new SpeedVector to update entity with
   */
  def speedVectorAfterTime(entity: PhysicalEntity, deltaTime: Double): SpeedVector =
    val speedVector = calculateSpeedVectorAfterTime(entity, deltaTime)
    Pair(entity.speedVector.x + speedVector.x, entity.speedVector.y + speedVector.y)

  /**
   * Calculate entity's new velocity vector after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return SpeedVector, speed change after delta time
   */
  def calculateSpeedVectorAfterTime(entity: PhysicalEntity, deltaTime: Double): SpeedVector =
    Pair(entity.gForceVector.x * deltaTime / entity.mass, entity.gForceVector.y * deltaTime / entity.mass)

  /**
   * Calculate entity's new position after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return new Position to update entity with
   */
  def vectorChangeOfDisplacement(entity: PhysicalEntity, deltaTime: Double): Position =
    val displacement = calculateChangeOfDisplacement(entity, deltaTime)
    Pair(entity.position.x + displacement.x, entity.position.y + displacement.y)

  /**
   * Calculate entity's displacement after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return Position that is the change of displacement
   */
  def calculateChangeOfDisplacement(entity: PhysicalEntity, deltaTime: Double): Position =
    Pair(entity.speedVector.x * deltaTime, entity.speedVector.y * deltaTime)
