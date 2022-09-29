package galaxy_sim.controller

import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
      var stop: Boolean = false

      override def startSimulation(): Unit =
        view.update(model.simulation.celestialBodies)
        val r = new Runnable:
          override def run(): Unit =
            while true do
              model.incrementVirtualTime()
              model.moveCelestialBodiesToNextPosition()
              view.update(model.simulation.celestialBodies)
              Thread.sleep(200)

        new Thread(r).start()
      //for _ <- Future{ view.update(model.simulation.celestialBodies) } yield ()
      // for
      //  calculate next position
      //  calculate collisions
      //  introduce new entities based on collisions
      //  calculate lifecycle
      //  update view


      override def stopSimulation(): Unit = stop = true

      override def pauseSimulation(): Unit = ???

      override def resumeSimulation(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
