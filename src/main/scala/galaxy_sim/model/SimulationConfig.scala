package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector
import physics.dynamics.GravitationLaws.astronomicUnit

object SimulationConfig:
  val bounds: Boundary = Boundary(0, astronomicUnit * 3, 0, astronomicUnit * 3)
  
  val blackHole: CelestialBody =
    CelestialBody(mass = 2.0e30,
      aphelionSpeed = 0,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 0),
      position = Pair(0, 0),
      name = "Black Hole",
      radius = 20,
      temperature = 1100)

  val interstellarCloud: CelestialBody =
    CelestialBody(mass = 5.972e24,
      aphelionSpeed = 29290,
      gForceVector = Pair(0, 0),
      speedVector = Pair(0, 29290),
      position = Pair(astronomicUnit * 1.0167, 0),
      name = "Interstellar Cloud",
      radius = 10,
      temperature = 0)
