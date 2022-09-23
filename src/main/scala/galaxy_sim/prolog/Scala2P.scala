package galaxy_sim.prolog

import alice.tuprolog.*
import galaxy_sim.model.EntityType

object Scala2P:

  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)
  given Conversion[Term, EntityType] = _.toString match
    case "MassiveStar" => EntityType.MassiveStar
    case "redSuperGiant" => EntityType.RedSuperGiant
    case "supernova" => EntityType.Supernova
    case "blackHole" => EntityType.BlackHole
    case "lowMassStar" => EntityType.LowMassStar
    case "redGiant" => EntityType.RedGiant
    case "planetaryNebula" => EntityType.PlanetaryNebula
    case "whiteDwarf" => EntityType.WhiteDwarf
    case "blackDwarf" => EntityType.BlackDwarf
    case "planet" => EntityType.Planet
    case "asteroid" => EntityType.Asteroid
    case "interstellarCloud" => EntityType.InterstellarCloud

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