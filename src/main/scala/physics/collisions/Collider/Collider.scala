package physics.collisions.Collider

import physics.collisions.Collider
import physics.collisions.Collision.Collision

case class Collider[A](a: A):
  def impactWith(other: A)(using col: Collision[A]): Collider[A] =
    Collider(col.impact(a, other))
  def flatMap[B](f: A => Collider[B]): Collider[B] =
    f(a)
  def map[B](f: A => B): Collider[B] =
    Collider(f(a))