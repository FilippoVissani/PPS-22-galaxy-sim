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

  def apply(windowPercentualWidth: Int, windowPercentualHeight: Int): StatisticsFrame =
    StatisticsFrameImpl(windowPercentualWidth: Int, windowPercentualHeight: Int)

  val pieChart: PieChart = PieChart("Celestial body types")

  private class StatisticsFrameImpl(windowPercentualWidth: Int,
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

  private class MainFrame extends JFrame :
    val mainPanel: ChartPanel = pieChart.wrapToPanel
    this.getContentPane.add(mainPanel)

  private class StatisticsPanel extends JPanel :

    def display(simulation: Simulation): Unit =
      pieChart.clearAllValues()
      Statistics.numberOfCelestialBodiesForEachType(simulation.galaxy).filter(element => element._2 != 0).foreach(element => pieChart.addValue(element._1.toString, element._2))

    override def paint(g: Graphics): Unit = ()

    override def getPreferredSize: Dimension =
      val d: Dimension = this.getParent.getSize()
      var newSize: Int = if d.width > d.height then d.height else d.width
      newSize = if newSize == 0 then 100 else newSize
      Dimension(newSize, newSize)

  end StatisticsPanel