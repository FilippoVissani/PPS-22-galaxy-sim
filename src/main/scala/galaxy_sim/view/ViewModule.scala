package galaxy_sim.view

import galaxy_sim.controller.ControllerModule
import galaxy_sim.model.Entity
import java.awt.{Dimension, Toolkit}
import javax.swing.{JFrame, JPanel}

object ViewModule:
  trait View:
    def update(entities: Set[Entity]): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class GraphicalView(windowWidth: Int, windowHeight: Int) extends View:
      val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)

      override def update(entities: Set[Entity]): Unit =
        gui.update(entities)

    class TextualView extends View:
      override def update(entities: Set[Entity]): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
