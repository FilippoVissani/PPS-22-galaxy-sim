package galaxy_sim.view

import org.jfree.chart.ChartPanel

import java.awt.{Dimension, GridBagConstraints, GridBagLayout, Toolkit}
import javax.swing.{JLabel, JPanel, JTextArea}
import galaxy_sim.model.{CelestialBodyType, Simulation}
import galaxy_sim.utils.Percentage
import galaxy_sim.utils.Statistics
import scala.annotation.tailrec

class StatisticsPanel extends JPanel(GridBagLayout()):
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
