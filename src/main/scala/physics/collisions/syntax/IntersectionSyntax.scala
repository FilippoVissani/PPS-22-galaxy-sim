package physics.collisions.syntax

import physics.collisions.intersection.Intersection

/** Provides an extension empowering some type with access to the [[Intersection]] API through unary operators,
 * in a sort of DSL fashion. */
object IntersectionSyntax:
  extension [A](a: A)
    def intersects(other: A)(using Intersection[A]) =
      Intersection.intersects(a, other)
