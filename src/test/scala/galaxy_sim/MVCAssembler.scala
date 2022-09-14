package galaxy_sim

import galaxy_sim.controller.ControllerModule
import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule

class MVCAssembler extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface :

  override val model: ModelModule.Model = Model()
  override val view: ViewModule.View = View(90, 90)
  override val controller: ControllerModule.Controller = Controller()
