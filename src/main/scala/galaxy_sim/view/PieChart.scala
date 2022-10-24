package galaxy_sim.view

import org.jfree.chart.title.Title
import org.jfree.chart.{ChartFactory, ChartPanel, JFreeChart}
import org.jfree.data.general.DefaultPieDataset

/**
 * Scala facade for the JFreeChart Pie Chart
 */
trait PieChart:
  /**
   * Wrap the chart to a Panel
   * @return the panel
   */
  def wrapToPanel: ChartPanel

  /**
   * The title of the chart
   * @return the title
   */
  def title: String

  /**
   * Set a value to the dataset
   * @param key key parameter
   * @param value value parameter
   */
  def setValue(key: String, value: Double): Unit

  /**
   * Delete all values from the dataset
   */
  def clearAllValues(): Unit

object PieChart:
  def apply(title: String): PieChart =
    new PieChartImpl(title)

  private class PieChartImpl(override val title: String) extends PieChart:
    private val dataset: DefaultPieDataset[String] = DefaultPieDataset[String]()
    private val chart: JFreeChart =
      ChartFactory.createPieChart(
        title,
        dataset,
        true,
        true,
        false,
      )

    override def wrapToPanel: ChartPanel =
      ChartPanel(chart)

    override def setValue(key: String, value: Double): Unit =
      dataset.setValue(key, value)

    override def clearAllValues(): Unit =
      dataset.clear()
