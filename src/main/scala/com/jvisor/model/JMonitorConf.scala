package com.jvisor.model

case class JMonitorConf(threadDumps: Option[ThreadDumpMonitorConf],
                        heapDumps: Option[HeapDumpMonitorConf])

case class ThreadDumpMonitorConf(frequencyInMins: Int = 10,
                                 ageInDays: Int = 5)

case class HeapDumpMonitorConf(numHeapDumps: Int = 3)

case class GCMonitorConf(frequencyInSecs: Int = 10)

case class ProcessMonitorConf(metrics: Seq[String], frequencyInSecs: Int = 10)
