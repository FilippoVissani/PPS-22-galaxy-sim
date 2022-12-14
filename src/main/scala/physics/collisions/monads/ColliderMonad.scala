package physics.collisions.monads

import physics.collisions.collision.CollisionEngine
import physics.collisions.impact.Impact
import physics.collisions.intersection.Intersection
import physics.collisions.syntax.CollisionSyntax.*

object ColliderMonad:
  /** A Monad that can be used to get access to collision api inside for-comprehensions */
  trait Collider[A]:
    def flatMap[B](f: A => Collider[B]): Collider[B]
    def map[B](f: A => B): Collider[B] = flatMap(a => unit(f(a)))

  def unit[A](a: A): Collider[A] = new Collider[A]:
    override def flatMap[B](f: A => Collider[B]): Collider[B] = f(a)

  /**
   * Checks whether an intersection between the elements is in place or not.
   * @param a1 One of the elements.
   * @param a2 The other element.
   * @param Intersection An [[Intersection]] function between the two elements.
   * @tparam A The type of the elements.
   * @return A [[Collider]] wrapping the check result.
   */
  def collides[A](a1: A, a2: A)(using Intersection[A]): Collider[Boolean] =
    unit(a1 |#| a2)

  /**
   * Computes the impact between the two elements, provided they are intersecting.
   * @param a1 One of the elements, the "subject" element which will be the unit of the impact operation.
   * @param a2 The other element.
   * @param Intersection An intersection function between the two elements.
   * @param Impact An impact function between the two elements.
   * @tparam A The type of the elements.
   * @return A [[Collider]] wrapping the result of the impact if the elements are intersecting, the unit of the operation if not.
   */
  def impact[A](a1: A, a2: A)(using Intersection[A])(using Impact[A]): Collider[A] =
    unit(a1 |*| a2)

  /** Computes the impact between an element of [[A]] and a sequence of other elements of [[A]] */
  def impactMany[A](a1: A, others: Seq[A])(using Intersection[A])(using Impact[A]): Collider[A] =
    unit(CollisionEngine.impactMany(a1, others))