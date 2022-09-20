package galaxy_sim.model

import scala.util.Random

object EntityGenerator:
  val random: Random = Random(100)

  def generateRandomStar(bound: Int): Star = Star(generateRandomBody(bound))

  def generateRandomPlanet(bound: Int): Planet = Planet(generateRandomBody(bound))

  def generateRandomAsteroid(bound: Int): Asteroid = Asteroid(generateRandomBody(bound))

  def generateRandomInterstellarCloud(bound: Int): InterstellarCloud = InterstellarCloud(generateRandomBody(bound))

  private def generateRandomBody(bound: Int): Body =
    Body(name = s"Star ${random.nextInt(bound)}",
      mass = random.nextInt(bound),
      volume = random.nextInt(bound),
      speed = Pair(random.nextInt(bound), random.nextInt(bound)),
      position = Pair(random.nextInt(1000), random.nextInt(1000)))
