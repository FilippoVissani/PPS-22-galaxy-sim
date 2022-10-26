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
   * @param mass the mass of the entity
   * @param temperature the temperature of the entity
   * @return the entity type as CelestialBodyType
   */ 
  def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType =
    val goal = s"typeOfEntity($temperature, $mass, E)"
    solveOneAndGetTerm(engine, goal, "E")

  /**
   * Get the minimum mass value for a given celestial body type
   *
   * @param celestialBodyType the celestial body type
   * @return the minimum mass
   */
  def minMassFor(celestialBodyType: CelestialBodyType): Mass =
    val prologCelestialBodyType = celestialBodyType.toString.substring(0, 1).toLowerCase() + celestialBodyType.toString.substring(1)
    val goal = s"minMass(E, $prologCelestialBodyType)"
    val term = solveOneAndGetTerm(engine, goal, "E")
    val numbers = ("""\d+""".r findAllIn term.toString).toList
    if numbers.size == 1 then
      numbers.head.toDouble
    else
      scala.math.pow(numbers.head.toDouble, numbers.last.toDouble)
