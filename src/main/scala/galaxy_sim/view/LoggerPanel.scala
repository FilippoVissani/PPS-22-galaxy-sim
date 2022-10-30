package galaxy_sim.view

import galaxy_sim.model.CelestialBody

import java.awt.{BorderLayout, Dimension, Toolkit}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JComboBox, JPanel, JScrollBar, JScrollPane, JTextArea, ScrollPaneConstants}

class LoggerPanel() extends JPanel :
  val textArea: JTextArea = JTextArea()
  textArea.setEditable(false)
  val scrollPane: JScrollPane = JScrollPane(textArea)
  scrollPane.setPreferredSize(Dimension(
    40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
    40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
  this.add(scrollPane, BorderLayout.CENTER)

  def display(text: String): Unit =
    textArea.setText(text)
