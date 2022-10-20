package physics.collisions
import physics.collisions.CollisionDetection.CollisionChecker
import physics.collisions.CollisionSolving.CollisionSolver

import scala.annotation.targetName

object Collider extends App:
  enum Collider[+A]:
    case Some(a: A)
    case None()

  export Collider.*

  extension [A](c: Collider[A])
    def flatMap[B](f: A => Collider[B]): Collider[B] = c match
      case Some(a) => f(a)
      case None() => None()
    def map[B](f: A => B): Collider[B] =
      c.flatMap(a => Collider(f(a)))
    def filter(f: A => Boolean): Collider[A] =
      c.flatMap(a => if f(a) then Collider(a) else None())
    def foreach[U](f: A => U): Unit =
      c.map(a => { f(a) ; a } )

  object Collider:
    def apply[A](a: A): Collider[A] = Collider.Some(a)
    def apply[A](): Collider[A] = None()
    def check[A, B](c: Collider[A], other: B)(f: (A, B) => Boolean): Collider[A] =
      c.filter(a => f(a, other))
    def solve[A, B, C](c: Collider[A], other: B)(f: (A, B) => Boolean)(g: (A, B) => C): Collider[C] =
      check(c, other)(f).map(a => g(a, other))

    /** Checks the collision against every element */
    def checkMany[A, B](c: Collider[A], others: B*)(using f: CollisionChecker[A, B]): Collider[A] =
      others.foldLeft(c)((acc, b) => check(acc, b)(f.check))

    //creare una funzione che applica in cascata n collisioni: risultato o collider nuovo o se stesso

    def isEmpty[A](c: Collider[A]): Boolean = c equals None()

    def get[A](c: Collider[A]): A = c match
      case Some(a) => a
      case None() => throw Exception()

    extension [A](c: Collider[A])
      @targetName("check")
      def %[B](other: B)(using col: CollisionChecker[A, B]): Collider[A] =
        check(c, other)(col.check)
      @targetName("solve")
      def ><[B, C](other: B)(using col: CollisionChecker[A, B])(using sol: CollisionSolver[A, B, C]): Collider[C] =
        solve(c, other)(col.check)(sol.solve)
      def subject: A = get(c)
