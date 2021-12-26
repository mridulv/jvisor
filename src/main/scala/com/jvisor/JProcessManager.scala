package com.jvisor

import com.jvisor.integrations.S3Integration
import com.jvisor.model.JVisorConf
import com.jvisor.monitor.{HeapDumpMonitor, ThreadDumpMonitor}

class JProcessManager(jVisorConf: JVisorConf) {
  import jVisorConf._

  def startProcess(): JProcess = {
    val diskLocation = jVisorConf.integrations.location()
    val processArgs = process.jEnv.processArgs(
      process.execution,
      diskLocation,
      process.monitor.flatMap(_.threadDumps).isDefined,
      process.monitor.flatMap(_.heapDumps).isDefined
    )
    val jProcess = new JProcess(process.name)
    jProcess.startAndWatch(processArgs, process.jEnv.enviornment.getOrElse(Map.empty))

    val monitors = process.monitor match {
      case Some(monitorConf) =>
        Seq(
          monitorConf.heapDumps.map(e => new HeapDumpMonitor(jProcess, e, diskLocation)),
          monitorConf.threadDumps.map(e => new ThreadDumpMonitor(jProcess, e, diskLocation))
        ).flatten
      case None => Seq.empty
    }

    monitors.foreach(_.monitorAndWatch())

    jVisorConf.integrations.s3.map(conf => new FileUploader(diskLocation, new S3Integration(conf)))
    jProcess
  }
}
