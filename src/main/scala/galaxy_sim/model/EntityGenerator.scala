package galaxy_sim.model

import scala.util.Random

object EntityGenerator:
  val random: Random = Random(100)
  def generateRandomEntity(bound: Int): Entity =
      Entity(name = s"Entity ${random.nextInt(bound)}",
        mass = random.nextInt(bound),
        volume = random.nextInt(bound),
        speed = random.nextInt(bound),
        acceleration = random.nextInt(bound),
        position = Pair(random.nextInt(bound), random.nextInt(bound)))

