package galaxy_sim.model

import galaxy_sim.model.BoundaryAliases.Bound

object BoundaryAliases:
  type Bound = Double

trait Boundary:
  def leftBound: Bound
  def rightBound: Bound
  def topBound: Bound
  def bottomBound: Bound

object Boundary:
  def apply(leftBound: Bound, rightBound: Bound, topBound: Bound, bottomBound: Bound): Boundary =
    BoundaryImpl(leftBound, rightBound, topBound, bottomBound)

  private case class BoundaryImpl(override val leftBound: Bound,
                          override val rightBound: Bound,
                          override val topBound: Bound,
                          override val bottomBound: Bound) extends Boundary
