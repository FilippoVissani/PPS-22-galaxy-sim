package physics.dynamics

import physics.*
import math.{cbrt, pow, sqrt}

/**
 * Trait with different constants that can be useful
 */
trait Constants:
  val gravityConstant: Double = 6.6743e-11
  val daySec: Double = 24.0 * 60 * 60 //seconds in a day
  val deltaYear: Double = daySec * 365 //one year
  val moduleConstant: Double = 1.5
  val astronomicUnit: Double = 1.5e11 //equals as earth-sun distance
  val lightYear: Double = 9.461e12 //63241.1 * astronomicUnit
  val solarMass: Double = 2.0e30 //unit reference for stars and blackholes
  val earthMass: Double = 5.9722e24 //unit reference for planets and little objects

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
   * @param vector
   * @return
   */
  def calculateMagnitude(vector: Pair[Double, Double]): Double =
    sqrt(pow(vector.x, 2) + pow(vector.y, 2))

  /**
   * Calculate the sphere of influence of an entity
   * @param smallerEntity
   * @param biggerEntity
   * @return
   */
  def calculateSphereOfInfluence(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): Double =
    val distance = distanceBetweenTwoEntities(smallerEntity, biggerEntity)
    calculateMagnitude(distance) * pow(smallerEntity.mass, 0.4)

  /**
   * Calculate the radius of the sphere of influence with eccentricity of the orbit
   * @param semiMayorAxis
   * @param eccentricity
   * @param smallerEntityMass
   * @param biggerEntityMass
   * @return
   */
  def radiusSphereOfInfluenceWithEccentricity(semiMayorAxis: Double, eccentricity: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * (1 - eccentricity) * cbrt(smallerEntityMass / (biggerEntityMass * 3))

  /**
   * Calculate the radius of the sphere of influence without eccentricity of the orbit, means that the orbit it's circolar
   * @param semiMayorAxis
   * @param smallerEntityMass
   * @param biggerEntityMass
   * @return
   */
  def radiusSphereOfInfluence(semiMayorAxis: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * cbrt(smallerEntityMass / (biggerEntityMass * 3))