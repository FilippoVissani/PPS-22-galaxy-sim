package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.SwingGUI.SimulationPanel
import physics.dynamics.GravitationLaws.astronomicUnit

import java.awt.*
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing.{JButton, JFrame, JPanel, SwingUtilities}
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.Statistics
import galaxy_sim.view.StatisticsFrame.pieChart
import org.jfree.chart.ChartPanel

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
    val controlPanel: JPanel = JPanel()
    val startButton: JButton = JButton("Start Simulation")
    val stopButton: JButton = JButton("Stop Simulation")

    val pieChart: PieChart = PieChart("Celestial body types")

    val pieChartPanel: ChartPanel = pieChart.wrapToPanel
    pieChartPanel.setPreferredSize(Dimension(
      25 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      50 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))

    startButton.addActionListener((_: ActionEvent) => view.start())
    stopButton.addActionListener((_: ActionEvent) => view.stop())
    controlPanel.add(startButton)
    controlPanel.add(stopButton)
    controlPanel.add(pieChartPanel)
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
        //Update the pie chart
        pieChart.clearAllValues()
        Statistics.numberOfCelestialBodiesForEachType(simulation.galaxy).filter(element => element._2 != 0).foreach(element => pieChart.addValue(element._1.toString, element._2))
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

    private def scaleXSize(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.rightBound
      Math.round(percent * this.getWidth / 100).toInt

    private def scaleYSize(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.bottomBound
      Math.round(percent * this.getHeight / 100).toInt

    private def scaleXPosition(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.rightBound
      val sizeOnDisplay = percent * this.getWidth / 100
      Math.round(sizeOnDisplay + this.getWidth / 2).toInt

    private def scaleYPosition(value: Double): Int =
      val percent = value * 100 / simulation.get.bounds.bottomBound
      val sizeOnDisplay = percent * this.getHeight / 100
      Math.round(sizeOnDisplay + this.getHeight / 2).toInt

    private def cleanPanel(g: Graphics2D): Unit = 
      g.setColor(java.awt.Color.BLACK)
      g.fillRect(0, 0, this.getWidth, this.getHeight)

    private def drawVirtualTime(g: Graphics2D, virtualTime: String): Unit = 
      g.setColor(java.awt.Color.WHITE)
      g.drawString("Virtual Time: " + virtualTime, 10, 10)

    private def drawCelestialBody(g: Graphics2D, celestialBody: CelestialBody, celestialBodyType: CelestialBodyType): Unit =
      g.setColor(java.awt.Color.WHITE)
      g.drawString(celestialBody.name, scaleXPosition(celestialBody.position.x), scaleYPosition(celestialBody.position.y))
      selectCelestialBodyColor(g, celestialBodyType)
      g.fillOval(
        scaleXPosition(celestialBody.position.x),
        scaleYPosition(celestialBody.position.y),
        scaleXSize(celestialBody.radius),
        scaleYSize(celestialBody.radius)
        )
      g.setColor(java.awt.Color.WHITE)
      g.drawOval(
        scaleXPosition(celestialBody.position.x),
        scaleYPosition(celestialBody.position.y),
        scaleXSize(celestialBody.radius),
        scaleYSize(celestialBody.radius)
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
