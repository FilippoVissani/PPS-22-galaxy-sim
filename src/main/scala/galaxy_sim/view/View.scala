package galaxy_sim.view

import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.{StartPressed, StopPressed, ViewActorCommand}
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Simulation}

/** Defines view of the simulation. */
trait View:

  /** Updates the simulation view.
   *
   *  Called from ViewActor.
   *
   *  @param simulation current state of the simulation
   */
  def displaySimulation(simulation: Simulation): Unit

  /** Updates the log view.
   *
   * Called from ViewActor.
   *
   * @param events events to display
   */
  def displayEvents(events: List[String]): Unit

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

    override def displaySimulation(simulation: Simulation): Unit = gui.displaySimulation(simulation)

    override def displayEvents(events: List[String]): Unit = gui.displayEvents(events.mkString("\n"))

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed

