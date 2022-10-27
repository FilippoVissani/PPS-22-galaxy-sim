package physics.dynamics

import physics.*
import physics.dynamics.PhysicsFormulas.*

import math.{cbrt, pow, sqrt}

/**
 * @author Angela Cortecchia
 */

/**
 * Functions to calculate the essential gravitation laws implied in basic object's movements in 2D space.
 * Includes vector and non-vector calculations
 *
 * STEPS TO FOLLOW:
 *
 * 1) update the entity subject's gForceVector by using gravitationalForceOnEntity(smallerEntity, ...)
 *
 * 2) update the entity subject's speedVector by using speedVectorAfterTime(smallerEntity, ...)
 *
 * 3) update the entity subject's position by using vectorChangeOfDisplacement(smallerEntity, ...)
 *
 * - when done you should also update the entity reference's fields:
 *
 * 4) update the entity reference's speedVector by using biggerEntitySpeedVectorAfterTime(biggerEntity, ...)
 *
 * 5) update the entity reference's position by using vectorChangeOfDisplacement(biggerEntity, ...)
 */
object GravitationLaws:

  /**
   * Calculate the gravity force put on entity subject direction
   * @param smallerEntity PhysicalEntity, is the entity that orbits around another one
   * @param biggerEntity PhysicalEntity, is the entity that has other entities that orbit around it
   * @return a vector that represent the gravitational force of the entity subject in a bi-dimensional space
   */
  def gravitationalForceOnEntity(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): GravityForceVector =
    val distance = distanceBetweenTwoEntities(smallerEntity, biggerEntity)
    val mod = moduleOfDistance(distance)
    val gravConstEntitySubj = entitiesGravitationalConstant(smallerEntity.mass, biggerEntity.mass)
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
   * Calculate entity's new position after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return new Position to update entity with
   */
  def vectorChangeOfDisplacement(entity: PhysicalEntity, deltaTime: Double): Position =
    val displacement = calculateChangeOfDisplacement(entity, deltaTime)
    Pair(entity.position.x + displacement.x, entity.position.y + displacement.y)

  /**
   * Calculate the new speed vector of the entity reference
   * @param biggerEntity PhysicalEntity, the entity around which other entities orbit
   * @param entities Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime Double, time passed
   * @return the new speed vector of the entity reference
   */
  def entityReferenceSpeedVectorAfterTime[A <: PhysicalEntity](biggerEntity: PhysicalEntity, entities: Set[A], deltaTime: Double): SpeedVector =
    val speedVector = calculateEntityReferenceSpeedVector(biggerEntity, entities, deltaTime)
    Pair(biggerEntity.speedVector.x + speedVector.x , biggerEntity.speedVector.y + speedVector.y)
