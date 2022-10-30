package galaxy_sim.view

import galaxy_sim.model.{CelestialBody, CelestialBodyType, Simulation}
import physics.dynamics.PhysicsFormulas.solarMass
import java.awt.{BorderLayout, Dimension, Toolkit}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JComboBox, JPanel, JTextArea}

class InformationPanel(view: View) extends JPanel, ActionListener:
  private var galaxy: Option[Map[CelestialBodyType, Set[CelestialBody]]] = Option.empty
  val dropdown: JComboBox[String] = JComboBox()
  val textArea: JTextArea = JTextArea("")
  dropdown.addActionListener(this)
  dropdown.setEditable(false)
  textArea.setEditable(false)
  textArea.setPreferredSize(Dimension(
    40 * Toolkit.getDefaultToolkit.getScreenSize.width / 100,
    40 * Toolkit.getDefaultToolkit.getScreenSize.height / 100))
  textArea.setLineWrap(true)
  this.add(dropdown, BorderLayout.SOUTH)
  this.add(textArea, BorderLayout.CENTER)

  override def actionPerformed(e: ActionEvent): Unit =
    val cb: JComboBox[String] = e.getSource.asInstanceOf[JComboBox[String]]
    galaxy.foreach(x =>
      x.values
        .flatten
        .find(y => y.name == cb.getSelectedItem.asInstanceOf[String])
        .foreach(z =>
          textArea.setText(s"Name: ${z.name.toUpperCase}\n" +
            s"Position: (${BigDecimal(z.position.x).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble} , ${BigDecimal(z.position.y).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble})\n" +
            s"Speed: ${BigDecimal(z.speedVector.y / 1000).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble} Km/s\n" +
            s"Mass = ${BigDecimal(z.mass / solarMass).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble}} Solar Mass\n" +
            s"Temperature = ${BigDecimal(z.temperature).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble}} °C\n\n")
        )
    )

  def updateData(data: Map[CelestialBodyType, Set[CelestialBody]]): Unit =
    if this.galaxy.isEmpty then data.values.flatten.foreach(x => dropdown.addItem(x.name))
    this.galaxy = Option(data)
