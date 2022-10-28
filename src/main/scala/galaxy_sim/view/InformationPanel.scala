package galaxy_sim.view

import galaxy_sim.utils.ViewLogger

import java.awt.{BorderLayout, Dimension, Toolkit}
import java.awt.event.{ActionListener, ActionEvent}
import javax.swing.{JComboBox, JPanel, JTextArea}

class InformationPanel(viewLogger: ViewLogger) extends JPanel, ActionListener :
  var data: Option[List[String]] = Option.empty
  val dropdown: JComboBox[String] = JComboBox()
  val textArea: JTextArea = JTextArea()
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
    val body = viewLogger.bodyInfos(cb.getSelectedItem.asInstanceOf[String])
    textArea.setText(body)

  def display(): Unit =
    this.data = Some(viewLogger.getBodiesNames)
    textArea.setText("")
    if data.isDefined then
      dropdown.removeAllItems()
      data.get.foreach(d => dropdown.addItem(d))