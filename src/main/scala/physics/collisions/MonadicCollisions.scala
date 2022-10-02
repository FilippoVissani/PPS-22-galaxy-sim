package physics.collisions

import physics.Pair
import physics.collisions.CollisionDetection.Colliders.CircleCollider

object MonadicCollisions extends App:
  // The monad Collision[A, B] encapsulates the possibility of a: A colliding with b: B
  enum Collision[A, B]:
    case Cons(a: A, b: B)
    case None()

  export Collision.*
  extension[A, B](c: Collision[A, B])
    def map[C](f: A => C): Collision[C, B] = c match
      case Cons(a, b) => Cons(f(a), b)
      case None() => None()
    def flatMap[C](f: A => Collision[C, B]): Collision[C, B] = c match
      case Cons(a, _) => f(a)
      case None() => None()
  
  object Collision:
    def apply[A, B](a: A, b: B): Collision[A, B] = Collision.Cons(a, b)
    def check[A, B](c: Collision[A, B], f: (A, B) => Boolean): Collision[A, B] = c match
      case Cons(a, b) => if f(a, b) then Collision(a, b) else None()
      case None() => None()
    def solve[A, B, C](c: Collision[A, B])(checker: (A, B) => Boolean)(mapper: A => C): Collision[C, B] =
      check(c, checker).map(mapper)
    def solveMany[A, B, C](l: List[Collision[A, B]])(checker: (A, B) => Boolean)(mapper: A => C): List[Collision[C, B]] =
      l.collect(c => solve(c)(checker)(mapper))

  @main def main(): Unit =
    val a = CircleCollider(Pair(0, 0), 2)
    val b = CircleCollider(Pair(1, 1), 0.5)
    val c = Collision(a, b)
    val g = for
      x <- check(c, (_: CircleCollider, _: CircleCollider) => true)
    yield x
    println(g)