package galaxy_sim.utils

import galaxy_sim.model.CelestialBody
import galaxy_sim.model.CelestialBodyType.{BlackHole, InterstellarCloud, MassiveStar, Planet}
import physics.collisions.Collision.Collision
import physics.rigidbody.RigidBody.CircularEntity
import physics.collisions.instances.CollisionInstances.given

object SimulationGivens:
  private def absorb(bigger: CelestialBody, smaller: CelestialBody): CelestialBody =
    bigger.copy(mass = bigger.mass + smaller.mass / 2, temperature = bigger.temperature * 1.5)

  private def disintegrate(smaller: CelestialBody): CelestialBody =
    smaller.copy(mass = smaller.mass / 2, temperature = smaller.temperature * 0.8)

  given CelestialBodyCollision: Collision[CelestialBody, CelestialBody] =
    Collision.from[CelestialBody, CelestialBody](
      (a, b) => Collision.collides(a.collisionBox, b.collisionBox))(
      (a, b) => if a.mass >= b.mass then absorb(a, b) else disintegrate(a))