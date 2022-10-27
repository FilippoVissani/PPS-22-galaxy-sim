package physics.collisions.impact

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
   * Constructs an Impact by chaining two other impacts.
   * @param f The first impact to be applied.
   * @param g The last impact to be applied.
   * @tparam A The type of the elements impacting.
   * @return An [[Impact]] that chains the input impacts.
   */
  def composeTwo[A](f: Impact[A], g: Impact[A]): Impact[A] = (a1, a2) => g.impact(f.impact(a1, a2), a2)

  /**
   * Constructs an [[Impact]] starting from multiple impacts.
   * @param impacts The impacts to chain.
   * @tparam A The type of the elements impacting.
   * @return An [[Impact]] that chains the input impacts.
   */
  def composeMany[A](impacts: Impact[A]*): Impact[A] =
    impacts.foldLeft(impacts.head)((acc, f) => composeTwo(acc, f))

  /**
   * Interface function for computing the impact between elements.
   * @param a1 One of the impacting elements.
   * @param a2 The other impacting element.
   * @tparam A the type of the elements impacting.
   * @return the result of the impact.
   */
  def impact[A](a1: A, a2: A)(using Impact[A]): A =
    summon[Impact[A]].impact(a1, a2)