package galaxy_sim.prolog

import alice.tuprolog.Theory
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.Scala2P.{*, given}
import physics.Mass
import galaxy_sim.model.CelestialBodyAliases.Temperature

trait EntityIdentifierProlog:
  /**
   * Function to check the entity type.
   * @param mass the mass of the entity
   * @param temperature the temperature of the entity
   * @return the entity type as EntityType
   */
  def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType

  def minMassFor(celestialBodyType: CelestialBodyType): Mass

object EntityIdentifierProlog:
  def apply(): EntityIdentifierProlog = EntityIdentifierPrologImpl()

  private case class EntityIdentifierPrologImpl() extends EntityIdentifierProlog:
    private val engine = mkPrologEngine(Theory.parseLazilyWithStandardOperators(getClass.getResourceAsStream("/prolog/EntityIdentifier.pl")))
    
    override def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType =
      val goal = s"typeOfEntity($temperature, $mass, E)"
      solveOneAndGetTerm(engine, goal, "E")

    override def minMassFor(celestialBodyType: CelestialBodyType): Mass =
      val goal = s"minMass(E, $celestialBodyType)"
      val term = solveOneAndGetTerm(engine, goal, "E")
      val numbers = ("""\d+""".r findAllIn term.toString).toList
      if numbers.size == 1 then
        numbers.head.toDouble
      else
        scala.math.pow(numbers.head.toDouble, numbers.last.toDouble)
