package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector

object SimulationConfig:
  val bounds: Boundary = Boundary(0, 1000, 0, 1000)
  
  val blackHole: CelestialBody =
    CelestialBody(mass = 750,
      aphelionSpeed = 20,
      gForceVector = Pair(0, 0.98),
      speedVector = Pair(0, 0),
      position = Pair(500, 500),
      name = "Black Hole",
      radius = 20,
      temperature = 1100)

  val interstellarCloud: CelestialBody =
    CelestialBody(mass = 750,
      aphelionSpeed = 20,
      gForceVector = Pair(0, 0.98),
      speedVector = Pair(20, 20),
      position = Pair(400, 400),
      name = "Interstellar Cloud",
      radius = 10,
      temperature = 0)
