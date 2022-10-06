package galaxy_sim.model

import galaxy_sim.model.SimulationConfig.{blackHole, bounds, earth, moon, sun}
import physics.*
import physics.dynamics.PhysicalEntity
import physics.dynamics.GravitationLaws.*

import javax.security.auth.Subject
import scala.language.postfixOps

object EntityReferenceDetector:
    def getReference(entity: CelestialBody, entities: Set[CelestialBody]): CelestialBody =
      var gForce1: GravityForceVector = Pair(0,0)
      var magnitude1: Double = 0
      var reference: CelestialBody = entity.copy()
      var rSOI: Double = 0
//      println(s"===== subject ${entity.name.toUpperCase} with mass ${entity.mass}")
      entities.filter(e => e.name != entity.name).filter(e =>
//          println(s" - ${e.name} has mass ${e.mass}")
          entity.mass < e.mass).foreach(e =>
        gForce1 = gravitationalForceOnEntity(entity, e)
        val magnitude2 = calculateMagnitude(gForce1)
        val rSphereOfInfluence = calculateSphereOfInfluence(entity, e)
        val distance = calculateMagnitude(distanceBetweenTwoEntities(entity, e))
        println(s"distance is $distance - roi is $rSphereOfInfluence")
//        println(s"\tis ${e.name.toUpperCase} reference?")
//        println(s" gforce between ${entity.name.toUpperCase} and ${e.name} is $gForce1")
//        println(s"\tsphere of influence is $rSphereOfInfluence")
//        println(s"\tmagnitude2 between them is $magnitude2")
//        println(s"\t - magnitude1 < magnitude2 " +(magnitude1 < magnitude2))
//        println(s"\t - rSphereOfInfluence < rSOI " +(rSphereOfInfluence < rSOI || rSOI == 0))
        if magnitude1 < magnitude2 && (rSphereOfInfluence < rSOI || rSOI == 0) then //maybe not always right that m1 should be less than m2
            magnitude1 = magnitude2
            rSOI = rSphereOfInfluence
//            println(s"\t - actually ${e.name} is ref")
            reference = e.copy()
//            println(s"${entity.name} reference will be ${e.name.toUpperCase} for $gForce1")
        else
          reference
      )
      reference

object test extends App with ModelModule.Interface:
  val erd = EntityReferenceDetector
  val entities = Set(sun, earth, moon)
  var actualSimulation: Simulation = Simulation(celestialBodies = entities, bounds = bounds, deltaTime = 10)

  val model = ModelImpl()
  (1 until 10 by 1).foreach(_ =>
    model.incrementVirtualTime()
    model.moveCelestialBodiesToNextPosition()
  )

//  actualSimulation.celestialBodies.foreach( e =>
//    val ref: CelestialBody = erd.getReference(e, entities)
//    println(s">>>> entity ${e.name} has reference ${ref.name}")
//  )

/*// if gForce1.x.abs >= e.gForceVector.x then
       /*magnitude1 <= magnitude2 ||
                 magnitude1 == 0 ||*/*/
