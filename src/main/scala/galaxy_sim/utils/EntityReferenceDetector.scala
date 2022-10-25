package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.CelestialBody
import physics.*
import physics.dynamics.GravitationLaws.*
import physics.dynamics.PhysicalEntity

import scala.language.postfixOps

trait EntityReferenceDetector:
  /**
   * Among a set of entities, calculate which one is the reference for a specific entity
   * @param entity Celestial Body, the entity that needs to know which one is its reference
   * @param entities Set[CelestialBody], set of all the existing entities
   * @return CelestialBody that is the entity's reference
   */
  def getReference(entity: CelestialBody, entities: Set[CelestialBody]): CelestialBody

object EntityReferenceDetector:
  def getReference(entity: CelestialBody, entities: Set[CelestialBody])(using detector: EntityReferenceDetector): CelestialBody =
    detector.getReference(entity, entities)

object EntityReferenceDetectors:
  given SphereOfInfluenceDetector: EntityReferenceDetector with
    override def getReference(entity: CelestialBody, entities: Set[CelestialBody]): CelestialBody =
      var refSOI: Option[Double] = Option.empty
      var refDistance: Option[Double] = Option.empty
      var reference = entity.copy()
      entities.filter(e => e.mass > entity.mass).foreach(e =>
        val distance = calculateMagnitude(distanceBetweenTwoEntities(entity, e))
        val rSOI = calculateSphereOfInfluence(entity, e)
        if distance <= rSOI &&
          (refDistance.isEmpty || distance < refDistance.get) &&
          (refSOI.isEmpty || refSOI.get > rSOI) then
            refSOI = Option(rSOI)
            refDistance = Option(distance)
            reference = e.copy()
      )
      reference