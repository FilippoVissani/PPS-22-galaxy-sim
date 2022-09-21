package galaxy_sim.model

import scala.util.Random

object EntityGenerator:
  val random: Random = Random(100)

  def generateRandomEntity(bound: Int): Entity = Entity(
    name = s"Star ${random.nextInt(bound)}",
    mass = random.nextInt(bound),
    volume = random.nextInt(bound),
    speed = Pair(random.nextInt(bound), random.nextInt(bound)),
    position = Pair(random.nextInt(1000), random.nextInt(1000)))
