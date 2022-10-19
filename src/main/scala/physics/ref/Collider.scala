package physics.ref

case class Collider[A](a: A):
  def impactWith[B](other: B)(using col: Collision[A, B]): Collider[A] =
    Collider(col.impact(a, other))
  def flatMap[B](f: A => Collider[B]): Collider[B] =
    f(a)
  def map[B](f: A => B): Collider[B] =
    Collider(f(a))


object TryCollider extends App:
  import Collisions.given

  val a = 1
  val b = 1
  val col = Collider(a)
  val res = for
    y <- col.impactWith(b)
  yield y
  println(res)