package physics.dynamics

import org.w3c.dom.EntityReference
import physics.*
import math.{pow, sqrt, cbrt}

/**
 * @author Angela Cortecchia
 */

/**
 * Trait with different constants that can be useful
 */
trait Constants:
  val gravityConstant: Double = 6.6743e-11 //Nm^2/kg^2
  val daySec: Double = 24.0 * 60 * 60 //seconds in a day
  val deltaYear: Double = daySec * 365 //one year
  val moduleConstant: Double = 1.5
  val astronomicUnit: Double = 1.5e11 //meters, equals as earth-sun distance
  val lightYear: Double = 9.461e12 //63241.1 * astronomicUnit
  val solarMass: Double = 2.0e30 //unit reference for stars and black holes
  val earthMass: Double = 5.9722e24 //unit reference for planets and little objects

/**
 * Fields characteristics of an entity in order to calculate gravitation laws
 *
 * mass in kg
 *
 * position is a Pair(Double, Double)
 *
 * aphelionSpeed is in m/s
 *
 * speedVector is a Pair(0, aphelionSpeed)
 *
 * gForceVector is a Pair(Double, Double)
 */
trait PhysicalEntity:
  def mass: Mass
  def position: Position
  def aphelionSpeed: Speed
  def speedVector: SpeedVector
  def gForceVector: GravityForceVector

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
object GravitationLaws extends Constants:

  /**
   * Calculate the gravity constant between two entities
   * @param smallerEntityMass Mass, of the entity that orbits around another one
   * @param biggerEntityMass Mass, of the entity that has other entities that orbit around it
   * @return Double, the gravity constant between two entities
   */
  def entitiesGravitationalConstant(smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    gravityConstant * smallerEntityMass * biggerEntityMass

  /**
   * Calculate the distance between two entities
   * @param smallerEntity PhysicalEntity, is the entity that orbits around another one
   * @param biggerEntity PhysicalEntity, is the entity that has other entities that orbit around it
   * @return Position, the distance in a bi-dimensional space
   */
  def distanceBetweenTwoEntities(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): Position =
    Pair(smallerEntity.position.x - biggerEntity.position.x, smallerEntity.position.y - biggerEntity.position.y)

  /**
   * Calculate the module of a Position (type) that represent the distance between two entities
   * @param pos Position
   * @return Double
   */
  def moduleOfDistance(pos: Position): Double =
    pow(pow(pos.x, 2) + pow(pos.y, 2), moduleConstant)

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
   * Calculate entity's new velocity vector after some time
   * @param entity PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return SpeedVector, speed change after delta time
   */
  def calculateSpeedVectorAfterTime(entity: PhysicalEntity, deltaTime: Double): SpeedVector =
    Pair((entity.gForceVector.x * deltaTime) / entity.mass, (entity.gForceVector.y * deltaTime) / entity.mass)

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
   * @param biggerEntity PhysicalEntity, the entity around which other entities orbit
   * @param entities Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime Double, time passed
   * @return the new speed vector of the entity reference
   */
  def entityReferenceSpeedVectorAfterTime[A <: PhysicalEntity](biggerEntity: PhysicalEntity, entities: Set[A], deltaTime: Double): SpeedVector =
    val speedVector = calculateEntityReferenceSpeedVector(biggerEntity, entities, deltaTime)
    Pair(biggerEntity.speedVector.x + speedVector.x , biggerEntity.speedVector.y + speedVector.y)

  /**
   * Summary of the forces of the other entities that affect the entity reference's speed
   * @param biggerEntity PhysicalEntity, the entity around which other entities orbit
   * @param entities Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime Double, time passed
   * @return SpeedVector
   */
  def calculateEntityReferenceSpeedVector[A <: PhysicalEntity](biggerEntity: PhysicalEntity, entities: Set[A], deltaTime: Double): SpeedVector =
    Pair( - entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / biggerEntity.mass,
          - entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / biggerEntity.mass)
  /**
   * Calculate the magnitude of a vector
   * @param vector Pair[Double, Double]
   * @return double
   */
  def calculateMagnitude(vector: Pair[Double, Double]): Double =
    sqrt(pow(vector.x, 2) + pow(vector.y, 2))

  /**
   * Calculate the sphere of influence of an entity
   * @param smallerEntity Physical Entity, the entity that orbits the bigger one
   * @param biggerEntity Physical Entity, the entity around which the smaller orbtis
   * @return Double, the radius of the sphere of influence
   */
  def calculateSphereOfInfluence(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): Double =
    euclideanDistance(smallerEntity.position, biggerEntity.position) * pow(smallerEntity.mass, 0.4)

  /**
   * Calculate the radius of the sphere of influence with eccentricity of the orbit
   * @param semiMayorAxis Double, the bigger axis of the elliptical orbit
   * @param eccentricity Double, between [0,1] represent how much elliptical the orbit is
   * @param smallerEntityMass Mass, the mass of the smaller entity
   * @param biggerEntityMass Mass, the mass of the bigger entity
   * @return Double, the radius of the sphere of influence
   */
  def radiusSphereOfInfluenceWithEccentricity(semiMayorAxis: Double, eccentricity: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * (1 - eccentricity) * cbrt(smallerEntityMass / (biggerEntityMass * 3))

  /**
   * Calculate the radius of the sphere of influence without eccentricity of the orbit, means that the orbit it's circolar
   * @param semiMayorAxisDouble  , the bigger axis of the elliptical orbit
   * @param smallerEntityMass   Mass, the mass of the smaller entity
   * @param biggerEntityMass    Mass, the mass of the bigger entity
   * @return Double, the radius of the sphere of influence
   */
  def radiusSphereOfInfluence(semiMayorAxis: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * cbrt(smallerEntityMass / (biggerEntityMass * 3))

  /**
   * Calculate the euclidean distance of two points (entities) in 2 dimension (x, y)
   * @param pos1 Position of the entity 1
   * @param pos2 Position of the entity 2
   * @return the euclidean distance between the two positions
   */
  def euclideanDistance(pos1: Position, pos2: Position): Double =
    sqrt(pow(pos1.x - pos2.x, 2) + pow(pos1.y - pos2.y, 2))