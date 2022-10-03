package galaxy_sim.prolog

import alice.tuprolog.Theory
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.Scala2P.{*, given}

type Mass = Double
type Temperature = Double

trait EntityIdentifierProlog:
  /**
   * 
   * @param mass the mass of the entity
   * @param temperature the temperature of the entity
   * @return the entity type as EntityType
   */
  def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType

object EntityIdentifierProlog:
  def apply(): EntityIdentifierProlog = EntityIdentifierPrologImpl()

  private case class EntityIdentifierPrologImpl() extends EntityIdentifierProlog:
    private val engine = mkPrologEngine(Theory.parseLazilyWithStandardOperators(getClass.getResourceAsStream("/prolog/EntityIdentifier.pl")))
    
    override def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType =
      val goal = s"typeOfEntity($temperature, $mass, E)"
      solveOneAndGetTerm(engine, goal, "E")