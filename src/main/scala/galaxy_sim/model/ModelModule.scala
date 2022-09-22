package galaxy_sim.model

object ModelModule:
  trait Model:
    def entities: Seq[CelestialBody]

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      override val entities: Seq[CelestialBody] = (0 to 9) map (_ => CelestialBodyGenerator.generateRandomCelestialBody(150))

  trait Interface extends Provider with Component
