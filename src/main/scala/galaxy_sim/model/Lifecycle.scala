package galaxy_sim.model

import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.EntityIdentifierProlog
import physics.Mass
import physics.dynamics.GravitationLaws.solarMass
import galaxy_sim.model.CelestialBodyAliases.Temperature
import galaxy_sim.utils.OperationsOnCelestialBody.{updateMass, updateTemperature}

import scala.annotation.targetName
import scala.util.Random

object Lifecycle:

  private val entityIdentifierProlog = EntityIdentifierProlog()
  private val deltaMass = 7 * 10e-14 * solarMass

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
      case Planet => (celestialBody, bType)
      case Asteroid => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 0.99)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case InterstellarCloud => {
        val newCelestialBody = celestialBody.updateMass(mass => mass * 1.01).updateTemperature(temperature => temperature * 1.01)
        (newCelestialBody, bodyType(newCelestialBody))
      }
      case BlackHole => {
        val newCelestialBody = celestialBody.updateMass(mass => mass +- deltaMass)
        if newCelestialBody.mass > entityIdentifierProlog.minMassFor(BlackHole) then
          (newCelestialBody, BlackHole)
        else
          (newCelestialBody, bodyType(newCelestialBody))
      }
      case _ => {
        val newCelestialBody = celestialBody.updateMass(mass => mass +- deltaMass)
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

  def bodyType(celestialBody: CelestialBody): CelestialBodyType =
    entityIdentifierProlog.checkEntityType(celestialBody.mass, celestialBody.temperature)

  /**
   * Extension method for Mass
   */
  extension (n: Mass)
    @targetName("addOrSubtract")
    def +-(otherN: Mass): Mass =
      if Random.nextBoolean then n + otherN else n - otherN