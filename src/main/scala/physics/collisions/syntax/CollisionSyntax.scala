package physics.collisions.syntax

import physics.collisions.Collision.Collision
import physics.collisions.Collision.Collision.{collides, impact}

object CollisionSyntax:
  import Collision.*

  extension [A](a: A)
    def collidesWith(b: A)(using col: Collision[A]): Boolean =
      collides(a, b)

    def impactWith(other: A)(using col: Collision[A]): A =
      impact(a, other)