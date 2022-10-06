package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*

class LifecycleRules:

  trait EntityLifecycle[A]:
    def oneStep(entity: A): A

  given EntityLifecycle[CelestialBody] with
    override def oneStep(celestialBody: CelestialBody): CelestialBody = celestialBody.typeOf match
      case MassiveStar => celestialBody.copy()
      case RedSuperGiant => celestialBody.copy()
      case Supernova => celestialBody.copy()
      case BlackHole => celestialBody.copy()
      case Planet => celestialBody.copy()
      case Asteroid => celestialBody.copy()
      case InterstellarCloud => celestialBody.copy()

  def entityOneStep[A](entity: A)(using entityLifeCycle: EntityLifecycle[A]): A = entityLifeCycle.oneStep(entity)
