package physics.collisions.monads

import physics.collisions.impact.Impact
import physics.collisions.syntax.ImpactSyntax.*
import physics.collisions.syntax.IntersectionSyntax.*
import physics.collisions.intersection.Intersection

object Collision:
  opaque type Collision[A] = Option[(A, A)]

  def unit[A](a: (A, A)): Collision[A] = Option(a)

  def empty[A]: Collision[A] = None

  def collides[A](a1: A, a2: A)(using Intersection[A]): Collision[A] =
    if a1 intersects a2 then unit((a1, a2)) else empty

  def collisionResult[A](a1: A, a2: A)(using Intersection[A])(using Impact[A]): Collision[A] =
    for
      col <- collides(a1, a2)
      res <- if col equals empty then unit((a1 impact a2, a2 impact a1)) else empty
    yield res

  def collideMany[A](a: A, others: Seq[A])(using Intersection[A])(using Impact[A]): Seq[Collision[A]] =
    for
      b <- others
      r <- collisionResult(a, b)
    yield unit(r)