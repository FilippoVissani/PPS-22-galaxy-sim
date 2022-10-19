package physics.ref

case class Collider[A, B](fun: (A, B) => Collision[A, B])