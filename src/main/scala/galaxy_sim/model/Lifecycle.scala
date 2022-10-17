package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.Mass
import galaxy_sim.model.CelestialBodyAliases.Temperature
import operationsOnCelestialBody.{updateMass, updateTemperature}

object Lifecycle:

  private val entityIdentifierProlog = EntityIdentifierProlog()

  trait LifecycleRules[A]:
    /**
     * This function modify the properties of the entity. The modifies are based on the type.
     * @param entity the celestial body
     * @param bodyType the type of celestial body
     * @return a tuple (celestialBody, bodyType) with the modified entity and the type. The type is calculated based on the new entity properties.
     */
    def oneStep(entity: CelestialBody, bodyType: A): (CelestialBody, A)

  /**
   * This given is used to modify the properties of the entity, based on the type.
   */
  given LifecycleRules[CelestialBodyType] with
    override def oneStep(celestialBody: CelestialBody, bType: CelestialBodyType): (CelestialBody, CelestialBodyType) = bType match
      case MassiveStar => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.12).updateTemperature(temperature => temperature * 1.06)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case RedSuperGiant => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.05).updateTemperature(temperature => temperature * 1.07)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case Supernova => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.2).updateTemperature(temperature => temperature * 1.05)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case BlackHole => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.15).updateTemperature(temperature => temperature * 1.02)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case Planet => (celestialBody, bType)
      case Asteroid => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 0.98)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case InterstellarCloud => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.1).updateTemperature(temperature => temperature * 1.1)
        (newCelestialBody, bodyType(newCelestialBody))
      }

  /**
   * function to modify properties of the entity, using specified LifecycleRules
   * @param entity the celestial body
   * @param bodyType the type of celestial body
   * @param lifecycleRules define the rules to use
   * @tparam A the generic bodyType
   * @return a tuple (celestialBody, bodyType) with the modified entity and the type. The type is calculated based on the new entity properties.
   */
  def entityOneStep[A](entity: CelestialBody, bodyType: A)(using lifecycleRules: LifecycleRules[A]): (CelestialBody, A) =
    lifecycleRules.oneStep(entity, bodyType)

  private def bodyType(celestialBody: CelestialBody): CelestialBodyType =
    entityIdentifierProlog.checkEntityType(celestialBody.mass, celestialBody.temperature)

object operationsOnCelestialBody:
  extension (celestialBody: CelestialBody)
    /**
     * Update the mass applying the given function
     */
    def updateMass(f: Mass => Mass): CelestialBody = celestialBody.copy(mass = f(celestialBody.mass))

    /**
     * Update the temperature applying the given function
     */
    def updateTemperature(f: Temperature => Temperature): CelestialBody = celestialBody.copy(temperature = f(celestialBody.temperature))
