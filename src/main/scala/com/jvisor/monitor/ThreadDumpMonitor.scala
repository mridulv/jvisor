package com.jvisor.monitor

import com.jvisor.JProcess
import com.jvisor.integrations.S3Integration
import com.jvisor.model.{JEnv, ThreadDumpMonitorConf}

import java.io.File
import java.nio.file.Paths
import scala.sys.process.Process
import scala.util.{Random, Try}

class ThreadDumpMonitor(jProcess: JProcess,
                        monitorConf: ThreadDumpMonitorConf,
                        location: String) extends Monitor {

  val id: String = "thread-dumps"

  def monitorAndWatch(): Unit = {
    monitor()
    watch()
  }

  private def watch(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit =
        while (true) {
          monitor()
          deleteOlderFiles()
          Thread.sleep(monitorConf.frequencyInMins * 60 * 1000)
        }
    }
    new Thread(runnable).run()
  }

  private def deleteOlderFiles(): Unit = {
    val folder = new File(Paths.get(location, id).toAbsolutePath.toString)
    if (folder.exists()) {
      folder.listFiles().filter { e =>
        e.lastModified() <= System.currentTimeMillis() - monitorConf.ageInDays * 24 * 60 * 60 * 1000
      }.map(e => e.delete())
    }
  }

  private def monitor(): Unit = {
    val jstackBinary = JEnv.JAVA_HOME + "bin/jstack"
    val threadStackDumpName = s"${System.currentTimeMillis()/1000}-${Random.alphanumeric.take(10).mkString("")}.dump"
    val dumpLocation = Paths.get(location, threadStackDumpName)
    val command = Seq(jstackBinary, "-l", jProcess.pid().toString, ">", dumpLocation.toAbsolutePath.toString)
    Try(Process(command).!!).getOrElse(throw new RuntimeException("Bad Jstack"))
  }

}
