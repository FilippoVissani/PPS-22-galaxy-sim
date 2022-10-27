package physics.dynamics

import physics.{GravityForceVector, Mass, Pair, Position}

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