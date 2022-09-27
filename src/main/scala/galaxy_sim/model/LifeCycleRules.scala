package galaxy_sim.model

import galaxy_sim.model.CelestialBodyOperations.*
import galaxy_sim.model.CelestialBodyType.*
import physics.dynamics.PhysicalEntity.changeMass

class LifeCycleRules:

  trait EntityLifeCycle[A]:
    def oneStep(entity: A): A

  given EntityLifeCycle[CelestialBody] with
    override def oneStep(celestialBody: CelestialBody): CelestialBody = celestialBody.typeOf match
      case MassiveStar => celestialBody.updatePhysicalEntity(b => changeMass(b, celestialBody.mass + 1))
      case RedSuperGiant => ???
      case Supernova => ???
      case BlackHole => ???
      case Planet => ???
      case Asteroid => ???
      case InterstellarCloud => ???

  def entityOneStep[A](entity: A)(using entityLifeCycle: EntityLifeCycle[A]): A = entityLifeCycle.oneStep(entity)
