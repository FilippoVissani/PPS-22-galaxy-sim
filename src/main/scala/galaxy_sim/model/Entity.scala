package galaxy_sim.model

trait Entity:
  def name: String
  def mass: Float
  def volume: Float
  def speed: Float
  def acceleration: Float
  def position: Pair[Float, Float]
  def updateName(name: String): Entity
  def updateMass(delta: Float): Entity
  def updateVolume(delta: Float): Entity
  def updateSpeed(delta: Float): Entity
  def updateAcceleration(delta: Float): Entity
  def updatePosition(deltaX: Float)(deltaY: Float): Entity

object Entity:
  def apply(name: String,
            mass: Float,
            volume: Float,
            speed: Float,
            acceleration: Float,
            position: Pair[Float, Float]): Entity =
    EntityImpl(name, mass, volume, speed, acceleration, position)

  private case class EntityImpl(name: String,
                                mass: Float,
                                volume: Float,
                                speed: Float,
                                acceleration: Float,
                                position: Pair[Float, Float]) extends Entity:

    override def updatePosition(deltaX: Float)(deltaY: Float): Entity =
      Entity(name, mass, volume, speed, acceleration, position.map(_ + deltaX)(_ + deltaY))

    override def updateName(name: String): Entity =
      Entity(name, mass, volume, speed, acceleration, position)

    override def updateVolume(delta: Float): Entity =
      Entity(name, mass, volume + delta, speed, acceleration, position)

    override def updateSpeed(delta: Float): Entity =
      Entity(name, mass, volume, speed + delta, acceleration, position)

    override def updateAcceleration(delta: Float): Entity =
      Entity(name, mass, volume, speed, acceleration + delta, position)

    override def updateMass(delta: Float): Entity =
      Entity(name, mass + delta, volume, speed, acceleration, position)