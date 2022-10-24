package physics.collisions.intersection

/** This type class provides a mechanism to express the intersection between two elements of a same type. */
trait Intersection[A]:
  /**
   * Checks whether the two elements of a certain type intersect.
   * @param a1 One of the elements.
   * @param a2 The other element.
   * @return Whether the two elements are intersecting or not.
   */
  def intersects(a1: A, a2: A): Boolean

object Intersection:
  /** Constructs an [[Intersection]] starting from a function of the type (A, A) => Boolean. */
  def from[A](f: (A, A) => Boolean): Intersection[A] = (a1: A, a2: A) => f(a1, a2)

  /**
   * Interface function for checking an intersection between two elements.
   * @param a1 One of the elements.
   * @param a2 The other element.
   * @tparam A The type of the two elements.
   * @return Whether the two elements are intersecting or not.
   */
  def intersects[A: Intersection](a1: A, a2: A): Boolean =
    summon[Intersection[A]].intersects(a1, a2)