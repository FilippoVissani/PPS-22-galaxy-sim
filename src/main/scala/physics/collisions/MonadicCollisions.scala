package physics.collisions

import physics.Pair
import physics.collisions.CollisionDetection.Colliders.CircleCollider

object MonadicCollisions extends App:
  // The monad Collision[A, B] encapsulates the possibility of a: A colliding with b: B
  sealed trait Collision[A, B]:
    def map[C](f: A => C): Collision[C, B]
    def flatMap[C](f: A => Collision[C, B]): Collision[C, B]
    def check(f: (A, B) => Boolean): Collision[A, B]

  case class SuccessfulCollision[A, B](a: A, b: B) extends Collision[A, B]:
    override def map[C](f: A => C): Collision[C, B] = SuccessfulCollision(f(a), b)
    override def flatMap[C](f: A => Collision[C, B]): Collision[C, B] = f(a)
    override def check(f: (A, B) => Boolean): Collision[A, B] =
      if f(a, b) then SuccessfulCollision(a, b) else FailedCollision()

  case class FailedCollision[A, B]() extends Collision[A, B]:
    override def map[C](f: A => C): Collision[C, B] = FailedCollision()
    override def flatMap[C](f: A => Collision[C, B]): Collision[C, B] = FailedCollision()
    override def check(f: (A, B) => Boolean): Collision[A, B] = FailedCollision()


  object Collision:
    def solve[A, B, C](c: Collision[A, B])(checker: (A, B) => Boolean)(mapper: A => C): Collision[C, B] =
      c.check(checker).map(mapper)
    def solveMany[A, B, C](l: List[Collision[A, B]])(checker: (A, B) => Boolean)(mapper: A => C): List[Collision[C, B]] =
      l.collect(c => solve(c)(checker)(mapper))

  @main def main(): Unit =
    val a = CircleCollider(Pair(0, 0), 2)
    val b = CircleCollider(Pair(1, 1), 0.5)
    val collider = SuccessfulCollision(a, b)
    val check = for
      x <- collider.check((_: CircleCollider, _: CircleCollider) => true)
    yield x
    println(check)