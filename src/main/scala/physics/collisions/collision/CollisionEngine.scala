package physics.collisions.collision

import physics.collisions.impact.Impact
import physics.collisions.intersection.Intersection

/** Provides an access point for the Collision API, reuniting [[Intersection]] and [[Impact]] with a facade. */
object CollisionEngine:
  /** Checks whether the two elements are intersecting, given an appropriate [[Intersection]] */
  def collides[A](a1: A, a2: A)(using Intersection[A]): Boolean =
    summon[Intersection[A]].intersects(a1, a2)

  /**
   * Computes the impact between the two elements, provided they are intersecting.
   * @param a1 One of the elements, the "subject" element which will be the unit of the impact operation.
   * @param a2 The other element.
   * @param Intersection An intersection function between the two elements.
   * @param Impact An impact function between the two elements.
   * @tparam A The type of the elements.
   * @return The result of the impact between the two elements if they are intersecting, the unit of the operation if not.
   */
  def impact[A](a1: A, a2: A)(using Intersection[A])(using Impact[A]): A =
    if collides(a1, a2) then summon[Impact[A]].impact(a1, a2) else a1

  /** Computes the impact between an element of [[A]] and a sequence of other elements of [[A]] */
  def impactMany[A](a: A, others: Seq[A])(using Intersection[A])(using Impact[A]): A =
    others.foldLeft(a)((acc, a1) => impact(acc, a1))