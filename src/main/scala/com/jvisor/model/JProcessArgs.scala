package com.jvisor.model

case class JProcessArgs(name: String,
                        jarExecution: Option[JarExecution] = None,
                        classpathExecution: Option[ClasspathExecution] = None,
                        jEnv: JEnv,
                        monitor: Option[JMonitorConf]) {

  def execution: Execution = {
    jarExecution match {
      case Some(execution) => execution
      case None => classpathExecution match {
        case Some(execution) => execution
        case None => throw new RuntimeException("Invalid State of the JProcess")
      }
    }
  }

}

