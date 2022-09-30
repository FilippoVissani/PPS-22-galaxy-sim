package galaxy_sim.controller

import galaxy_sim.model.{ModelModule, Simulation}
import galaxy_sim.view.ViewModule
import scala.math.BigDecimal.double2bigDecimal

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
    class ControllerImpl extends Controller:

      def iteration(): Unit =
        (0d until 10e4 by 1d).foreach{_ =>
          model.incrementVirtualTime()
          model.moveCelestialBodiesToNextPosition()
        }
        view.display(model.simulation)

      override def startSimulation(): Unit =
        val r = new Runnable:
          override def run(): Unit =
            while true do
              iteration()
        new Thread(r).start()
/*      for _ <- Future{ view.update(model.simulation.celestialBodies) } yield ()
       for
        calculate next position
        calculate collisions
        introduce new entities based on collisions
        calculate lifecycle
        update view*/


      override def stopSimulation(): Unit = ???

      override def pauseSimulation(): Unit = ???

      override def resumeSimulation(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
