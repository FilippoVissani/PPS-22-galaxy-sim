package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.SwingGUI.SimulationPanel
import physics.dynamics.GravitationLaws.astronomicUnit

import java.awt.{BorderLayout, Dimension, Graphics, Graphics2D, GridBagConstraints, GridBagLayout, RenderingHints, Toolkit}
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing.{JButton, JComboBox, JFrame, JLabel, JPanel, JScrollPane, JTabbedPane, JTextArea, SwingUtilities}
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.{Statistics, ViewLogger}
import galaxy_sim.utils.Percentage
import org.jfree.chart.ChartPanel
import galaxy_sim.utils.LoggerActions

import scala.annotation.tailrec

trait SwingGUI:
  def display(simulation: Simulation): Unit
  def updateInfos(): Unit
  def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int, viewLogger: ViewLogger): SwingGUI =
    SwingGUIImpl(view: View, windowWidth: Int, windowHeight: Int, viewLogger: ViewLogger)


  private class SwingGUIImpl(
                              view: View,
                              windowWidth: Int,
                              windowHeight: Int,
                              viewLogger: ViewLogger) extends SwingGUI:
    val frameSize: Dimension = Dimension(
      windowWidth * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      windowHeight * Toolkit.getDefaultToolkit.getScreenSize.height / 100
      )
    val tp: JTabbedPane = JTabbedPane()
    val mainFrame: JFrame = JFrame()
    val gbc: GridBagConstraintsBuilder = GridBagConstraintsBuilder()

    val simulationPanel: SimulationPanel = SimulationPanel()
    val simulationPanelContainer: JPanel = JPanel(GridBagLayout())
    val informationPanel: InformationPanel = InformationPanel(viewLogger, gbc.infos)
    val informationPanelContainer: JPanel = JPanel(GridBagLayout())
    val loggerPanel: LoggerPanel = LoggerPanel(viewLogger)
    val loggerPanelContainer: JPanel = JPanel(GridBagLayout())

    val statisticsPanel: StatisticsPanel = StatisticsPanel()
    val statisticsPanelContainer: JPanel =JPanel(GridBagLayout())

    val controlPanel: JPanel = JPanel()
    val startButton: JButton = JButton("Start Simulation")
    val stopButton: JButton = JButton("Stop Simulation")

    startButton.addActionListener((_: ActionEvent) => view.start())
    stopButton.addActionListener((_: ActionEvent) => view.stop())
    controlPanel.add(startButton, gbc.start)
    controlPanel.add(stopButton, gbc.stop)
    mainFrame.addWindowListener(new WindowAdapter {
      override def windowClosing(windowEvent: WindowEvent): Unit =
        System.exit(0)
    })
    simulationPanelContainer.add(simulationPanel)
    simulationPanelContainer.setPreferredSize(Dimension(
      45 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      45 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
    informationPanelContainer.add(informationPanel)
    loggerPanelContainer.add(loggerPanel)
    statisticsPanelContainer.add(statisticsPanel)
    tp.add("Info", informationPanelContainer)
    tp.add("Logger", loggerPanelContainer)
    tp.add("Statistics", statisticsPanelContainer)
    tp.setPreferredSize(Dimension(
      45 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      45 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
    mainFrame.setSize(frameSize)
    mainFrame.setResizable(false)
    mainFrame.add(controlPanel, BorderLayout.NORTH)
    mainFrame.add(simulationPanelContainer, BorderLayout.WEST)
    mainFrame.add(tp, BorderLayout.EAST)
    mainFrame.setVisible(true)

    override def updateInfos(): Unit =
      SwingUtilities.invokeLater(() => informationPanel.display())

    override def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit =
      SwingUtilities.invokeLater(() => loggerPanel.display(bodiesInvolved, description))

    override def display(simulation: Simulation): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.display(simulation)
        statisticsPanel.update(simulation)
      })
  end SwingGUIImpl

  private class InformationPanel(viewLogger: ViewLogger, gbc: GridBagConstraints) extends JPanel, ActionListener:
      var data: Option[List[String]] = Option.empty
      val dropdown: JComboBox[String] = JComboBox()
      val textArea: JTextArea = JTextArea()
      dropdown.addActionListener(this)
      dropdown.setEditable(false)
      textArea.setEditable(false)
      textArea.setPreferredSize(Dimension(
        40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
        40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
      this.add(dropdown, BorderLayout.NORTH)
      this.add(textArea, BorderLayout.CENTER)

      override def actionPerformed(e: ActionEvent): Unit =
        val cb: JComboBox[String] = e.getSource.asInstanceOf[JComboBox[String]]
        val body = viewLogger.bodyInfos(cb.getSelectedItem.asInstanceOf[String])
        if body.isDefined then
          textArea.setText(s">>> Name = ${body.get.name}\n " +
            s"Position = (${body.get.position.x}, ${body.get.position.y})\n" +
            s"Speed = ${body.get.speedVector.y / 1000} km/s\n" +
            s"Mass = ${body.get.mass} kg\n " +
            s"Temperature = ${body.get.temperature}\n " +
            s"Birth time = ${body.get.birthTime}\n\n")

      def display(): Unit =
        this.data = Some(viewLogger.getBodiesNames)
        textArea.setText("")
        if data.isDefined then
          dropdown.removeAllItems()
          data.get.foreach(d => dropdown.addItem(d))

  private class LoggerPanel(viewLogger: ViewLogger) extends JPanel:
      val textArea: JTextArea = JTextArea()
      textArea.setEditable(false)
      val scrollPane: JScrollPane = JScrollPane(textArea)
      textArea.setPreferredSize(Dimension(
        40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
        40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
      this.add(scrollPane)

      def display(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit = textArea.append(viewLogger.bodiesLogger(bodiesInvolved, description))

  private class GridBagConstraintsBuilder:
    val pie: GridBagConstraints = GridBagConstraints()
    val start: GridBagConstraints = GridBagConstraints()
    val stop: GridBagConstraints = GridBagConstraints()
    val infos: GridBagConstraints = GridBagConstraints()

    infos.gridx = 1
    infos.gridy = 1
    infos.anchor = GridBagConstraints.CENTER
    // Row 0 - Buttons
    // Col 0
    //gbc.fill = GridBagConstraints.HORIZONTAL
    start.gridx = 0;
    start.gridy = 0;
    //gbc.insets = new Insets(5, 0, 0, 10);
    start.anchor = GridBagConstraints.LINE_END
    //    this.add(startButton, gbc);

    // Col 1
    //gbc.fill = GridBagConstraints.HORIZONTAL
    stop.gridx = 1;
    stop.gridy = 0;
    stop.anchor = GridBagConstraints.LINE_START;
    //    this.add(stopButton, gbc);

    // Row 1 - Chart
    // Col 0

    //gbc.insets = new Insets(5, 0, 0, 10);
    //gbc.anchor = GridBagConstraints.CENTER;

  private class StatisticsPanel() extends JPanel(GridBagLayout()):
    val gbc: GridBagConstraints = GridBagConstraints()
    val pieChart: PieChart = PieChart("Celestial body types")
    val pieChartPanel: ChartPanel = pieChart.wrapToPanel
    pieChartPanel.setPreferredSize(Dimension(
      25 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
      50 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))

    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.gridheight = 2;
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(pieChartPanel, gbc);

    val totalBodiesLabel: JLabel = JLabel()
    gbc.gridheight = 1
    gbc.gridx = 1
    gbc.gridy = 0
    this.add(totalBodiesLabel, gbc)

    val bodiesPercentage: JTextArea = JTextArea()
    bodiesPercentage.setEditable(false)
    gbc.gridx = 1
    gbc.gridy = 1
    this.add(bodiesPercentage, gbc)

    def update(simulation: Simulation): Unit =
      //update the pie chart
      pieChart.clearAllValues()
      Statistics.numberOfCelestialBodiesForEachType(simulation.galaxy).filter(element => element._2 != 0).foreach(element => pieChart.setValue(element._1.toString, element._2))

      //update total bodies count
      totalBodiesLabel.setText(s"Total bodies: ${Statistics.quantityOfTotalBodies(simulation.galaxy).toString}")

      //update percentages count
      bodiesPercentage.setText(s"Percentages: \n${createStringOfPercentages(Statistics.percentageOfCelestialBodiesForEachType(simulation.galaxy))}")

    @tailrec
    private def createStringOfPercentages(galaxy: Map[CelestialBodyType, Percentage], percentagesAsText: String = ""): String = galaxy.size match
      case 0 => percentagesAsText
      case _ => {
        val newPercentage = s"${galaxy.head._1.toString}: ${galaxy.head._2}%"
        createStringOfPercentages(galaxy.tail, newPercentage + "\n" + percentagesAsText)
      }


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
