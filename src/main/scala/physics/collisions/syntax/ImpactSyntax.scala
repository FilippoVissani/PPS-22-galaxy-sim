package physics.collisions.syntax

import physics.collisions.impact.Impact

/** Provides an extension empowering some type with access to the [[Impact]] API through unary operators,
 * in a sort of DSL fashion. */
object ImpactSyntax:
  extension [A](a: A)
    def impact(other: A)(using Impact[A]) =
      Impact.impact(a, other)
