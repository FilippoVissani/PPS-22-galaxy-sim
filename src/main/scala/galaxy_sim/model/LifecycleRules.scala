package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*

object LifecycleRules:

  trait EntityLifecycle[A]:
    def oneStep(entity: A): A

  given EntityLifecycle[CelestialBody] with
    override def oneStep(celestialBody: CelestialBody): CelestialBody = celestialBody.typeOf match
      case MassiveStar => celestialBody.copy()
      case RedSuperGiant => celestialBody.copy()
      case Supernova => celestialBody.copy()
      case BlackHole => celestialBody.copy()
      case Planet => celestialBody.copy()
      case Asteroid => celestialBody.copy(mass = celestialBody.mass * 1.1)
      case InterstellarCloud => celestialBody.copy()

  /*
  given EntityLifecycle[MassiveStar] with
    override def oneStep(entity: MassiveStar): MassiveStar = ???

  given EntityLifecycle[RedSuperGiant] with
    override def oneStep(entity: RedSuperGiant): RedSuperGiant = ???

  given EntityLifecycle[Supernova] with
    override def oneStep(entity: Supernova): Supernova = ???

  given EntityLifecycle[BlackHole] with
    override def oneStep(entity: BlackHole): BlackHole = ???

  given EntityLifecycle[Planet] with
    override def oneStep(entity: Planet): Planet = ???

  given EntityLifecycle[Asteroid] with
    override def oneStep(entity: Asteroid): Asteroid = ???

  given EntityLifecycle[InterstellarCloud] with
    override def oneStep(entity: InterstellarCloud): InterstellarCloud = ???

  */

  def entityOneStep[A](entity: A)(using entityLifeCycle: EntityLifecycle[A]): A = entityLifeCycle.oneStep(entity)
