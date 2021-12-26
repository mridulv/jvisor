package com.jvisor.model

import java.nio.file.{Path, Paths}

case class JEnv(systemArguements: Option[Seq[String]],
                systemEnviornment: Option[Map[String, String]],
                enviornment: Option[Map[String, String]],
                arguements: Option[Seq[String]]) {


  def processArgs(execution: Execution,
                  location: String,
                  enableHeapDump: Boolean,
                  enableGc: Boolean): Seq[String] = {
    val javaHome = Seq(JEnv.JAVA_HOME)
    val enviornmentArgs =  systemEnviornment.getOrElse(Map.empty).map { case (key, value) =>
      s"-D$key=$value"
    }.toSeq
    val executionArgs = execution match {
      case jarExecution: JarExecution => Seq("-jar", jarExecution.jar)
      case classpathExecution: ClasspathExecution => Seq("-cp", classpathExecution.classPath)
    }

    val systemArgs = allSystemArgs(systemArguements.getOrElse(Seq.empty), location, enableGc, enableHeapDump)
    javaHome ++ systemArgs ++ heapDumpArgs(location) ++ enviornmentArgs ++ executionArgs ++ execution.mainClass.map(e => Seq(e)).getOrElse(Seq.empty)
  }

  private def allSystemArgs(systemArguements: Seq[String],
                            location: String,
                            enableGC: Boolean,
                            enableHeapDump: Boolean): Seq[String] = {
    val systemArgs = systemArguements
    val gcArguments = if (enableGC) {
      gcArgs(location)
    } else {
      Seq.empty
    }
    val heapDumpArguements = if (enableHeapDump) {
      heapDumpArgs(location)
    } else {
      Seq.empty
    }
    systemArgs ++ gcArguments ++ heapDumpArguements
  }

  private def heapDumpArgs(location: String): Seq[String] = {
    Seq("-XX:+HeapDumpOnOutOfMemoryError", "-XX:+ExitOnOutOfMemoryError", s"-XX:HeapDumpPath=${appendPath(location, "gc_dumps")}")
  }

  private def gcArgs(location: String): Seq[String] = {
    Seq("-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-XX:+PrintGCDateStamps", "-XX:+PrintGCCause", s"-Xloggc:${appendPath(location, "heap_dumps")}")
  }

  private def appendPath(parentPath: String, suffixPath: String): String = {
    val path = Paths.get(parentPath, suffixPath)
    path.toAbsolutePath.toString
  }

  def heapDumpEnabled(): Boolean = {
    systemArguements.exists { e =>
      e.contains("-XX:+HeapDumpOnOutOfMemoryError") && e.exists(f => f.contains("-XX:+HeapDumpPath="))
    }
  }

}

object JEnv {
  val JAVA_HOME: String = sys.env.getOrElse("JAVA_HOME", "java")
}