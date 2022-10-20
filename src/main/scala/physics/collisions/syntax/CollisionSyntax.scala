package physics.collisions.syntax

import physics.collisions.Collision.Collision
import physics.collisions.Collision.Collision.{collides, impact}

object CollisionSyntax:
  import Collision.*

  extension [A](a: A)
    def collidesWith[B](b: B)(using col: Collision[A, B]): Boolean =
      collides(a, b)

    def impactWith[B](other: B)(using col: Collision[A, B]): A =
      impact(a, other)