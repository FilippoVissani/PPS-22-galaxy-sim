package galaxy_sim.model

import galaxy_sim.model.ClockAliases.Time

object ClockAliases:
  type Time = Double

trait Clock:
  def virtualTime: Time
  def deltaTime: Time
  def incrementVirtualTime(): Clock

object Clock:
  def apply(virtualTime: Time = 0, deltaTime: Time = 0.1): Clock = ClockImpl(virtualTime, deltaTime)
  
  private case class ClockImpl(override val virtualTime: Time,
                               override val deltaTime: Time) extends Clock:
    override def incrementVirtualTime(): Clock = Clock(virtualTime + deltaTime, deltaTime)
