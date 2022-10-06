package physics.collisions

object CollisionSolving:
  trait CollisionSolver[A, B, C]:
    def solve(a: A, b: B): C