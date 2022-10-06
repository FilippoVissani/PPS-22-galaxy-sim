package galaxy_sim.utils

import galaxy_sim.model.CelestialBody

import java.io.{File, FileNotFoundException, FileOutputStream, PrintWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Use the Logger to print on file
 */
trait Logger:
  /**
   * Log the date and time the simulation started
   */
  def logSimulationStarted(): Unit

  /**
   * Log the date and time the simulation paused
   */
  def logSimulationPaused(): Unit

  /**
   * Log the date and time the simulation terminated
   */
  def logSimulationTerminated(): Unit

  /**
   * Log a collision between two entities
   * @param entity1 the entity that collided with entity2
   * @param entity2 the entity that collided with entity1
   */
  def logCollision(entity1: CelestialBody, entity2: CelestialBody): Unit

//todo take the path of the file in the apply
object Logger:

  def apply(): Logger = LoggerImpl()

  private class LoggerImpl extends Logger:

    override def logSimulationStarted(): Unit =
      log("SIMULATION STARTED " + dateTime())

    override def logSimulationPaused(): Unit =
      log("SIMULATION PAUSED " + dateTime())

    override def logSimulationTerminated(): Unit =
      log("SIMULATION TERMINATED " + dateTime())
      log("")

    override def logCollision(entity1: CelestialBody, entity2: CelestialBody): Unit =
      log(s"COLLISION DETECTED BETWEEN $entity1 AND $entity2 AT ${dateTime()}")

    private def dateTime(): String =
      LocalDateTime.now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

    private def log(text: String): Unit =
      val out = new PrintWriter(new FileOutputStream(new File("simulation.log"), true))
      out.println(text)
      println(text)
      out.close()