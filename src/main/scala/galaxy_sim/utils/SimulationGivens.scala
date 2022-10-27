package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyType.{BlackHole, InterstellarCloud, MassiveStar, Planet}
import physics.collisions.impact.Impact
import physics.collisions.instances.IntersectionInstances.given
import physics.collisions.intersection.Intersection
import physics.dynamics.PhysicalEntity

object SimulationGivens:
  private def absorb(bigger: CelestialBody, smaller: CelestialBody): CelestialBody =
    bigger.copy(mass = bigger.mass + smaller.mass * 0.8, temperature = bigger.temperature * 1.5)

  private def disintegrate(smaller: CelestialBody): CelestialBody =
    smaller.copy(mass = smaller.mass * 0.2, temperature = smaller.temperature * 0.8)

  given CelestialBodyIntersection: Intersection[CelestialBody] =
    Intersection.from((a1, a2) => CircleToCircleCollision.intersects(a1.collisionBox, a2.collisionBox))

  given CelestialBodyImpact: Impact[CelestialBody] =
    Impact.from((a1, a2) => if a1.mass > a2.mass then absorb(a1, a2) else disintegrate(a1))