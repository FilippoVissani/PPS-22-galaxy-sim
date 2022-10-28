package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.SwingGUI.SimulationPanel

import java.awt.{BorderLayout, Dimension, Graphics, Graphics2D, GridBagConstraints, GridBagLayout, RenderingHints, Toolkit}
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing.{JButton, JComboBox, JFrame, JLabel, JPanel, JScrollPane, JTabbedPane, JTextArea, SwingUtilities}
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.Statistics
import galaxy_sim.utils.Percentage
import org.jfree.chart.ChartPanel
import galaxy_sim.view.StatisticsPanel

trait SwingGUI:
  def display(simulation: Simulation): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int): SwingGUI =
    SwingGUIImpl(view: View, windowWidth: Int, windowHeight: Int)


  private class SwingGUIImpl(
                              view: View,
                              windowWidth: Int,
                              windowHeight: Int) extends SwingGUI:
    val frameSize: Dimension = Dimension(
      windowWidth * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      windowHeight * Toolkit.getDefaultToolkit.getScreenSize.height / 100
      )
    val mainFrame: MainFrame = MainFrame()
    val simulationPanel: SimulationPanel = SimulationPanel()
    val simulationPanelContainer: JPanel = JPanel(GridBagLayout())

    val statisticsPanel: StatisticsPanel = StatisticsPanel()

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
    simulationPanelContainer.setPreferredSize(Dimension(
      45 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      45 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
    mainFrame.setSize(frameSize)
    mainFrame.setResizable(false)
    mainFrame.add(controlPanel, BorderLayout.NORTH)
    mainFrame.add(simulationPanelContainer, BorderLayout.WEST)
    mainFrame.add(statisticsPanel, BorderLayout.EAST)
    mainFrame.setVisible(true)

    override def display(simulation: Simulation): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.display(simulation)
        statisticsPanel.update(simulation)
      })
  end SwingGUIImpl

  private class MainFrame extends JFrame :
    val mainPanel: JPanel = JPanel(BorderLayout())
    this.getContentPane.add(mainPanel)

  private class SimulationPanel extends JPanel:
    var simulation: Option[Simulation] = Option.empty

    def display(simulation: Simulation): Unit =
      this.simulation = Option(simulation)
      repaint()

    override def paint(g: Graphics): Unit =
      simulation.foreach(x =>{
        val g2: Graphics2D = g.asInstanceOf[Graphics2D]
        g2.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON
        )
        g2.setRenderingHint(
          RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY
        )
        cleanPanel(g2)
        drawVirtualTime(g2, x.virtualTime.toString)
        x.galaxy.foreach((k, v) => {
          v.foreach(y => drawCelestialBody(g2, y, k))
        })
      })

    override def getPreferredSize: Dimension =
      val d: Dimension = this.getParent.getSize()
      var newSize: Int = if d.width > d.height then d.height else d.width
      newSize = if newSize == 0 then 100 else newSize
      Dimension(newSize, newSize)

    private def scaleX(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.rightBound
      Math.round(percent * this.getWidth / 100).toInt

    private def scaleY(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.bottomBound
      Math.round(percent * this.getHeight / 100).toInt

    private def cleanPanel(g: Graphics2D): Unit = 
      g.setColor(java.awt.Color.BLACK)
      g.fillRect(0, 0, this.getWidth, this.getHeight)

    private def drawVirtualTime(g: Graphics2D, virtualTime: String): Unit = 
      g.setColor(java.awt.Color.WHITE)
      g.drawString("Virtual Time: " + virtualTime, 10, 10)

    private def drawCelestialBody(g: Graphics2D, celestialBody: CelestialBody, celestialBodyType: CelestialBodyType): Unit =
      g.setColor(java.awt.Color.WHITE)
      g.drawString(celestialBody.name, scaleX(celestialBody.position.x), scaleY(celestialBody.position.y))
      selectCelestialBodyColor(g, celestialBodyType)
      g.fillOval(
        scaleX(celestialBody.position.x),
        scaleY(celestialBody.position.y),
        scaleX(celestialBody.radius),
        scaleY(celestialBody.radius)
        )
      g.setColor(java.awt.Color.WHITE)
      g.drawOval(
        scaleX(celestialBody.position.x),
        scaleY(celestialBody.position.y),
        scaleX(celestialBody.radius),
        scaleY(celestialBody.radius)
        )
      
    private def selectCelestialBodyColor(g: Graphics2D, celestialBodyType: CelestialBodyType): Unit =
      celestialBodyType match
        case MassiveStar => g.setColor(java.awt.Color.YELLOW)
        case RedSuperGiant => g.setColor(java.awt.Color.RED)
        case Supernova => g.setColor(java.awt.Color.BLUE)
        case BlackHole => g.setColor(java.awt.Color.BLACK)
        case Planet => g.setColor(java.awt.Color.DARK_GRAY)
        case Asteroid => g.setColor(java.awt.Color.WHITE)
        case InterstellarCloud => g.setColor(java.awt.Color.CYAN)
    
  end SimulationPanel
