package galaxy_sim.model

import physics.GravityForceVector
import physics.Pair
import scala.util.Random

object CelestialBodyGenerator:
  val random: Random = Random(100)

  def generateRandomCelestialBody(bound: Int): CelestialBody = CelestialBody(name = "Star1",
    gForceVector = Pair(random.nextInt(bound), random.nextInt(bound)),
    speedVector = Pair(random.nextInt(bound), random.nextInt(bound)),
    mass = random.nextInt(bound),
    position = Pair(random.nextInt(1000), random.nextInt(1000)),
    aphelionSpeed = random.nextInt(bound),
    radius = random.nextInt(bound))
