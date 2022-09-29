package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import scala.util.Random
import physics.*
import physics.collisions.CollisionDetection.*

object CelestialBodyGenerator:
  val random: Random = Random(100)

  def generateRandomCelestialBody(bound: Int): CelestialBody =
    CelestialBody(mass = random.nextInt(bound).toDouble,
      aphelionSpeed = random.nextInt(bound).toDouble,
      gForceVector = Pair(random.nextInt(bound).toDouble, random.nextInt(bound).toDouble),
      speedVector = Pair(random.nextInt(bound).toDouble, random.nextInt(bound).toDouble),
      position = Pair(random.nextInt(1000).toDouble, random.nextInt(1000).toDouble),
      name = "Star1",
      birthTime = random.nextInt(bound).toDouble,
      radius = random.nextInt(bound).toDouble,
      temperature = random.nextInt(bound).toDouble)
