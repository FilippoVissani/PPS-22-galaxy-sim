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
    val mainPanel: JPanel = JPanel(BorderLayout())
    this.getContentPane.add(mainPanel)

  private class StatisticsPanel extends JPanel :
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
        simulation.get.galaxy.values.foreach(v =>
          v.foreach(e =>
            g2.drawString(e.name, scaleX(e.position.x), scaleY(e.position.y))
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

    private def pieChart(): Unit =
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

      chart.getPlot


  end StatisticsPanel