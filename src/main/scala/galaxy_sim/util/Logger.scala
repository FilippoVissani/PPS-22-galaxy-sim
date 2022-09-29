package galaxy_sim.util

import java.io.{File, FileNotFoundException, FileOutputStream, PrintWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Use the Logger to print on file
 */
trait Logger:
  /**
   * Call this method when starting the simulation to log the date and time the simulation started
   */
  def logSimulationStarted(): Unit

  /**
   * Call this method when pausing the simulation to log the date and time the simulation paused
   */
  def logSimulationPaused(): Unit

  /**
   * Call this method when terminating the simulation to log the date and time the simulation terminated
   */
  def logSimulationTerminated(): Unit

object Logger:

  def apply(): Logger = LoggerImpl()

  private class LoggerImpl extends Logger:

    override def logSimulationStarted(): Unit =
    log("SIMULATION STARTED " + dateTimeFormatted())

    override def logSimulationPaused(): Unit =
    log("SIMULATION PAUSED " + dateTimeFormatted())

    override def logSimulationTerminated(): Unit =
    log("SIMULATION TERMINATED " + dateTimeFormatted())
    log("")

    private def dateTimeFormatted(): String =
      LocalDateTime.now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

    private def log(text: String): Unit =
      val out = new PrintWriter(new FileOutputStream(new File("simulation.log"), true))
      out.println(text)
      println(text)
      out.close()