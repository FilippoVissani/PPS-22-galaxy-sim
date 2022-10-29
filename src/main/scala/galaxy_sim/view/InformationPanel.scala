package galaxy_sim.view

import galaxy_sim.model.CelestialBody

import java.awt.{BorderLayout, Dimension, Toolkit}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JComboBox, JPanel, JTextArea}

class InformationPanel(view: View) extends JPanel, ActionListener :
  val dropdown: JComboBox[String] = JComboBox()
  val textArea: JTextArea = JTextArea("")
  dropdown.addActionListener(this)
  dropdown.setEditable(false)
  textArea.setEditable(false)
  textArea.setPreferredSize(Dimension(
    40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
    40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
  textArea.setLineWrap(true)
  this.add(dropdown, BorderLayout.NORTH)
  this.add(textArea, BorderLayout.CENTER)

  override def actionPerformed(e: ActionEvent): Unit =
    val cb: JComboBox[String] = e.getSource.asInstanceOf[JComboBox[String]]
    view.getBodyInfo(cb.getSelectedItem.asInstanceOf[String])
//    val body = viewLogger.bodyInfos(cb.getSelectedItem.asInstanceOf[String])
//    textArea.setText(body)

  def setDropdown(name: String): Unit =
//    dropdown.removeAllItems()
    dropdown.addItem(name)

  def display(bodyInfo: CelestialBody): Unit =
    textArea.setText(s"Name: ${bodyInfo.name.toUpperCase}\n" +
      s"Position: (${bodyInfo.position.x.toString.substring(0, 4)} , ${bodyInfo.position.y.toString.substring(0, 4)})\n" +
      s"Speed: ${bodyInfo.speedVector.y / 1000} Km/s\n" +
      s"Mass = ${bodyInfo.mass} kg\n" +
      s"Temperature = ${bodyInfo.temperature.toString.substring(0, 4)} °C\n\n")
