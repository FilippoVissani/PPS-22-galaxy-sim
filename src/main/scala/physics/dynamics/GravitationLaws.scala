package physics.dynamics

import org.w3c.dom.EntityReference
import physics.*

import math.{pow, sqrt}

/**
 * Trait with different constants that can be useful
 */
trait Constants:
  val gravityConstant: Double = 6.67e-11
  val daySec: Double = 24.0 * 60 * 60 //seconds in a day
  val deltaYear: Double = daySec * 365 //one year
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
   * Calculate the gravity constant between two entities
   * @param entitySubjectMass Mass, of the entity that orbits around another one
   * @param entityReferenceMass Mass, of the entity that has other entities that orbit around it
   * @return Double, the gravity constant between two entities
   */
  def entitiesGravitationalConstant(entitySubjectMass: Mass, entityReferenceMass: Mass): Double =
    gravityConstant * entitySubjectMass * entityReferenceMass

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
    val gravConstEntitySubj = entitiesGravitationalConstant(entitySubject.mass, entityReference.mass)
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

  /**
   * Calculate the new speed vector of the entity reference
   * @param entityReference PhysicalEntity, the entity around which other entities orbit
   * @param entities Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime Double, time passed
   * @return the new speed vector of the entity reference
   */
  def entityReferenceSpeedVectorAfterTime(entityReference: PhysicalEntity, entities: Set[PhysicalEntity], deltaTime: Double): SpeedVector =
    val speedVector = calculateEntityReferenceSpeedVector(entityReference, entities, deltaTime)
    Pair(entityReference.speedVector.x + speedVector.x , entityReference.speedVector.y + speedVector.y)

  /**
   * Summary of the forces of the other entities that affect the entity reference's speed
   * @param entityReference PhysicalEntity, the entity around which other entities orbit
   * @param entities Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime Double, time passed
   * @return SpeedVector
   */
  def calculateEntityReferenceSpeedVector(entityReference: PhysicalEntity, entities: Set[PhysicalEntity], deltaTime: Double): SpeedVector =
    Pair( - entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / entityReference.mass,
          - entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / entityReference.mass)

  def moveEntitySubjectAfterTime[A <: PhysicalEntity](entitySubject: A, entityReference: A, deltaTime: Double): A =
    val gravityConstant = entitiesGravitationalConstant(entitySubject.mass, entityReference.mass)
    val gForce = gravitationalForceOnEntity(entitySubject, entityReference)
    val speedVector = speedVectorAfterTime(entitySubject, deltaTime)
    val position = vectorChangeOfDisplacement(entitySubject, deltaTime)
    ???

  def moveEntityReferenceAfterTime[A <: PhysicalEntity](entityReference: A, entities: Set[A], deltaTime: Double): A = ???
