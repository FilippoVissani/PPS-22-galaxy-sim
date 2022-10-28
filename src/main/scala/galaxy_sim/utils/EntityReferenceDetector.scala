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
      val list: List[(CelestialBody, Double, Double)] = entities.filter(e => e.mass > entity.mass).map(e => (e, euclideanDistance(entity.position, e.position), calculateSphereOfInfluence(entity, e))).filter((_, d, r) => d < r).toList
      check(list)

    /**
     * Function to check which entity inside a list is the entity reference
     *
     * NB: the list must be without entities with less radius of influence than distance
     *
     * @param list list of (CelestialBody, Double, Double) where the first double must be the euclidean distance between two entities and the second one must be the radius sphere of influence of the same entities
     * @param ref is a tuple of option which represent the actual reference
     * @return an option of CelestialBody, can also be empty
     */
    @tailrec
    private def check(list: List[(CelestialBody, Double, Double)], ref:(Option[CelestialBody], Option[Double], Option[Double]) = (Option.empty, Option.empty, Option.empty)): Option[CelestialBody] = ref match
      case (b, _, _) if b.isEmpty && list.nonEmpty => check(list.tail, (Option(list.head._1), Option(list.head._2), Option(list.head._3)))
      case  _ if list.nonEmpty => list.head match
        case (b, d, r) if d < ref._2.get && ref._3.get > r  => list.tail match
          case l if l.nonEmpty => check(list.tail, (Option(b), Option(d), Option(r)))
          case _ => ref._1
        case _ => check(list.tail, ref)
      case _ => ref._1