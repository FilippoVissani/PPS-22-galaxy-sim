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

  //todo add rest of doc
  /**
   *
   * @param bodiesInvolved
   * @param description
   * @return
   */
  def bodiesLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String
  def bodyUpdated(celestialBody: CelestialBody): Unit
  def bodyDied(celestialBody: CelestialBody): Unit
  def getBodiesNames: List[String]
  def bodyInfos(bodyName: String): Option[CelestialBody]

object ViewLogger:
  def apply(): ViewLogger =
    ViewLoggerImpl()

  private class ViewLoggerImpl() extends ViewLogger:
    var galaxyList: List[CelestialBody] = List.empty

    override def simulationGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit = galaxyList = galaxy.values.filter(x => x.nonEmpty).flatten.toList //todo synchronized ?

    override def bodyDied(celestialBody: CelestialBody): Unit = galaxyList = galaxyList.filterNot(b => b.name == celestialBody.name) //todo synchronized ?

    override def bodyUpdated(celestialBody: CelestialBody): Unit = galaxyList = galaxyList.filterNot(b => b.name == celestialBody.name).::(celestialBody) //todo synchronized ?

    override def newBody(celestialBody: CelestialBody): Unit = galaxyList = galaxyList.::(celestialBody) //todo synchronized ?

    override def getBodiesNames: List[String] = galaxyList.map(b => b.name)

    override def bodyInfos(bodyName: String): Option[CelestialBody] = galaxyList.find(b => b.name == bodyName)

    override def bodiesLogger(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String =
      val res = s"${bodiesInvolved._1.name} just ${description.toString.toUpperCase}\n"
      if bodiesInvolved._2.isDefined then res + s" with ${bodiesInvolved._2.get.name}"
      res
      /*description match
      case Collided => s"${body.name} ${Collided.toString.toUpperCase}\n"
      case Spawn => s"${bodiesInvolved._1.name.toUpperCase} just ${Spawn.toString.toUpperCase}\n"
      case Died => s"${bodiesInvolved._1.name.toUpperCase} just ${Died.toString.toUpperCase}\n"*/