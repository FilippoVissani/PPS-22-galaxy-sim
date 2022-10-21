package physics.collisions.Collision

import physics.collisions.syntax.CollisionSyntax
import physics.collisions.Collision

import scala.annotation.targetName

trait Collision[A, B]:
  def collisionFun: (A, B) => Boolean
  def impactFun: (A, B) => A
  def impact(a: A, b: B): A  =
    if collisionFun(a, b) then impactFun(a, b) else a

object Collision:
  def from[A, B](f: (A, B) => Boolean)(g: (A, B) => A): Collision[A, B] = new Collision[A, B]:
    override def impactFun: (A, B) => A = g
    override def collisionFun: (A, B) => Boolean = f

  def collides[A, B](a: A, b: B)(using col: Collision[A, B]): Boolean =
    col.collisionFun(a, b)

  def impact[A, B](a: A, b: B)(using col: Collision[A, B]): A =
    col.impact(a, b)

  def impactMany[A, B](a: A, others: Seq[B])(using col: Collision[A, B]): A =
    others.foldLeft(a)((acc, b) => impact(acc, b))