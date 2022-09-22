package galaxy_sim.model

import physics.GravityForceVector
import physics.Pair
import physics.dynamics.PhysicalEntity

import scala.util.Random

object CelestialBodyGenerator:
  val random: Random = Random(100)

  def generateRandomCelestialBody(bound: Int): CelestialBody =
    val body = PhysicalEntity(
      gForceVector = Pair(random.nextInt(bound), random.nextInt(bound)),
      speedVector = Pair(random.nextInt(bound), random.nextInt(bound)),
      mass = random.nextInt(bound),
      pos = Pair(random.nextInt(1000), random.nextInt(1000)),
      aphelionSpeed = random.nextInt(bound))
    CelestialBody(name = "Star1", radius = random.nextInt(bound), birthTime = random.nextInt(bound), physicalEntity = body)
