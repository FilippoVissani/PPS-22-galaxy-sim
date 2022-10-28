package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.SimulationPanel
import physics.dynamics.GravitationLaws.*

import java.awt.{BorderLayout, Dimension, Graphics, Graphics2D, GridBagConstraints, GridBagLayout, RenderingHints, Toolkit}
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import javax.swing.{JButton, JComboBox, JFrame, JLabel, JPanel, JScrollPane, JTabbedPane, JTextArea, SwingUtilities}
import galaxy_sim.model.CelestialBodyType
import galaxy_sim.model.CelestialBodyType.*
import galaxy_sim.utils.{Statistics, ViewLogger}
import galaxy_sim.utils.Percentage
import org.jfree.chart.ChartPanel
import galaxy_sim.utils.LoggerActions

trait SwingGUI:
  def display(simulation: Simulation): Unit
  def updateInfos(): Unit
  def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit

object SwingGUI:
  def apply(view: View, windowWidth: Int, windowHeight: Int, viewLogger: ViewLogger): SwingGUI =
    SwingGUIImpl(view: View, windowWidth: Int, windowHeight: Int, viewLogger: ViewLogger)


  private class SwingGUIImpl(view: View,
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
    val informationPanel: InformationPanel = InformationPanel(viewLogger)
    val informationPanelContainer: JPanel = JPanel(GridBagLayout())
    val loggerPanel: LoggerPanel = LoggerPanel(viewLogger)
    val loggerPanelContainer: JPanel = JPanel(GridBagLayout())

    val statisticsPanel: StatisticsPanel = StatisticsPanel()
    val statisticsPanelContainer: JPanel = JPanel(GridBagLayout())

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

    override def updateInfos(): Unit = SwingUtilities.invokeLater(() => informationPanel.display())

    override def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit = SwingUtilities.invokeLater(() => loggerPanel.display(bodiesInvolved, description))

    override def display(simulation: Simulation): Unit =
      SwingUtilities.invokeLater(() => {
        simulationPanel.display(simulation)
        statisticsPanel.update(simulation)
      })
  end SwingGUIImpl

  private class GridBagConstraintsBuilder:
    val start: GridBagConstraints = GridBagConstraints()
    val stop: GridBagConstraints = GridBagConstraints()

    start.gridx = 0
    start.gridy = 0
    start.anchor = GridBagConstraints.LINE_END

    stop.gridx = 1;
    stop.gridy = 0;
    stop.anchor = GridBagConstraints.LINE_START