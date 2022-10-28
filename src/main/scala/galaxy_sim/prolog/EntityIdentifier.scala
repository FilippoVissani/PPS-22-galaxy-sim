package galaxy_sim.prolog

import alice.tuprolog.Theory
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.prolog.Scala2P.{*, given}
import physics.Mass
import galaxy_sim.model.CelestialBodyAliases.Temperature

object EntityIdentifier:
  
  private val engine = mkPrologEngine(Theory.parseLazilyWithStandardOperators(getClass.getResourceAsStream("/prolog/EntityIdentifier.pl")))

  /**
   * Function to check the entity type.
   *
   * @param mass        the mass of the entity
   * @param temperature the temperature of the entity
   * @return the entity type as CelestialBodyType
   */ 
  def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType =
    val goal = s"typeOfEntity($temperature, $mass, E)"
    solveOneAndGetTerm(engine, goal, "E")