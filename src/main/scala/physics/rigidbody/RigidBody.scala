package physics.rigidbody

import CollisionBoxes.{CircleCollisionBox, CollisionBox}
import physics.dynamics.PhysicalEntity

object RigidBody:
  /** Ties the [[PhysicalEntity]] to the collision world using [[CollisionBox]] */
  trait RigidBody extends PhysicalEntity:
    def collisionBox: CollisionBox
    
  /** A circular [[RigidBody]] */
  trait CircularEntity extends RigidBody:
    def radius: Double
    override def collisionBox: CircleCollisionBox = CircleCollisionBox(position, radius)
