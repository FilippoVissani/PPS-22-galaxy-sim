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
        (0d until 10e4 by 1d).foreach{_ =>
          model.incrementVirtualTime()
          model.moveCelestialBodiesToNextPosition()
        }
        view.display(model.simulation)

      override def startSimulation(): Unit =
        synchronized{
          val r = new Runnable :
            override def run(): Unit =
              while !stop do
                iteration()
          new Thread(r).start()
        }

      override def stopSimulation(): Unit = synchronized { stop = true }

  trait Interface extends Provider with Component:
    self: Requirements =>
