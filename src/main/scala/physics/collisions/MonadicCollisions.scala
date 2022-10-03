package physics.collisions

import physics.Pair
import physics.collisions.CollisionDetection.Colliders.CircleCollider

object MonadicCollisions extends App:
  // The monad Collision[A, B] encapsulates the possibility of a: A colliding with b: B
  enum Collision[A, B]:
    case Cons(c: (A, B))
    case None()

  export Collision.*
  extension[A, B](c: Collision[A, B])
    def map[C](f: A => C): Collision[C, B] = c match
      case Cons(a, b) => Cons(f(a), b)
      case None() => None()
    def flatMap[C](f: A => Collision[C, B]): Collision[C, B] = c match
      case Cons(a, _) => f(a)
      case None() => None()
    def filter(p: (A, B) => Boolean): Collision[A, B] = c match
      case Cons(a, b) => if p(a, b) then Cons(a, b) else None()
      case None() => None()

  object Collision:
    def apply[A, B](a: A, b: B): Collision[A, B] = Collision.Cons(a, b)
    def check[A, B](c: Collision[A, B])(using f: CollisionChecker[A, B]): Collision[A, B] =
      c.filter(f.check)
    def solve[A, B, C](c: Collision[A, B])(using checker: CollisionChecker[A, B])(using solver: CollisionSolver[A, B]): Collision[C, B] =
      check(c).flatMap(_ => solver.solve(c))
    def solveMany[A, B, C](l: List[Collision[A, B]])(using checker: CollisionChecker[A, B])(using solver: CollisionSolver[A, B]): List[Collision[C, B]] =
      l.collect(c => solve(c))

  trait CollisionChecker[A, B]:
    def check(a: A, b: B): Boolean

  trait CollisionSolver[A, B]:
    def solve[C](c: Collision[A, B]): Collision[C, B]

  object CollisionCheckers:
    given CircleToCircleChecker: CollisionChecker[CircleCollider, CircleCollider] with
      override def check(a: CircleCollider, b: CircleCollider): Boolean =
        val dx = a.origin.x - b.origin.x
        val dy = a.origin.y - b.origin.y
        val dist = Math.sqrt(dx * dx + dy * dy)
        dist < a.radius + b.radius

  @main def main(): Unit =
    import CollisionCheckers.given

    val a = CircleCollider(Pair(0, 0), 2)
    val b = CircleCollider(Pair(1, 1), 0.5)
    val c = Collision(a, b)
    val g = for
      x <- check(c)
    yield x
    println(g)