package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyType.{BlackHole, InterstellarCloud, MassiveStar, Planet}
import physics.collisions.impact.Impact
import physics.collisions.instances.IntersectionInstances.given
import physics.collisions.intersection.Intersection
import galaxy_sim.utils.OperationsOnCelestialBody.{updateTemperature, updateMass}

object SimulationGivens:
  private def absorb(bigger: CelestialBody, smaller: CelestialBody): CelestialBody =
    bigger.updateMass(mass => mass + smaller.mass / 2).updateTemperature(temperature => temperature * 1.5)

  private def disintegrate(smaller: CelestialBody): CelestialBody =
    smaller.updateMass(mass => mass / 2).updateTemperature(temperature => temperature * 0.8)

  given CelestialBodyIntersection: Intersection[CelestialBody] =
    Intersection.from((a1, a2) => CircleToCircleCollision.intersects(a1.collisionBox, a2.collisionBox))

  given CelestialBodyImpact: Impact[CelestialBody] =
    Impact.from((a1, a2) => if a1.mass >= a2.mass then absorb(a1, a2) else disintegrate(a1))