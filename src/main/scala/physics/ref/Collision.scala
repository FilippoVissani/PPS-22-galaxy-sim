package physics.ref

import scala.annotation.targetName

trait Collision[A, B]:
  def collides(a: A, b: B): Boolean
  def impactFun: (A, B) => A

  def impact(a: A, b: B): A =
    if collides(a, b) then impactFun(a, b) else a

object Collision:
  def collides[A, B](a: A, b: B)(using col: Collision[A, B]): Boolean =
    col.collides(a, b)

  def impact[A, B](a: A, b: B)(using col: Collision[A, B]): A =
    col.impact(a, b)

  def impactMany[A, B](a: A, others: B*)(using col: Collision[A, B]): A =
    others.foldLeft(a)((acc, b) => impact(acc, b))

object CollisionSyntax:
  import Collision.*

  extension [A](a: A)
    def collidesWith[B](b: B)(using col: Collision[A, B]): Boolean =
      collides(a, b)

    def impactWith[B](other: B)(using col: Collision[A, B]): A =
      impact(a, other)

object Collisions:
  given IntToIntCollision : Collision[Int, Int] with
    override def collides(a: Int, b: Int): Boolean =
      a == b

    override def impactFun: (Int, Int) => Int =
      _ + _

object TryCollisions extends App:
  import Collision.*
  import CollisionSyntax.*
  import Collisions.given

  println(1 collidesWith 2) // false
  println(1 impactWith 1 impactWith 3) // 2
  println(impactMany(1, 1, 2, 4, 5)) // 8

  val list = List(1, 2, 4, 5)
  val start = Option(1)
  val res1 = for
    x <- list
    y <- start
  yield x impactWith y
  println(res1)