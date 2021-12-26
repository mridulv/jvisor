package com.jvisor.monitor

import com.jvisor.JProcess
import com.jvisor.model.HeapDumpMonitorConf

import java.io.File
import java.nio.file.Paths

class HeapDumpMonitor(jProcess: JProcess,
                      monitorConf: HeapDumpMonitorConf,
                      location: String) extends Monitor {

  val id: String = "heap-dumps"

  def monitorAndWatch(): Unit = {
    watch()
  }

  private def watch(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit =
        while (true) {
          deleteOlderFiles()
          Thread.sleep(60 * 1000)
        }
    }
    new Thread(runnable).run()
  }

  private def deleteOlderFiles(): Unit = {
    val folder = new File(Paths.get(location, id).toAbsolutePath.toString)
    if (folder.exists()) {
      val numFilesToBeDeleted = if (folder.listFiles().length - monitorConf.numHeapDumps > 0) {
        folder.listFiles().length - monitorConf.numHeapDumps
      } else {
        0
      }
      folder.listFiles().sortBy(_.lastModified()).take(numFilesToBeDeleted).map(_.delete())
    }
  }
}
