package galaxy_sim.utils

import galaxy_sim.model.SimulationConfig.*
import galaxy_sim.model.CelestialBody
import physics.*
import physics.dynamics.GravitationLaws.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.PhysicsFormulas.*

import scala.annotation.tailrec
import scala.language.postfixOps

trait EntityReferenceDetector:
  /**
   * Among a set of entities, calculate which one is the reference for a specific entity
   * @param entity Celestial Body, the entity that needs to know which one is its reference
   * @param entities Set[CelestialBody], set of all the existing entities
   * @return CelestialBody that is the entity's reference
   */
  def getReference(entity: CelestialBody, entities: Set[CelestialBody]): Option[CelestialBody]

object EntityReferenceDetector:
  def getReference(entity: CelestialBody, entities: Set[CelestialBody])(using detector: EntityReferenceDetector): Option[CelestialBody] =
    detector.getReference(entity, entities)

object EntityReferenceDetectors:
  given SphereOfInfluenceDetector: EntityReferenceDetector with
    override def getReference(entity: CelestialBody, entities: Set[CelestialBody]): Option[CelestialBody] =
      val list: List[(CelestialBody, Double, Double)] = entities.filter(b => entity.mass < b.mass).map(b => (b, euclideanDistance(entity.position, b.position), calculateSphereOfInfluence(entity, b))).filter((_, d, r) => d <= r).toList
      check(list)
    
    /**
     * Function to check which entity inside a list is the entity reference
     *
     * NB: the list must be without entities with less radius of influence than distance
     *
     * @param list list of (CelestialBody, Double, Double) where the first double must be the euclidean distance between two entities and the second one must be the radius sphere of influence of the same entities
     * @param ref is an option of a tuple(CelestialBody, distance, sphere of influence) which represent the actual reference
     * @return an option of CelestialBody, can also be empty
     */
    @tailrec
     private def check(list: List[(CelestialBody, Double, Double)], ref: Option[(CelestialBody, Double, Double)] = Option.empty): Option[CelestialBody] = ref match
       case r if r.isEmpty && list.nonEmpty => check(list.tail, Option(list.head._1, list.head._2, list.head._3))
       case r if r.nonEmpty && list.nonEmpty => list.head match
         case (b, d, r) if d < ref.get._2 && ref.get._3 > r => list.tail match
           case l if l.nonEmpty => check(l, Option(b, d, r))
           case _ => Option(b)
         case _ => check(list.tail, ref)
       case r if r.nonEmpty && list.isEmpty => Option(r.get._1)
       case _ => Option.empty
