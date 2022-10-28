package galaxy_sim.view

import galaxy_sim.model.CelestialBody
import galaxy_sim.utils.{LoggerActions, ViewLogger}

import java.awt.{BorderLayout, Dimension, Toolkit}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JComboBox, JPanel, JScrollBar, JScrollPane, JTextArea, ScrollPaneConstants}

class LoggerPanel(viewLogger: ViewLogger) extends JPanel :
  val textArea: JTextArea = JTextArea()
  textArea.setEditable(false)
  textArea.setPreferredSize(Dimension(
    40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
    40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
  val scrollPane: JScrollPane = JScrollPane(textArea)
  this.add(scrollPane, BorderLayout.CENTER)

  def display(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit =
    textArea.append(viewLogger.bodiesLogger(bodiesInvolved, description) + "\n")
