package physics.collisions

import physics.collisions.CollisionDetection.Colliders.Collider
import physics.collisions.CollisionDetection.CollisionDetector
import physics.collisions.CollisionEngine.RigidBody
import physics.collisions.CollisionSolving.CollisionSolver
import physics.dynamics.PhysicalEntity

object CollisionEngine:

  trait RigidBody[T <: Collider] extends PhysicalEntity :
    def collider: T

  def detect[A <: Collider, B <: Collider](c1: A, c2: B)(using col: CollisionDetector[A, B]): Boolean =
    col.detect(c1, c2)

  def process[T <: Collider, Z <: RigidBody[T]](e1: Z, e2: Z)
                                               (using col: CollisionDetector[T, T])
                                               (using sol: CollisionSolver[Z]): List[Z] =
    if detect(e1.collider, e2.collider) then
      sol.solve(e1, e2)
    else
      List.empty
