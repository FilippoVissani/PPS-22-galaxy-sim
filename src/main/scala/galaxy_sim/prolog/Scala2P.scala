package galaxy_sim.prolog

import alice.tuprolog.*
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.CelestialBodyType.*

object Scala2P:

  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)
  given Conversion[Term, CelestialBodyType] = _.toString match
    case "massiveStar" => MassiveStar
    case "redSuperGiant" => RedSuperGiant
    case "supernova" => Supernova
    case "blackHole" => BlackHole
    case "planet" => Planet
    case "asteroid" => Asteroid
    case "interstellarCloud" => InterstellarCloud

  def extractTerm(solveInfo: SolveInfo, s: String): Term =
    solveInfo.getTerm(s)

  def mkPrologEngine(theory: Theory): Term => LazyList[SolveInfo] =
    val engine = Prolog()
    engine.setTheory(theory)

    goal => new Iterable[SolveInfo] {
        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo] {
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean = solution.isDefined &&
            (solution.get.isSuccess || solution.get.hasOpenAlternatives)

          override def next(): SolveInfo =
            try solution.get
            finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
        }
      }.to(LazyList)

  def solveOneAndGetTerm(engine: Term => LazyList[SolveInfo], goal: Term, term: String): Term =
    engine(goal).headOption.map(extractTerm(_, term)).get