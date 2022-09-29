package physics
import physics.Pair

type Position = Pair[Double, Double]
type Speed = Double
type SpeedVector = Pair[Speed, Speed]
type GravityForceVector = Pair[GForce, GForce]
type Mass = Double
type GForce = Double

trait Pair[A, B]:
  def x: A
  def y: B

object Pair:
  def apply[A, B](x: A, y: B): Pair[A, B] = PairImpl[A, B](x, y)
  def map[A, B, C, D](p: Pair[A, B])(f: A => C)(g: B => D): Pair[C, D] = Pair(f(p.x), g(p.y))
  extension[A,B] (p: Pair[A,B])
    def +(other: Pair[A, B])(using op: PairOperations[A,B]) = op.sum(p, other)

  private case class PairImpl[A, B](x: A, y: B) extends Pair[A, B]

  sealed trait PairOperations[A, B]:
    def sum(p1: Pair[A, B], p2: Pair[A, B]): Pair[A, B]

  object PairOperations:
    given SumPairOfDouble: PairOperations[Double, Double] with
      override def sum(p1: Pair[Double, Double], p2: Pair[Double, Double]): Pair[Double, Double] = Pair(p1.x + p2.x, p1.y + p2.y)