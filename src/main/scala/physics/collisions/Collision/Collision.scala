package physics.collisions.Collision

import physics.collisions.syntax.CollisionSyntax
import physics.collisions.Collision

import scala.annotation.targetName

trait Collision[A]:
  def collisionFun: (A, A) => Boolean
  def impactFun: (A, A) => A
  def impact(a1: A, a2: A): A  =
    if collisionFun(a1, a2) then impactFun(a1, a2) else a1

object Collision:
  def from[A](f: (A, A) => Boolean)(g: (A, A) => A): Collision[A] = new Collision[A]:
    override def impactFun: (A, A) => A = g
    override def collisionFun: (A, A) => Boolean = f

  def collides[A](a1: A, a2: A)(using col: Collision[A]): Boolean =
    col.collisionFun(a1, a2)

  def impact[A](a1: A, a2: A)(using col: Collision[A]): A =
    col.impact(a1, a2)

  def impactMany[A](a: A, others: Seq[A])(using col: Collision[A]): A =
    others.foldLeft(a)((acc, a1) => impact(acc, a1))