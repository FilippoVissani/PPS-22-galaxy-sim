package physics.collisions.rigidbody

import physics.collisions.CollisionDetection.CollisionBoxes.{CircleCollisionBox, CollisionBox}
import physics.dynamics.PhysicalEntity

object RigidBody:
  trait RigidBody extends PhysicalEntity:
    def collisionBox: CollisionBox
    
  trait CircularEntity extends RigidBody:
    def radius: Double
    override def collisionBox: CollisionBox = CircleCollisionBox(position, radius)
