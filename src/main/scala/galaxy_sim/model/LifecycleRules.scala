package galaxy_sim.model

import galaxy_sim.model.CelestialBodyOperations.*
import galaxy_sim.model.CelestialBodyType.*
import physics.dynamics.PhysicalEntity.changeMass

class LifecycleRules:

  trait EntityLifecycle[A]:
    def oneStep(entity: A): A

  given EntityLifecycle[CelestialBody] with
    override def oneStep(celestialBody: CelestialBody): CelestialBody = celestialBody.typeOf match
      case MassiveStar => celestialBody.updatePhysicalEntity(b => changeMass(b, celestialBody.mass + 1))
      case RedSuperGiant => ???
      case Supernova => ???
      case BlackHole => ???
      case Planet => ???
      case Asteroid => ???
      case InterstellarCloud => ???

  def entityOneStep[A](entity: A)(using entityLifeCycle: EntityLifecycle[A]): A = entityLifeCycle.oneStep(entity)
