package physics.collisions.syntax

import physics.collisions.collision.CollisionEngine.*
import physics.collisions.impact.Impact
import physics.collisions.intersection.Intersection

import scala.annotation.targetName

/** Provides an extension empowering some type with access to the collision API through unary operators,
 * in a sort of DSL fashion. */
object CollisionSyntax:
  extension [A](a: A)
    @targetName("collidesWith")
    def |#|(other: A)(using Intersection[A]): Boolean =
      collides(a, other)

    @targetName("impactWith")
    def |*|(other: A)(using Intersection[A])(using Impact[A]): A =
      impact(a, other)