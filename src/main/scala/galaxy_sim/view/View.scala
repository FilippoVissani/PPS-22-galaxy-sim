package galaxy_sim.view

import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.{StartPressed, StopPressed, ViewActorCommand}
import galaxy_sim.model.{Boundary, CelestialBody, Simulation}

trait View:
  def display(simulation: Simulation): Unit
  def start(): Unit
  def stop(): Unit

object View:
  def apply(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int): View =
    ViewImpl(viewActor, windowWidth, windowHeight)

  private class ViewImpl(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int) extends View:
    val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)

    override def display(simulation: Simulation): Unit =
      gui.display(simulation)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed
