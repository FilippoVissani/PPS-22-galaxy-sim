package galaxy_sim.model

import galaxy_sim.model.SimulationConfig.{blackHole, bounds, earth, moon, sun}
import physics.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.GravitationLaws.*

import javax.security.auth.Subject
import scala.language.postfixOps

trait EntityReferenceDetector:
  def getReference(entity: CelestialBody, entities: Set[CelestialBody]): CelestialBody

object EntityReferenceDetector:
  def getReference(entity: CelestialBody, entities: Set[CelestialBody])(using detector: EntityReferenceDetector): CelestialBody =
    detector.getReference(entity, entities)

object EntityReferenceDetectors:
  given SphereOfInfluenceDetector: EntityReferenceDetector with
    override def getReference(entity: CelestialBody, entities: Set[CelestialBody]): CelestialBody =
      var refSOI = 0.0
      var refDistance = 0.0
      var reference = entity.copy()
      entities.filter(e => e.mass > entity.mass).foreach(e =>
        val distance = calculateMagnitude(distanceBetweenTwoEntities(entity, e))
        val rSOI = calculateSphereOfInfluence(entity, e)
        if distance <= rSOI &&
          (distance < refDistance || refDistance == 0) &&
          (refSOI > rSOI || refSOI == 0) then
            refSOI = rSOI
            refDistance = distance
            reference = e.copy()
      )
      reference


object test extends App:
  import EntityReferenceDetector.*
  import EntityReferenceDetectors.given
  val entities = Set(sun, earth, moon, blackHole)

  println("TRYING CHOOSE REFERENCE")
  entities.foreach( e =>
    val ref = getReference(e, entities)
    println(s">>>>${e.name.toUpperCase}'s reference is ${ref.name.toUpperCase}\n")
  )