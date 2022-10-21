package physics.collisions.syntax

import physics.collisions.Collision.CollisionEngine.*
import physics.collisions.Impact.Impact
import physics.collisions.Intersection.Intersection

import scala.annotation.targetName

/** Provides an extension empowering some type with access to the collision API through binary operators,
 * in a sort of DSL fashion. */
object CollisionSyntax:
  extension [A](a: A)
    @targetName("collidesWith")
    def |#|(other: A)(using Intersection[A]): Boolean =
      computeCollision(a, other)

    @targetName("impactWith")
    def |*|(other: A)(using Intersection[A])(using Impact[A]): A =
      computeImpact(a, other)