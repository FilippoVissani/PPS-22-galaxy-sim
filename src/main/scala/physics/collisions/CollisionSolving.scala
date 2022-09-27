package physics.collisions

import physics.{GravityForceVector, Mass, Position, Speed, SpeedVector}
import physics.collisions.CollisionDetection.Colliders.{CircleCollider, Collider, RectangleCollider}
import physics.collisions.CollisionDetection.P2d
import physics.collisions.CollisionEngine.RigidBody
import physics.dynamics.PhysicalEntity

object CollisionSolving:

/*Collision solving input: can be TWO different subtypes of PhysicalEntity (ex Spherical and a custom entity like a trigger)
  Collision solving output: a variable number of entities (possibly with different types), for ex:
  -0 if the two entities colliding destroy themselves
  -1 for example when a star/black hole absorbs galactic dust or a planet or a smaller star, increasing its mass
  -* if the result of the collision is a system of entities or if we want to simulate elastic-inelastic impacts
    (by returning the same entities with new speed vectors and directions for example)
*/
  trait CollisionSolver[T <: PhysicalEntity]:
    def solve(c: (T, T)): List[T]

  class SphericalEntity(override val mass: Mass,
                        override val position: Position,
                        override val aphelionSpeed: Speed,
                        override val speedVector: SpeedVector,
                        override val gForceVector: GravityForceVector,
                        radius: Double) extends RigidBody[CircleCollider]:
    override def collider: CircleCollider = CircleCollider(P2d(position.x, position.y), radius)

  object CollisionSolvers:
    given SphericalEntitySolver: CollisionSolver[SphericalEntity] with
      override def solve(c: (SphericalEntity, SphericalEntity)): List[SphericalEntity] =
        if c._1.mass > c._2.mass then
          List(c._1)
        else
          List(c._2)