package galaxy_sim.model

import physics.GravityForceVector
import physics.Pair
import physics.dynamics.PhysicalEntity
import scala.util.Random

object CelestialBodyGenerator:
  val random: Random = Random(100)

  def generateRandomCelestialBody(bound: Int): CelestialBody =
    val physicalEntity = PhysicalEntity(
      gForceVector = Pair(random.nextInt(bound), random.nextInt(bound)),
      speedVector = Pair(random.nextInt(bound), random.nextInt(bound)),
      mass = random.nextInt(bound),
      pos = Pair(random.nextInt(1000), random.nextInt(1000)),
      aphelionSpeed = random.nextInt(bound))
    val body: Body = Body(physicalEntity, random.nextInt(bound), random.nextInt(bound))
    CelestialBody("Star1", random.nextInt(bound), body)
