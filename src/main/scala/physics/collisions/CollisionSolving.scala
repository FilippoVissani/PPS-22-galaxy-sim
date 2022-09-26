package physics.collisions

import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.CollisionDetection.Colliders.{CircleCollider, Collider, RectangleCollider}
import physics.collisions.CollisionDetection.{P2d, RigidBody}
import physics.dynamics.PhysicalEntity

object CollisionSolving:


  trait CollisionSolver[CollidingEntity]:
    def solve(c: (CollidingEntity, CollidingEntity)): List[CollidingEntity]

  class SphericalEntity(override val mass: Mass,
                        override val position: Position,
                        override val aphelionSpeed: Speed,
                        override val speedVector: SpeedVector,
                        override val gForceVector: GravityForceVector,
                        radius: Double) extends PhysicalEntity with RigidBody[CircleCollider]:
    override def collider: CircleCollider = CircleCollider(P2d(position.x, position.y), radius)

  object CollisionSolvers:
    given SphericalEntitySolver: CollisionSolver[SphericalEntity] with
      override def solve(c: (SphericalEntity, SphericalEntity)): List[SphericalEntity] =
        if c._1.mass > c._2.mass then
          List(c._1)
        else
          List(c._2)