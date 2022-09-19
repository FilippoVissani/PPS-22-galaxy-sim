package galaxy_sim.model

object ModelModule:
  trait Model:
    def entities: Set[Entity]

  trait Provider:
    def model: Model

  trait Component:
    class ModelImpl extends Model:
      override val entities: Set[Entity] = Set(
        Entity(name = "Star1",
          mass = 10,
          volume = 10,
          speed = 10,
          acceleration = 10,
          position = Pair(100, 100))
      )

  trait Interface extends Provider with Component
