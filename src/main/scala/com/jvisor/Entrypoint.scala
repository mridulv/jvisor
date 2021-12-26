package com.jvisor

object Entrypoint {
  val jVisor = new JVisor()

  def main(args: Array[String]): Unit = {
    jVisor.start()
  }

}
