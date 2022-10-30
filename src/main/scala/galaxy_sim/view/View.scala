package galaxy_sim.view

import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.{StartPressed, StopPressed, ViewActorCommand}
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Simulation}

/** Defines view of the simulation. */
trait View:

  /** Updates the view.
   *
   *  Called from ViewActor.
   *
   *  @param simulation current state of the simulation
   */
  def display(simulation: Simulation): Unit

  /** Called when start button is pressed.
   *
   *  Called from the GUI.
   */
  def start(): Unit

  /** Called when stop button is pressed.
   *
   *  Called from the GUI.
   */
  def stop(): Unit

/** Factory for View. */
object View:

  /** Creates a View.
   *
   *  @param viewActor reference of ViewActor
   *  @param windowWidth window width in percent
   *  @param windowHeight window height in percent
   */
  def apply(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int): View =
    ViewImpl(viewActor, windowWidth, windowHeight)

  private class ViewImpl(viewActor: ActorRef[ViewActorCommand], windowWidth: Int, windowHeight: Int) extends View:
    val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight)

    override def display(simulation: Simulation): Unit = gui.display(simulation)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed

