package galaxy_sim.view

import galaxy_sim.model.CelestialBody
import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.ViewActorCommand
import galaxy_sim.model.Boundary
import galaxy_sim.actors.ViewActor.StartPressed
import galaxy_sim.actors.ViewActor.StopPressed
import galaxy_sim.model.Simulation

trait View:
  def display(simulation: Simulation): Unit
  def start(): Unit
  def stop(): Unit

object View:
  def apply(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int): View =
    ViewImpl(viewActor, windowWidth, windowHeight)

  private class ViewImpl(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int) extends View:
    val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)
    val statisticsFrame: StatisticsFrame = StatisticsFrame(this, 20, 50)

    override def display(simulation: Simulation): Unit =
      gui.display(simulation)
      statisticsFrame.display(simulation)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed
