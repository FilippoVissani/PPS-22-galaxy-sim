package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.EntityIdentifierProlog

object LifecycleRules:

  trait EntityLifecycle[A, B]:
    def oneStep(entity: A, bType: B): (A, B)

  given EntityLifecycle[CelestialBody, CelestialBodyType] with
    override def oneStep(celestialBody: CelestialBody, bType: CelestialBodyType): (CelestialBody, CelestialBodyType) =
      bType match
        case MassiveStar => (celestialBody.copy(), bType)
        case RedSuperGiant => (celestialBody.copy(), bType)
        case Supernova => (celestialBody.copy(), bType)
        case BlackHole => (celestialBody.copy(), bType)
        case Planet => (celestialBody.copy(), bType)
        case Asteroid => {
          val newCelestialBody = celestialBody.copy(mass = celestialBody.mass * 1.1)
          val newbType = EntityIdentifierProlog().checkEntityType(newCelestialBody.mass, newCelestialBody.temperature)
          (newCelestialBody, newbType)
        }
        case InterstellarCloud => (celestialBody.copy(), bType)

  def entityOneStep[A, B](entity: A, bType: B)(using entityLifeCycle: EntityLifecycle[A, B]): (A, B) =
    entityLifeCycle.oneStep(entity, bType)
