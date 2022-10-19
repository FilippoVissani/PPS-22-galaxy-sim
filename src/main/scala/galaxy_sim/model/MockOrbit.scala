package galaxy_sim.model

import physics.Pair

import scala.math.BigDecimal.double2bigDecimal

object MockOrbit:

  def computeEntityReference(celestialBody: CelestialBody, otherBodies: Set[CelestialBody]): Option[CelestialBody] =
    val biggerBodies = otherBodies.filter(x => x.mass > celestialBody.mass)
    if !biggerBodies.isEmpty then
      Option(biggerBodies.toList.sortBy(x => x.position <-> celestialBody.position).head)
    else
      Option.empty

  def computeNextPosition(celestialBody: CelestialBody, reference: CelestialBody, deltaTime: Double): CelestialBody =
    val direction = if celestialBody.speedVector.x > 0 then 1 else -1
    val radius = celestialBody.position <-> reference.position
    val newAngle = Math.atan2(celestialBody.position.y, celestialBody.position.x) + celestialBody.speedVector.|| * deltaTime * direction
    val newPosition = Pair(Math.cos(newAngle) * radius, Math.sin(newAngle) * radius)
    celestialBody.copy(position = newPosition)
