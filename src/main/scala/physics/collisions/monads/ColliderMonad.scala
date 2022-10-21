package physics.collisions.monads

import physics.collisions.Impact.Impact
import physics.collisions.Intersection.Intersection
import physics.collisions.syntax.CollisionSyntax.*

object ColliderMonad:
  trait Collider[A]:
    def flatMap[B](f: A => Collider[B]): Collider[B]
    def map[B](f: A => B): Collider[B] = flatMap(a => unit(f(a)))

  def unit[A](a: A): Collider[A] = new Collider[A]:
    override def flatMap[B](f: A => Collider[B]): Collider[B] = f(a)

  def checkCollision[A](a1: A, a2: A)(using Intersection[A]): Collider[Boolean] = new Collider[Boolean]:
    override def flatMap[B](f: Boolean => Collider[B]): Collider[B] = { val b = a1 |#| a2 ; f(b) }
  
  def collide[A](a1: A, a2: A)(using Intersection[A])(using Impact[A]): Collider[A] = new Collider[A]:
    override def flatMap[B](f: A => Collider[B]): Collider[B] = { val a3 = a1 |*| a2 ; f(a3)}