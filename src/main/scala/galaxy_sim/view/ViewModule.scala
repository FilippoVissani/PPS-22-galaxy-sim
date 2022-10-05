package galaxy_sim.view

import galaxy_sim.controller.ControllerModule
import galaxy_sim.model.{CelestialBody, Simulation}
import java.awt.{Dimension, Toolkit}
import javax.swing.{JFrame, JPanel}

object ViewModule:
  trait View:
    def display(simulation: Simulation): Unit
    def start(): Unit
    def stop(): Unit

  trait Provider:
    def view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class GraphicalView(windowWidth: Int, windowHeight: Int) extends View:
      val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)

      override def display(simulation: Simulation): Unit =
        gui.update(simulation)

      override def start(): Unit = context.controller.startSimulation()

      override def stop(): Unit = context.controller.stopSimulation()

    class FakeView extends View:
      override def display(simulation: Simulation): Unit = ???

      override def start(): Unit = ???

      override def stop(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
