package galaxy_sim

import galaxy_sim.controller.ControllerModule
import galaxy_sim.model.ModelModule
import galaxy_sim.view.ViewModule

class MVCAssembler extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface :

  override val model: ModelModule.Model = ModelImpl()
  override val view: ViewModule.View = TextualView()
  override val controller: ControllerModule.Controller = ControllerImpl()
