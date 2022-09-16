package galaxy_sim.model

trait Pair[+A, +B]:
  val x: A

  val y: B

  def map[C, D](f: A => C )(g: B => D ): Pair[C, D]

object Pair:
  def apply[A, B](x: A, y: B): Pair[A, B] = PairImpl[A, B](x, y)

  private case class PairImpl[+A, +B](x: A, y: B) extends Pair[A, B]:

    override def map[C, D](f: A => C)(g: B => D): Pair[C, D] = Pair(f(x), g(y))
