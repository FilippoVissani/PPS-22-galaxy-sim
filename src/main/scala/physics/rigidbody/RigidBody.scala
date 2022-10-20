package physics.rigidbody

import physics.ref.CollisionBoxes.{CircleCollisionBox, CollisionBox}
import physics.dynamics.PhysicalEntity

object RigidBody:
  trait RigidBody extends PhysicalEntity:
    def collisionBox: CollisionBox
    
  trait CircularEntity extends RigidBody:
    def radius: Double
    override def collisionBox: CircleCollisionBox = CircleCollisionBox(position, radius)
