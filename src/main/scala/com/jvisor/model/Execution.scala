package com.jvisor.model

trait Execution {
  def mainClass: Option[String]
}

case class JarExecution(jar: String,
                        mainClass: Option[String] = None) extends Execution

case class ClasspathExecution(classPath: String,
                              mainClass: Option[String]) extends Execution
