package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.StatisticsFrame.StatisticsPanel
import galaxy_sim.view.View
import galaxy_sim.utils.Statistics
import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.*
import javax.swing.{JButton, JFrame, JPanel, SwingUtilities}
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.general.DefaultPieDataset

trait StatisticsFrame:
  def display(simulation: Simulation): Unit

object StatisticsFrame:
  def apply(view: View, windowPercentualWidth: Int, windowPercentualHeight: Int): StatisticsFrame =
    StatisticsFrameImpl(view: View, windowPercentualWidth: Int, windowPercentualHeight: Int)

  private class StatisticsFrameImpl(
                              view: View,
                              windowPercentualWidth: Int,
                              windowPercentualHeight: Int) extends StatisticsFrame :
    val frameSize: Dimension = Dimension(windowPercentualWidth * Toolkit.getDefaultToolkit.getScreenSize.width / 100, windowPercentualHeight * Toolkit.getDefaultToolkit.getScreenSize.height / 100)
    val mainFrame: MainFrame = MainFrame()
    val statisticsPanel: StatisticsPanel = StatisticsPanel()
    val simulationPanelContainer: JPanel = JPanel(GridBagLayout())

    simulationPanelContainer.add(statisticsPanel)
    mainFrame.setSize(frameSize)
    mainFrame.setResizable(false)
    mainFrame.mainPanel.add(simulationPanelContainer, BorderLayout.CENTER)
    mainFrame.setVisible(true)

    override def display(simulation: Simulation): Unit =
      SwingUtilities.invokeLater(() => {
        statisticsPanel.display(simulation)
      })
  end StatisticsFrameImpl

  val dataset: DefaultPieDataset[String] = DefaultPieDataset[String]()

  private class MainFrame extends JFrame :

    def createPieChartPanel(): ChartPanel =
      val chart = ChartFactory.createPieChart(
        "Celestial bodies types",
        dataset,
        true,
        true,
        false);
      ChartPanel(chart)

    val mainPanel: ChartPanel = createPieChartPanel()
    this.getContentPane.add(mainPanel)

  private class StatisticsPanel extends JPanel :

    def display(simulation: Simulation): Unit =
      dataset.clear()
      Statistics.numberOfCelestialBodiesForEachType(simulation.galaxy).filter(element => element._2 != 0).foreach(element => dataset.setValue(element._1.toString, element._2))

    override def paint(g: Graphics): Unit = ()

    override def getPreferredSize: Dimension =
      val d: Dimension = this.getParent.getSize()
      var newSize: Int = if d.width > d.height then d.height else d.width
      newSize = if newSize == 0 then 100 else newSize
      Dimension(newSize, newSize)

  end StatisticsPanel