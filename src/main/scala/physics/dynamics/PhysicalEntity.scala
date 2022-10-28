package physics.dynamics

import physics.*

/**
 * @author Angela Cortecchia
 */

/**
 * Fields characteristics of an entity in order to calculate gravitation laws
 *
 * mass in kg
 *
 * position is a Pair(Double, Double)
 *
 * speedVector is a Pair(0, speed)
 *
 * gForceVector is a Pair(Double, Double)
 */
trait PhysicalEntity:
  def mass: Mass
  def position: Position
  def speedVector: SpeedVector
  def gForceVector: GravityForceVector

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