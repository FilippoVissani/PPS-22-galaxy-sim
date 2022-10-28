package galaxy_sim.model

import galaxy_sim.model.BoundaryAliases.Bound
import physics.Pair

/** Defines type aliases used in Boundary. */
object BoundaryAliases:
  type Bound = Double

/** Defines bounds used in a simulation. */
trait Boundary:

  /** Left bound. */
  def leftBound: Bound

  /** Right bound. */
  def rightBound: Bound

  /** Top bound. */
  def topBound: Bound

  /** Bottom bound. */
  def bottomBound: Bound

  /** makes bounds toroidal */
  def toToroidal(position: Pair): Pair =
    val x = position match
      case Pair(x, _) if x < leftBound => rightBound
      case Pair(x, _) if x > rightBound => leftBound
      case _ => position.x
    val y = position match
      case Pair(_, y) if y < topBound => bottomBound
      case Pair(_, y) if y > bottomBound => topBound
      case _ => position.y
    Pair(x, y)

/** Factory for Boundary. */
object Boundary:
  
  /** Creates a Boundary.
   * 
   *  @param leftBound left bound
   *  @param rightBound right bound
   *  @param topBound top bound
   *  @param bottomBound bottom bound
   */
  def apply(leftBound: Bound, rightBound: Bound, topBound: Bound, bottomBound: Bound): Boundary =
    BoundaryImpl(leftBound, rightBound, topBound, bottomBound)

  private case class BoundaryImpl(
    override val leftBound: Bound,
    override val rightBound: Bound,
    override val topBound: Bound,
    override val bottomBound: Bound) extends Boundary
