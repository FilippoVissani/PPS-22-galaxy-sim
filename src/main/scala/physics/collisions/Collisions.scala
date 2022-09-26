package physics.collisions

import physics.collisions.Collisions.Colliders.{CircleCollider, Collider}
import physics.collisions.Collisions.RigidBody
import physics.dynamics.PhysicalEntity

object Collisions:
  type Collision[A <: Collider] = (RigidBody[A], RigidBody[A])
  type CollisionResult[A <: Collider] = List[RigidBody[A]]
  case class P2d(x: Double, y: Double)
  trait RigidBody[T <: Collider] extends PhysicalEntity:
    def collider: T

  object Colliders:
    trait Collider
    case class CircleCollider(origin: P2d, radius: Double) extends Collider
    case class RectangleCollider(topLeft: P2d, height: Double, width: Double) extends Collider

  import Colliders.*

  object CollisionManager:
    import CollisionDetectors.given
    def detect[A <: Collider](c1: RigidBody[A], c2: RigidBody[A])(using col: CollisionDetector[A]): Option[Collision[A]] =
      if col.detect(c1.collider, c2.collider) then
        Option(c1, c2)
      else
        None
    def process[A <: Collider](c1: RigidBody[A], c2: RigidBody[A])
                              (using dec: CollisionDetector[A])
                              (using sol: CollisionSolver[A]): Option[CollisionResult[A]] =
      detect(c1, c2).map(c => sol.solve(c))

  trait CollisionDetector[A <: Collider]:
    def detect(c1: A, c2: A): Boolean

  trait CollisionSolver[A <: Collider]:
    def solve(c: Collision[A]): CollisionResult[A]

  object CollisionDetectors:
    given CircleToCircleDetector: CollisionDetector[CircleCollider] with
      override def detect(c1: CircleCollider, c2: CircleCollider): Boolean =
        val dx = c1.origin.x - c2.origin.x
        val dy = c1.origin.y - c2.origin.y
        val dist = Math.sqrt(dx*dx + dy*dy)
        dist < c1.radius + c2.radius

    given RectangleToRectangleDetector: CollisionDetector[RectangleCollider] with
      override def detect(c1: RectangleCollider, c2: RectangleCollider): Boolean =
        ! (c1.topLeft.x > c2.topLeft.x + c2.width ||
          c1.topLeft.x + c1.width < c2.topLeft.x ||
          c1.topLeft.y > c2.topLeft.y + c2.height ||
          c1.height + c1.topLeft.y < c2.topLeft.y)
        
  object CollisionSolvers:
    given StarToGasNebulaSolver: CollisionSolver[CircleCollider] with
      override def solve(c: (RigidBody[CircleCollider], RigidBody[CircleCollider])): CollisionResult[CircleCollider] =
        c._1.mass match
          case _ if c._1.mass > c._2.mass => List(c._1)
          case _ if c._2.mass > c._1.mass => List(c._2)