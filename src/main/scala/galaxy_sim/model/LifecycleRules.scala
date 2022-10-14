package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.Mass
import galaxy_sim.model.CelestialBodyAliases.Temperature
import operationsOnCelestialBody.{updateMass, updateTemperature}

object LifecycleRules:

  private val entityIdentifierProlog = EntityIdentifierProlog()

  /*
  trait EntityLifecycle[A, B]:
    def oneStep(entity: A, bType: B): (A, B)
*/
  trait EntityLifecycle[A]:
    def oneStep(entity: CelestialBody, bodyType: A): (CelestialBody, A)

  /*
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
          val newbType = entityIdentifierProlog.checkEntityType(newCelestialBody.mass, newCelestialBody.temperature)
          (newCelestialBody, newbType)
        }
        case InterstellarCloud => (celestialBody.copy(), bType)
*/

  given EntityLifecycle[CelestialBodyType] with
    override def oneStep(celestialBody: CelestialBody, bodyType: CelestialBodyType): (CelestialBody, CelestialBodyType) = bodyType match
      case MassiveStar => (celestialBody.copy(), bodyType)
      case RedSuperGiant => (celestialBody.copy(), bodyType)
      case Supernova => (celestialBody.copy(), bodyType)
      case BlackHole => (celestialBody.copy(), bodyType)
      case Planet => (celestialBody.copy(), bodyType)
      case Asteroid => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.1).updateTemperature(temperature => temperature * 1.1)
        val newBodyType = entityIdentifierProlog.checkEntityType(newCelestialBody.mass, newCelestialBody.temperature)
        (newCelestialBody, newBodyType)
      }
      case InterstellarCloud => (celestialBody.copy(), bodyType)

  /*def entityOneStep[A, B](entity: A, bType: B)(using entityLifeCycle: EntityLifecycle[A, B]): (A, B) =
    entityLifeCycle.oneStep(entity, bType)
    */
  def entityOneStep[A](entity: CelestialBody, bodyType: A)(using entityLifeCycle: EntityLifecycle[A]): (CelestialBody, A) =
    entityLifeCycle.oneStep(entity, bodyType)

object operationsOnCelestialBody:
  extension (celestialBody: CelestialBody)
    def updateMass(f: Mass => Mass): CelestialBody = celestialBody.copy(mass = f(celestialBody.mass))
    def updateTemperature(f: Temperature => Temperature): CelestialBody = celestialBody.copy(temperature = f(celestialBody.temperature))
