package galaxy_sim.view

import org.jfree.chart.title.Title
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.general.DefaultPieDataset

/**
 * Scala facade for the JFreeChart Pie Chart
 */
trait PieChart:
  def wrapToPanel: ChartPanel
  def title: String
  def addValue(key: String, value: Double): Unit
  def clearAllValues(): Unit

object PieChart:
  def apply(title: String): PieChart =
    new PieChartImpl(title)

  private class PieChartImpl(override val title: String) extends PieChart:
    private val chart = createChart
    private val dataset: DefaultPieDataset[String] = DefaultPieDataset[String]()

    private def createChart: JFreeChart =
      ChartFactory.createPieChart(
        title,
        dataset,
        true,
        true,
        false,
      )

    override def wrapToPanel: ChartPanel =
      ChartPanel(chart)

    override def addValue(key: String, value: Double): Unit =
      dataset.setValue(key, value)

    override def clearAllValues(): Unit =
      dataset.clear()
