package galaxy_sim.view

import galaxy_sim.controller.ControllerModule
import java.awt.{Dimension, Toolkit}

object ViewModule:
  trait View:
    def update(): Unit
    def start(): Unit
    def stop(): Unit
    def pause(): Unit
    def resume(): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class GraphicalView(windowWidth: Int, windowHeight: Int) extends View:
      val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
      val simulationFrame: SimulationFrame = SimulationFrame(this, Dimension(windowWidth * screenSize.width / 100, windowHeight * screenSize.height / 100))

      override def update(): Unit = ???

      override def start(): Unit = ???

      override def stop(): Unit = ???

      override def pause(): Unit = ???

      override def resume(): Unit = ???
    end GraphicalView

    class TextualView extends View:
      override def update(): Unit = ???

      override def start(): Unit = ???

      override def stop(): Unit = ???

      override def pause(): Unit = ???

      override def resume(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
