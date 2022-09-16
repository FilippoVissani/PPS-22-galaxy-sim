package physics.dynamics

import physics.utils.{PhysicalEntity, Position}
import math.{ sqrt, pow }

trait Constants:
  val gravitationalCostant: Double = 6.67e-11
  val daySec: Double = 24.0 * 60 * 60

object GravitationLaws extends Constants:

  def forceBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    accellerationBetweenTwoEntities(entitySubject, entityReference) * entitySubject.mass

  def accellerationBetweenTwoEntities(entitySubject: PhysicalEntity, entityReference: PhysicalEntity): Double =
    val distance: Double = distanceBetweenTwoEntities(entitySubject.position, entityReference.position)
    gravitationalCostant * (entityReference.mass / pow(distance, 2))

  def speedAfterDeltaTime(accelleration: Double, time: Double): Double =
    accelleration * time

  def calculateNewPosition(): Position = ???

  def distanceBetweenTwoEntities(pos1: Position, pos2: Position): Double =
    sqrt(pow(pos1.x - pos2.x, 2) + pow(pos1.y - pos2.y, 2))

  def gravitationalForceOnEntity(entity: PhysicalEntity): Double = ???