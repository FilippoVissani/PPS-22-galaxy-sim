package physics.dynamics

import physics.{GravityForceVector, Mass, Pair, Position, SpeedVector}

import math.{cbrt, pow, sqrt}

object PhysicsFormulas extends Constants:
  /**
   * Calculate the gravity constant between two entities
   *
   * @param smallerEntityMass Mass, of the entity that orbits around another one
   * @param biggerEntityMass  Mass, of the entity that has other entities that orbit around it
   * @return Double, the gravity constant between two entities
   */
  def entitiesGravitationalConstant(smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    gravityConstant * smallerEntityMass * biggerEntityMass

  /**
   * Calculate the distance between two entities
   *
   * @param smallerEntity PhysicalEntity, is the entity that orbits around another one
   * @param biggerEntity  PhysicalEntity, is the entity that has other entities that orbit around it
   * @return Position, the distance in a bi-dimensional space
   */
  def distanceBetweenTwoEntities(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): Position =
    Pair(smallerEntity.position.x - biggerEntity.position.x, smallerEntity.position.y - biggerEntity.position.y)

  /**
   * Calculate the module of a Position (type) that represent the distance between two entities
   *
   * @param pos Position
   * @return Double
   */
  def moduleOfDistance(pos: Position): Double =
    pow(pow(pos.x, 2) + pow(pos.y, 2), moduleConstant)

  /**
   * Calculate the euclidean distance of two points (entities) in 2 dimension (x, y)
   *
   * @param pos1 Position of the entity 1
   * @param pos2 Position of the entity 2
   * @return the euclidean distance between the two positions
   */
  def euclideanDistance(pos1: Position, pos2: Position): Double =
    sqrt(pow(pos1.x - pos2.x, 2) + pow(pos1.y - pos2.y, 2))

  /**
   * Calculate the magnitude of a vector
   *
   * @param vector Pair[Double, Double]
   * @return double
   */
  def calculateMagnitude(vector: Pair): Double =
    sqrt(pow(vector.x, 2) + pow(vector.y, 2))

  /**
   * Calculate entity's new velocity vector after some time
   *
   * @param entity    PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return SpeedVector, speed change after delta time
   */
  def calculateSpeedVectorAfterTime(entity: PhysicalEntity, deltaTime: Double): SpeedVector =
    Pair((entity.gForceVector.x * deltaTime) / entity.mass, (entity.gForceVector.y * deltaTime) / entity.mass)

  /**
   * Calculate entity's displacement after some time
   *
   * @param entity    PhysicalEntity, is the entity subject that you want to move
   * @param deltaTime Double, time passed
   * @return Position that is the change of displacement
   */
  def calculateChangeOfDisplacement(entity: PhysicalEntity, deltaTime: Double): Position =
    Pair(entity.speedVector.x * deltaTime, entity.speedVector.y * deltaTime)

  /**
   * Summary of the forces of the other entities that affect the entity reference's speed
   *
   * @param biggerEntity PhysicalEntity, the entity around which other entities orbit
   * @param entities     Set[PhysicalEntity], set of the entities that orbits around the entity reference
   * @param deltaTime    Double, time passed
   * @return SpeedVector
   */
  def calculateEntityReferenceSpeedVector[A <: PhysicalEntity](biggerEntity: PhysicalEntity, entities: Set[A], deltaTime: Double): SpeedVector =
    Pair(-entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / biggerEntity.mass,
      -entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / biggerEntity.mass)

  /**
   * Calculate the sphere of influence of an entity
   *
   * @param smallerEntity Physical Entity, the entity that orbits the bigger one
   * @param biggerEntity  Physical Entity, the entity around which the smaller orbtis
   * @return Double, the radius of the sphere of influence
   */
  def calculateSphereOfInfluence(smallerEntity: PhysicalEntity, biggerEntity: PhysicalEntity): Double =
    euclideanDistance(smallerEntity.position, biggerEntity.position) * pow(smallerEntity.mass, 0.4)

  /**
   * Calculate the radius of the sphere of influence with eccentricity of the orbit
   *
   * @param semiMayorAxis     Double, the bigger axis of the elliptical orbit
   * @param eccentricity      Double, between [0,1] represent how much elliptical the orbit is
   * @param smallerEntityMass Mass, the mass of the smaller entity
   * @param biggerEntityMass  Mass, the mass of the bigger entity
   * @return Double, the radius of the sphere of influence
   */
  def radiusSphereOfInfluenceWithEccentricity(semiMayorAxis: Double, eccentricity: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * (1 - eccentricity) * cbrt(smallerEntityMass / (biggerEntityMass * 3))

  /**
   * Calculate the radius of the sphere of influence without eccentricity of the orbit, means that the orbit it's circolar
   *
   * @param semiMayorAxisDouble , the bigger axis of the elliptical orbit
   * @param smallerEntityMass   Mass, the mass of the smaller entity
   * @param biggerEntityMass    Mass, the mass of the bigger entity
   * @return Double, the radius of the sphere of influence
   */
  def radiusSphereOfInfluence(semiMayorAxis: Double, smallerEntityMass: Mass, biggerEntityMass: Mass): Double =
    semiMayorAxis * cbrt(smallerEntityMass / (biggerEntityMass * 3))

