package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.Mass
import galaxy_sim.model.CelestialBodyAliases.Temperature
import operationsOnCelestialBody.{updateMass, updateTemperature}

object LifecycleRules:

  private val entityIdentifierProlog = EntityIdentifierProlog()

  trait EntityLifecycle[A]:
    /**
     * This method modify the properties of the entity. The modifies are based on the type.
     * @param entity the celestial body
     * @param bodyType the type of celestial body
     * @return a tuple (celestialBody, bodyType) with the modified entity and the type. The type is calculated based on the new entity properties.
     */
    def oneStep(entity: CelestialBody, bodyType: A): (CelestialBody, A)

  /**
   * This given is used to modify the properties of the entity, based on the type.
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

  def entityOneStep[A](entity: CelestialBody, bodyType: A)(using entityLifeCycle: EntityLifecycle[A]): (CelestialBody, A) =
    entityLifeCycle.oneStep(entity, bodyType)

object operationsOnCelestialBody:
  extension (celestialBody: CelestialBody)
    def updateMass(f: Mass => Mass): CelestialBody = celestialBody.copy(mass = f(celestialBody.mass))
    def updateTemperature(f: Temperature => Temperature): CelestialBody = celestialBody.copy(temperature = f(celestialBody.temperature))
