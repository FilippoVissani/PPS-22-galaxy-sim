package galaxy_sim

import galaxy_sim.controller.ControllerModule
import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule

object Main extends App
  with ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface:

  override val model = ModelImpl()
  override val view = GraphicalView(90, 90)
  override val controller = ControllerImpl()
