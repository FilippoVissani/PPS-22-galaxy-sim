package galaxy_sim.model

object ModelModule:
  trait Model:
    def entities: Seq[Entity]

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      override val entities: Seq[Entity] = (0 to 9) map (_ => EntityGenerator.generateRandomEntity(700))

  trait Interface extends Provider with Component
