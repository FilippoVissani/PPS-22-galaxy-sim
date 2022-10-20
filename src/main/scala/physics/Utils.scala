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
  def map[C, D](f: ((A, B)) => (C, D)): Pair[C, D]

object Pair:
  def apply[A, B](x: A, y: B): Pair[A, B] = PairImpl[A, B](x, y)
  extension[A, B] (p: Pair[A,B])
    def +(other: Pair[A, B])(using op: PairOperations[A,B]) = op.sum(p, other)
    def <->(other: Pair[A, B])(using op: PairOperations[A,B]) = op.distance(p, other)
    def ||(using op: PairOperations[A,B]) = op.module(p)

  private case class PairImpl[A, B](x: A, y: B) extends Pair[A, B]:
    override def map[C, D](f: ((A, B)) => (C, D)): Pair[C, D] = 
      val result = f(x, y)
      Pair(result._1, result._2)

  sealed trait PairOperations[A, B]:
    def sum(p1: Pair[A, B], p2: Pair[A, B]): Pair[A, B]
    def distance(p1: Pair[A, B], p2: Pair[A, B]): Double
    def module(p: Pair[A, B]): Double

  object PairOperations:
    given SumPairOfDouble: PairOperations[Double, Double] with
      override def sum(p1: Pair[Double, Double], p2: Pair[Double, Double]): Pair[Double, Double] =
        Pair(p1.x + p2.x, p1.y + p2.y)

      override def distance(p1: Pair[Double, Double], p2: Pair[Double, Double]): Double =
        Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2))

      override def module(p: Pair[Double, Double]): Double = 
        Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2))