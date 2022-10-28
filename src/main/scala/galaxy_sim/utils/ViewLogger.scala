package galaxy_sim.utils

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, Behavior}
import galaxy_sim.model.{CelestialBody, CelestialBodyType}
import galaxy_sim.utils.LoggerActions.*
import galaxy_sim.view.{SwingGUI, View}

import scala.::

enum LoggerActions:
  case Spawn
  case Collided
  case Died

trait ViewLogger:
  /**
   * Set the list of Bodies inside the galaxy
   * @param galaxy a map containing the CelestialBodyType and a set of CelestialBody
   */
  def simulationGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit

  /**
   * Add a new CelestialBody to the actual galaxy
   * @param celestialBody the new body for the simulation
   */
  def newBody(celestialBody: CelestialBody): Unit

  /**
   *
   * @param bodiesInvolved
   * @param description
   * @return
   */
  def bodiesLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String

  /**
   * Updates the info of a certain body
   * @param celestialBody the body to update
   */
  def bodyUpdated(celestialBody: CelestialBody): Unit

  /**
   * Update the galaxy when a body dies
   * @param celestialBody the body that died
   */
  def bodyDied(celestialBody: CelestialBody): Unit

  /**
   * Gets the names of all the bodies
   * @return a list containing all bodies' names
   */
  def getBodiesNames: List[String]

  /**
   * Get the info of a specific body
   * @param bodyName the name of the body
   * @return
   */
  def bodyInfos(bodyName: String): String

object ViewLogger:
  def apply(): ViewLogger = ViewLoggerImpl()

  private class ViewLoggerImpl() extends ViewLogger:
    var galaxyList: List[CelestialBody] = List.empty

    override def simulationGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit = synchronized{ galaxyList = galaxy.values.filter(x => x.nonEmpty).flatten.toList }

    override def bodyDied(celestialBody: CelestialBody): Unit = galaxyList = synchronized{ galaxyList.filterNot(b => b.name == celestialBody.name) }

    override def bodyUpdated(celestialBody: CelestialBody): Unit = galaxyList = synchronized{ galaxyList.filterNot(b => b.name == celestialBody.name).::(celestialBody) }

    override def newBody(celestialBody: CelestialBody): Unit = synchronized { galaxyList = galaxyList.::(celestialBody) }

    override def getBodiesNames: List[String] = galaxyList.map(b => b.name)

    override def bodyInfos(bodyName: String): String =
      val body = galaxyList.find(b => b.name == bodyName)
      val res = ""
      if body.isDefined then
        res + s"Name: ${body.get.name.toUpperCase}\n" +
          s"Position: (${body.get.position.x.toString.substring(0, 4)} , ${body.get.position.y.toString.substring(0, 4)})\n" +
          s"Speed: ${body.get.speedVector.y/1000} Km/s\n" +
          s"Mass = ${body.get.mass} kg\n" +
          s"Temperature = ${body.get.temperature.toString.substring(0,4)} °C\n\n"
      else res + "Not found"

    override def bodiesLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String =
      val res = s"${bodiesInvolved._1.name} just ${description.toString.toLowerCase}"
      if bodiesInvolved._2.isDefined then res + s" with ${bodiesInvolved._2.get.name}" else res