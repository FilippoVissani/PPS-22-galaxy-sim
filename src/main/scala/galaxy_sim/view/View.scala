package galaxy_sim.view

import akka.actor.typed.ActorRef
import galaxy_sim.actors.ViewActor.{StartPressed, StopPressed, ViewActorCommand}
import galaxy_sim.model.{Boundary, CelestialBody, CelestialBodyType, Simulation}
import galaxy_sim.utils.{LoggerActions, ViewLogger}

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
   * Called to update the infos of the bodies
   */
  def updateInfos(): Unit //tmpCelestialBodies: Map[CelestialBodyType, Set[CelestialBody]]

  /**
   * Called to update the logger shown in the GUI
   * @param bodiesInvolved
   * @param description
   */
  def sendLogger(body: CelestialBody, description: LoggerActions): Unit

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
   * Called to set the entities' infos of the galaxy
   * @param galaxy
   */
  def setGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit

  /**
   *
   * @param celestialBody
   */ //todo
  def updateBody(celestialBody: CelestialBody): Unit
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
    val viewLogger: ViewLogger = ViewLogger()
    val gui: SwingGUI = SwingGUI(this, windowWidth, windowHeight, viewLogger)

    override def display(simulation: Simulation): Unit = gui.display(simulation)

    override def setGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit = viewLogger.simulationGalaxy(galaxy)

    override def updateInfos(): Unit = gui.updateInfos()

    override def updateBody(celestialBody: CelestialBody): Unit = viewLogger.bodyUpdated(celestialBody)

    override def sendLogger(body: CelestialBody, description: LoggerActions): Unit =
      gui.updateLogger(body, description)
    //      viewLogger.bodiesCollided(bodiesInvolved, description)

    override def start(): Unit = viewActor ! StartPressed

    override def stop(): Unit = viewActor ! StopPressed

