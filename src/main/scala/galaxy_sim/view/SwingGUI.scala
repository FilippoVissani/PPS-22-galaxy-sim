package galaxy_sim.view

import galaxy_sim.model.Entity
import galaxy_sim.view.SwingGUI.SimulationPanel
import galaxy_sim.view.ViewModule.View
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.{BorderLayout, Dimension, Graphics, Graphics2D, RenderingHints, Toolkit}
import javax.swing.{JButton, JFrame, JPanel, SwingUtilities}

trait SwingGUI:
  def update(entities: Seq[Entity]): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int): SwingGUI =
    SwingSimulationGUIImpl(view: View, windowWidth: Int, windowHeight: Int)

  private class SwingSimulationGUIImpl(view: View, windowWidth: Int, windowHeight: Int) extends SwingGUI:
    val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    val mainFrame: MainFrame = MainFrame()
    val simulationPanel: SimulationPanel = SimulationPanel()
    val controlPanel: JPanel = JPanel()
    val startButton: JButton = JButton("Start Simulation")
    val pauseButton: JButton = JButton("Pause Simulation")
    
    startButton.addActionListener((_: ActionEvent) => view.start())
    controlPanel.add(startButton)
    controlPanel.add(pauseButton)
    mainFrame.addWindowListener(new WindowAdapter {
      override def windowClosing(windowEvent: WindowEvent): Unit = System.exit(0)
    })
    mainFrame.setSize(Dimension(windowWidth * screenSize.width / 100, windowHeight * screenSize.height / 100))
    mainFrame.setResizable(false)
    mainFrame.mainPanel.add(controlPanel, BorderLayout.NORTH)
    mainFrame.mainPanel.add(simulationPanel, BorderLayout.CENTER)
    mainFrame.setVisible(true)

    override def update(entities: Seq[Entity]): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.entities_(entities)
      })
  end SwingSimulationGUIImpl

  private class MainFrame extends JFrame:
    val mainPanel: JPanel = JPanel(BorderLayout())
    this.getContentPane.add(mainPanel)

  private class SimulationPanel extends JPanel:
    var entities: Seq[Entity] = Seq()

    def entities_(entities: Seq[Entity]): Unit =
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
      g2.setColor(java.awt.Color.BLACK)
      entities.foreach(e => g2.drawOval(e.position.x.toInt, e.position.y.toInt, e.volume.toInt, e.volume.toInt))
      entities.foreach(e => g2.drawString(e.name, e.position.x, e.position.y))
  end SimulationPanel
