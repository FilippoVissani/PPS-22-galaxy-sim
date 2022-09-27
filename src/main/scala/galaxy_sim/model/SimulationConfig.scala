package galaxy_sim.model

import physics.dynamics.PhysicalEntity
import physics.Pair
import physics.SpeedVector

object SimulationConfig:
  val bounds: Boundary = Boundary(0, 1000, 0, 1000)
  
  val blackHole: CelestialBody = CelestialBody(
    name = "Black Hole",
    body = Body(
      radius = 20,
      temperature = 1100,
      physicalEntity = PhysicalEntity(
        gForceVector = Pair(0, 0.98),
        speedVector = Pair(0, 0),
        mass = 750,
        pos = Pair(500, 500),
        aphelionSpeed = 20)
    ))

  val interstellarCloud: CelestialBody = CelestialBody(
    name = "Interstellar Cloud",
    body = Body(
      radius = 10,
      temperature = 0,
      physicalEntity = PhysicalEntity(
        gForceVector = Pair(0, 0.98),
        speedVector = Pair(100, 100),
        mass = 750,
        pos = Pair(400, 400),
        aphelionSpeed = 20)
    ))
