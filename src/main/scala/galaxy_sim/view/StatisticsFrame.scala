package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, Simulation}
import galaxy_sim.view.StatisticsFrame.StatisticsPanel
import galaxy_sim.view.View

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

  private class MainFrame extends JFrame :
    def createPieChartPanel(): ChartPanel =
      val dataset = DefaultPieDataset[String]()
      dataset.setValue("IPhone 5s", 5.0)
      dataset.setValue("SamSung Grand", 20.0)

      val chart = ChartFactory.createPieChart(
        "Mobile Sales", // chart title
        dataset, // data
        true, // include legend
        true,
        false);

      chart.setBackgroundPaint(new GradientPaint(new Point(0, 0),
        new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

      ChartPanel(chart)

    val mainPanel = createPieChartPanel()

    this.getContentPane.add(mainPanel)

  private class StatisticsPanel extends JPanel :
    var simulation: Option[Simulation] = Option.empty

    def display(simulation: Simulation): Unit =
      this.simulation = Option(simulation)
      //repaint()

    override def paint(g: Graphics): Unit =
      if simulation.isDefined then ???


    override def getPreferredSize: Dimension =
      val d: Dimension = this.getParent.getSize()
      var newSize: Int = if d.width > d.height then d.height else d.width
      newSize = if newSize == 0 then 100 else newSize
      Dimension(newSize, newSize)





  end StatisticsPanel