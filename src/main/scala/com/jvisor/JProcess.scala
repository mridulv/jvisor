package com.jvisor

import com.jvisor.model.JProcessArgs

import scala.sys.process.Process

class JProcess(name: String) {

  @volatile var processArgs: Seq[String] = null
  @volatile var envs: Map[String, String] = null
  @volatile var process: Process = null

  def pid(): Long = {
    pid(process)
  }

  def startAndWatch(processArgs: Seq[String], envs: Map[String, String]): Unit = {
    println(s"process Args are: ${processArgs} and command is: ${processArgs.mkString(" ")}")
    this.processArgs = processArgs
    this.envs = envs
    start()
    watch()
  }

  def end(): Unit = {
    process.destroy()
  }

  private def start(): Unit = {
    process = Process(processArgs, cwd = None, extraEnv = envs.toSeq:_*).run
    pid(process)
  }

  private def watch(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit = {
        while (process.isAlive()) {
          Thread.sleep(10000)
        }
        start()
      }
    }
    new Thread(runnable).run()
  }

  private def getPrivateLongField(proc: Any, name: String): Long = {
    val privateField = proc.getClass.getDeclaredField(name)
    privateField.synchronized {
      privateField.setAccessible(true)
      try {
        privateField.getLong(proc)
      } finally {
        privateField.setAccessible(false)
      }
    }
  }

  private def pidJava(proc: Any): Long = {
    proc match {
      case unixProc: Any
        if unixProc.getClass.getName == "java.lang.UNIXProcess" => {
        getPrivateLongField(unixProc, "pid")
      }
      case _ => throw new RuntimeException(
        "Cannot get PID of a " + proc.getClass.getName)
    }
  }

  private def pid(p: Process): Long = {
    val procField = p.getClass.getDeclaredField("p")
    procField.synchronized {
      procField.setAccessible(true)
      val proc = procField.get(p)
      try {
        pidJava(proc)
      } finally {
        procField.setAccessible(false)
      }
    }
  }

}
