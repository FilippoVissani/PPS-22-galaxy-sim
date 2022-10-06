package galaxy_sim.controller

import galaxy_sim.model.{ModelModule, Simulation}
import galaxy_sim.view.ViewModule
import scala.math.BigDecimal.double2bigDecimal

object ControllerModule:
  trait Controller:
    def startSimulation(): Unit
    def stopSimulation(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      var stop: Boolean = false

      def iteration(): Unit =
        model.incrementVirtualTime()
        model.moveCelestialBodiesToNextPosition()

      override def startSimulation(): Unit =

        /**
         * iteration in the model
         */
        val r = new Runnable :
          override def run(): Unit =
            while !stop do
              iteration()
              Thread.sleep(1)
        new Thread(r).start()

        /**
         * view 30fps
         */
        val v = new Runnable :
          override def run(): Unit =
            while !stop do
              view.display(model.simulation)
              Thread.sleep(33)
        new Thread(v).start()

      override def stopSimulation(): Unit = synchronized { stop = true }

  trait Interface extends Provider with Component:
    self: Requirements =>
