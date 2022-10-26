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
  def simulationGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit
  def newBody(celestialBody: CelestialBody): Unit
  def bodiesCollided(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String
  def bodyUpdated(celestialBody: CelestialBody): Unit
  def bodyDied(celestialBody: CelestialBody): Unit
  def showBodies: List[CelestialBody]
  def getBodiesNames: List[String]
  def bodyInfos(bodyName: String): Option[CelestialBody]

object ViewLogger:
  def apply(): ViewLogger =
    ViewLoggerImpl()

  private class ViewLoggerImpl() extends ViewLogger:
    var galaxyList: List[CelestialBody] = List.empty
    override def simulationGalaxy(galaxy: Map[CelestialBodyType, Set[CelestialBody]]): Unit =
      galaxyList = galaxy.values.filter(x => x.nonEmpty).flatten.toList

    override def bodiesCollided(bodiesInvolved: (CelestialBody, Option[CelestialBody]), description: LoggerActions): String = description match
      case Collided => s"${bodiesInvolved._1.name} ${Collided.toString.toUpperCase} with ${bodiesInvolved._2.get.name}\n"
      case Spawn => s"${bodiesInvolved._1.name.toUpperCase} just ${Spawn.toString.toUpperCase}\n"
      case Died => s"${bodiesInvolved._1.name.toUpperCase} just ${Died.toString.toUpperCase}\n"

    override def bodyDied(celestialBody: CelestialBody): Unit =
      galaxyList = galaxyList.filterNot(b => b.name == celestialBody.name)

    override def bodyUpdated(celestialBody: CelestialBody): Unit =
      galaxyList = galaxyList.filterNot(b => b.name == celestialBody.name).::(celestialBody)

    override def newBody(celestialBody: CelestialBody): Unit = ??? //todo

    override def showBodies: List[CelestialBody] = galaxyList

    override def getBodiesNames: List[String] = galaxyList.map(b => b.name)

    override def bodyInfos(bodyName: String): Option[CelestialBody] = galaxyList.find(b => b.name == bodyName)

