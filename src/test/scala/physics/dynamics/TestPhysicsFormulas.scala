package physics.dynamics

import physics.*
import org.scalatest.funsuite.AnyFunSuite
import physics.dynamics.GravitationLaws.*
import physics.dynamics.PhysicsFormulas.*
import physics.dynamics.Utils.*

import scala.math.{cbrt, pow, sqrt}

class TestPhysicsFormulas extends AnyFunSuite:
  var earthFinal: PhysicalEntityImpl = earth.copy()

  test("Euclidean distance between Earth and Sun") {
    val euDist = euclideanDistance(earth.position, sun.position)
    val shouldEuDist = sqrt(pow(earth.position.x - sun.position.x, 2) + pow(earth.position.y - sun.position.y, 2))
    assert(euDist == shouldEuDist)
  }

  test("Radius of the sphere of influence without eccentricity") {
    val rSOI = radiusSphereOfInfluence(semiMayorAxis, earth.mass, sun.mass)
    val shouldSOI = semiMayorAxis * cbrt(earth.mass / (sun.mass * 3))
    assert(rSOI == shouldSOI)
  }

  test("Radius of the sphere of influence with eccentricity") {
    val rSOI = radiusSphereOfInfluenceWithEccentricity(semiMayorAxis, earthEccentricity, earth.mass, sun.mass)
    val shouldSOI = semiMayorAxis * (1 - earthEccentricity) * cbrt(earth.mass / (sun.mass * 3))
    assert(rSOI == shouldSOI)
  }

  test("Distance between two entities, its module and magnitude"){
    val shouldDistance = Pair(earth.position.x - sun.position.x, earth.position.y - sun.position.y)
    val sunEarthDistance = distanceBetweenTwoEntities(earth, sun)
    assert(sunEarthDistance == shouldDistance)

    val shouldMod = pow(pow(sunEarthDistance.x, 2) + pow(sunEarthDistance.y, 2), moduleConstant)
    val module = moduleOfDistance(sunEarthDistance)
    assert(module == shouldMod)

    val magnitude = calculateMagnitude(sunEarthDistance)
    val shouldMagnitude = sqrt(pow(sunEarthDistance.x, 2) + pow(sunEarthDistance.y, 2))
    assert(magnitude == shouldMagnitude)
  }

  test("Gravitational constant between two entities"){
    val shouldGravConst = gravityConstant * earth.mass * sun.mass
    val earthGravConst = entitiesGravitationalConstant(earth.mass, sun.mass)
    assert(earthGravConst == shouldGravConst)
  }

  test("Calculate speed vector"){
    val earthSpeedVector = calculateSpeedVectorAfterTime(earth, deltaTime)
    val shouldSpeedVector = (earth.gForceVector.x * deltaTime / earth.mass,
                            earth.gForceVector.y * deltaTime / earth.mass)
    assert(earthSpeedVector.x == shouldSpeedVector._1 && earthSpeedVector.y == shouldSpeedVector._2)
  }

  test("Calculate change of displacement"){
    val earthChangeOfDisplacement = calculateChangeOfDisplacement(earth, deltaTime)
    val shouldChangeOfDisplacement = (earth.speedVector.x * deltaTime, earth.speedVector.y * deltaTime)
    assert(earthChangeOfDisplacement.x == shouldChangeOfDisplacement._1 && earthChangeOfDisplacement.y == shouldChangeOfDisplacement._2)
  }

  test("Calculate entity reference speed vector"){
    val entities = Set(earth)
    val sunSpeedVector = calculateEntityReferenceSpeedVector(sun, entities, deltaTime)
    val shouldSpeedVector = (- entities.iterator.map(e => e.gForceVector.x).sum * deltaTime / sun.mass,
                             - entities.iterator.map(e => e.gForceVector.y).sum * deltaTime / sun.mass)
    assert(sunSpeedVector.x == shouldSpeedVector._1 && sunSpeedVector.y == shouldSpeedVector._2)
  }

  test("Calculate radius of the sphere of influence"){
    val rSOI = calculateSphereOfInfluence(earth, sun)
    val shouldSOI = sqrt(pow(earth.position.x - sun.position.x, 2) + pow(earth.position.y - sun.position.y, 2)) * pow(earth.mass, 0.4)
    assert(rSOI == shouldSOI)
  }



