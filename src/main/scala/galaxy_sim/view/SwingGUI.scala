package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.SwingGUI.SimulationPanel
import physics.dynamics.GravitationLaws.astronomicUnit
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.*
import javax.swing.{JButton, JFrame, JPanel, SwingUtilities}

trait SwingGUI:
  def display(simulation: Simulation): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int): SwingGUI =
    SwingGUIImpl(view: View, windowWidth: Int, windowHeight: Int)

  private class SwingGUIImpl(
    view: View,
    windowWidth: Int,
    windowHeight: Int) extends SwingGUI:
    val frameSize: Dimension = Dimension(windowWidth * Toolkit.getDefaultToolkit.getScreenSize.width / 100, windowHeight * Toolkit.getDefaultToolkit.getScreenSize.height / 100)
    val mainFrame: MainFrame = MainFrame()
    val simulationPanel: SimulationPanel = SimulationPanel()
    val simulationPanelContainer: JPanel = JPanel(GridBagLayout())
    val controlPanel: JPanel = JPanel()
    val startButton: JButton = JButton("Start Simulation")
    val stopButton: JButton = JButton("Stop Simulation")

    startButton.addActionListener((_: ActionEvent) => view.start())
    stopButton.addActionListener((_: ActionEvent) => view.stop())
    controlPanel.add(startButton)
    controlPanel.add(stopButton)
    mainFrame.addWindowListener(new WindowAdapter {
      override def windowClosing(windowEvent: WindowEvent): Unit =
        System.exit(0)
    })
    simulationPanelContainer.add(simulationPanel)
    mainFrame.setSize(frameSize)
    mainFrame.setResizable(false)
    mainFrame.mainPanel.add(controlPanel, BorderLayout.EAST)
    mainFrame.mainPanel.add(simulationPanelContainer, BorderLayout.CENTER)
    mainFrame.setVisible(true)

    override def display(simulation: Simulation): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.display(simulation)
      })
  end SwingGUIImpl

  private class MainFrame extends JFrame:
    val mainPanel: JPanel = JPanel(BorderLayout())
    this.getContentPane.add(mainPanel)

  private class SimulationPanel extends JPanel:
    var simulation: Option[Simulation] = Option.empty

    def display(simulation: Simulation): Unit =
      this.simulation = Option(simulation)
      repaint()

    override def paint(g: Graphics): Unit =
      if simulation.isDefined then
        val g2: Graphics2D = g.asInstanceOf[Graphics2D]
        g2.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON
        )
        g2.setRenderingHint(
          RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY
        )
        g2.setColor(java.awt.Color.BLACK)
        g2.fillRect(0, 0, this.getWidth, this.getHeight)
        g2.setColor(java.awt.Color.WHITE)
        g2.drawString(simulation.get.virtualTime.toString, 10, 10)
        simulation.get.galaxy.foreach((k, v) =>
          v.foreach(e =>
            g2.fillOval(
            scaleX(e.position.x),
            scaleY(e.position.y),
            e.radius.toInt,
            e.radius.toInt
          ))
        )
        simulation.get.galaxy.values.foreach(v =>
          v.foreach(e => 
            g2.drawString(e.name, scaleX(e.position.x), scaleY(e.position.y))
          )
        )
        g2.setColor(java.awt.Color.BLACK)
        simulation.get.galaxy.values.foreach(v =>
          v.foreach(e =>
            g2.drawOval(
            scaleX(e.position.x),
            scaleY(e.position.y),
            e.radius.toInt,
            e.radius.toInt
            )
          )
        )

    override def getPreferredSize: Dimension =
      val d: Dimension = this.getParent.getSize()
      var newSize: Int = if d.width > d.height then d.height else d.width
      newSize = if newSize == 0 then 100 else newSize
      Dimension(newSize, newSize)

    private def scaleX(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.rightBound
      val sizeOnDisplay = percent * this.getWidth / 100
      Math.round(sizeOnDisplay + this.getWidth / 2).toInt

    private def scaleY(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.bottomBound
      val sizeOnDisplay = percent * this.getHeight / 100
      Math.round(sizeOnDisplay + this.getHeight / 2).toInt
  end SimulationPanel
