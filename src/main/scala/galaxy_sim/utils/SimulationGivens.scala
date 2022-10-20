package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyType.{BlackHole, InterstellarCloud, MassiveStar, Planet}
import physics.collisions.CollisionDetection.CollisionChecker
import physics.collisions.CollisionDetection.CollisionCheckers.given
import physics.collisions.CollisionSolving.CollisionSolver
import physics.rigidbody.RigidBody.CircularEntity

object SimulationGivens:

  /** Collision Checking */
  given CircularEntityChecker : CollisionChecker[CelestialBody, CelestialBody] with
    override def check(a: CelestialBody, b: CelestialBody): Boolean =
      CircleToCircleChecker.check(a.collisionBox, b.collisionBox)

  /** Collision Solving */
  private def absorb(bigger: CelestialBody, smaller: CelestialBody): CelestialBody =
    bigger.copy(mass = bigger.mass + smaller.mass / 2, temperature = bigger.temperature * 1.5)

  private def disintegrate(smaller: CelestialBody): CelestialBody =
    smaller.copy(mass = smaller.mass / 2, temperature = smaller.temperature * 0.8)

  given CelestialBodySolver: CollisionSolver[CelestialBody, CelestialBody, CelestialBody] with
    override def solve(a: CelestialBody, b: CelestialBody): CelestialBody =
      if a.mass >= b.mass then absorb(a, b) else disintegrate(a)