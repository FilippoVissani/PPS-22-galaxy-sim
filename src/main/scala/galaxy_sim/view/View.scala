package galaxy_sim.view

import akka.actor.typed.ActorRef
import galaxy_sim.actors.LoggerActions
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

  /**
   * Called to update the logger shown in the GUI
   * @param bodiesInvolved
   * @param description
   */
  def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit

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

  /**
   * Send the body info to the gui
   * @param celestialBody body information
   */
  def showBodyInfo(celestialBody: CelestialBody): Unit

  /**
   * Send a new name to add to the dropdown to the gui
   * @param bodyName the name to add
   */
  def updateNames(bodyName: String): Unit

  /**
   *
   * @param bodyName
   */
  def getBodyInfo(bodyName: String): Unit

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

    override def updateNames(bodyName: String): Unit = gui.updateNames(bodyName)

    override def getBodyInfo(bodyName: String): Unit = viewActor ! GetBodyInfo(bodyName)

    override def showBodyInfo(celestialBody: CelestialBody): Unit = gui.updateInfos(celestialBody)

    override def updateLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): Unit =
      val text = s"${bodiesInvolved._1.name} just $description"
      if bodiesInvolved._2.isDefined then gui.updateLogger(text + s" with ${bodiesInvolved._2.get.name}")
      else gui.updateLogger(text)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed

