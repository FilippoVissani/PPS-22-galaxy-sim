package galaxy_sim.view

import java.awt.Dimension
import javax.swing.JFrame

trait SimulationFrame extends JFrame

object SimulationFrame:
  def apply(size: Dimension): SimulationFrame = SimulationFrameImpl(size)
  
  private class SimulationFrameImpl(size: Dimension) extends SimulationFrame:
    setSize(size)
    setVisible(true)
