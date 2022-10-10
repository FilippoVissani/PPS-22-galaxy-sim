package galaxy_sim.view

import galaxy_sim.model.CelestialBody
import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.ViewActorCommand
import galaxy_sim.actors.ViewActor.ViewActorCommand.*
import galaxy_sim.model.Boundary

trait View:
  def display(envelope: Envelope): Unit
  def start(): Unit
  def stop(): Unit

object View:
  def apply(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int): View =
    ViewImpl(viewActor, windowWidth, windowHeight)

  private class ViewImpl(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int) extends View:
    val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)

    override def display(envelope: Envelope): Unit =
      gui.display(envelope)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed
