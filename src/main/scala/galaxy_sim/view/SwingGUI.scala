package galaxy_sim.view

import galaxy_sim.model.Entity
import galaxy_sim.view.SwingGUI.SimulationPanel
import galaxy_sim.view.ViewModule.View
import java.awt.{Dimension, Graphics, Graphics2D, RenderingHints, Toolkit}
import javax.swing.{JFrame, JPanel, SwingUtilities}

trait SwingGUI:
  def update(entities: Set[Entity]): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int): SwingGUI =
    SwingSimulationGUIImpl(view: View, windowWidth: Int, windowHeight: Int)

  private class SwingSimulationGUIImpl(view: View, windowWidth: Int, windowHeight: Int) extends SwingGUI:
    val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    val mainFrame: MainFrame = MainFrame()
    val simulationPanel: SimulationPanel = SimulationPanel()

    mainFrame.setDefaultCloseOperation(0)
    mainFrame.setSize(Dimension(windowWidth * screenSize.width / 100, windowHeight * screenSize.height / 100))
    mainFrame.getContentPane.add(simulationPanel)
    mainFrame.setVisible(true)

    override def update(entities: Set[Entity]): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.entities_(entities)
      })

  private class MainFrame extends JFrame

  private class SimulationPanel extends JPanel:
    var entities: Set[Entity] = Set()

    def entities_(entities: Set[Entity]): Unit =
      this.entities = entities
      repaint()

    override def paint(g: Graphics): Unit =
      val g2: Graphics2D = g.asInstanceOf[Graphics2D]
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
      g2.setColor(java.awt.Color.BLACK)
      g2.fillRect(0, 0, this.getWidth, this.getHeight)
      g2.setColor(java.awt.Color.WHITE)
      entities.foreach(e => g2.fillOval(e.position.x.toInt, e.position.y.toInt, e.volume.toInt, e.volume.toInt))

