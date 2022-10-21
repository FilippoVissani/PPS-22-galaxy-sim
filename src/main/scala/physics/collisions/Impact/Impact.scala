package physics.collisions.Impact

/** This type class provides a mechanism to express the effect of an impact between two instances of a certain type. */
trait Impact[A]:
  /**
   * Combines the two elements of A, representing the result of an impact between them.
   * @param a1 One of the elements impacting.
   * @param a2 The other impacting element.
   * @return the result of the impact.
   */
  def impact(a1: A, a2: A): A

object Impact:
  /** Constructs an [[Impact]] starting from a function of the type (A, A) => A. */
  def from[A](f: (A, A) => A): Impact[A] = (a1: A, a2: A) => f(a1, a2)

  /**
   * Interface function for computing the impact between elements.
   * @param a1 One of the impacting elements.
   * @param a2 The other impacting element.
   * @tparam A the type of the elements colliding
   * @return the result of the impact.
   */
  def impact[A: Impact](a1: A, a2: A): A =
    summon[Impact[A]].impact(a1, a2)