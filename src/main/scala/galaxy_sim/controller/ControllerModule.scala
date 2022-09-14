package galaxy_sim.controller

import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule

object ControllerModule:
  trait Controller:
    def startSimulation(): Unit
    def stopSimulation(): Unit
    def pauseSimulation(): Unit
    def resumeSimulation(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    object Controller:
      def apply(): Controller = ControllerImpl()

      private class ControllerImpl extends Controller:
        override def startSimulation(): Unit = ???

        override def stopSimulation(): Unit = ???

        override def pauseSimulation(): Unit = ???

        override def resumeSimulation(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
