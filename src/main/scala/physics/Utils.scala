package physics
import physics.Pair

type Position = Pair[Double, Double]
type Speed = Double
type SpeedVector = Pair[Speed, Speed]
type GravityForceVector = Pair[GForce, GForce]
type Mass = Double
type GForce = Double

trait Pair[+A, +B]:
  def x: A
  def y: B
//  def map[C, D](f: A => C )(g: B => D ): physics.Pair[C, D]

object Pair:
  def apply[A, B](x: A, y: B): Pair[A, B] = PairImpl[A, B](x, y)

  private case class PairImpl[+A, +B](x: A, y: B) extends Pair[A, B]
//    override def map[C, D](f: A => C)(g: B => D): physics.Pair[C, D] = physics.Pair(f(x), g(y))
