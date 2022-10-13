package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyType.{BlackHole, InterstellarCloud, MassiveStar, Planet}
import physics.collisions.CollisionDetection.CollisionChecker
import physics.collisions.CollisionDetection.CollisionCheckers.given
import physics.collisions.CollisionSolving.CollisionSolver
import physics.collisions.rigidbody.RigidBody.CircularEntity

object SimulationGivens:

  given CircularEntityChecker : CollisionChecker[CircularEntity, CircularEntity] with
    override def check(a: CircularEntity, b: CircularEntity): Boolean =
      CircleToCircleChecker.check(a.collisionBox, b.collisionBox)

  given IncreaseMassSolver : CollisionSolver[CelestialBody, CelestialBody, CelestialBody] with
    override def solve(a: CelestialBody, b: CelestialBody): CelestialBody =
      val (bigger, smaller) = if a.mass > b.mass then (a, b) else (b, a)
      bigger.copy(mass = bigger.mass + smaller.mass)

  given ByTypeSolver : CollisionSolver[CelestialBody, CelestialBody, CelestialBody] with
    override def solve(a: CelestialBody, b: CelestialBody): CelestialBody = (a.typeOf, b.typeOf) match
      case (MassiveStar, InterstellarCloud) => IncreaseMassSolver.solve(a, b)
      case (MassiveStar, Planet) => IncreaseMassSolver.solve(a, b)
      case (BlackHole, MassiveStar) => IncreaseMassSolver.solve(a, b)
      case (BlackHole, Planet) => IncreaseMassSolver.solve(a, b)
      case (_, Planet) => b.copy(mass = b.mass / 2) 