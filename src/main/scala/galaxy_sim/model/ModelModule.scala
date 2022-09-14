package galaxy_sim.model

object ModelModule:
  trait Model:
    def entities(): Set[Entity]

  trait Provider:
    val model: Model

  trait Component:
    object Model:
      def apply(): Model = ModelImpl()

      private class ModelImpl extends Model:
        override def entities(): Set[Entity] = Set()

  trait Interface extends Provider with Component
