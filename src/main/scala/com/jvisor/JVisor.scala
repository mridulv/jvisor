package com.jvisor

import com.jvisor.model.{JProcessArgs, JVisorConf}
import org.yaml.snakeyaml.Yaml

import java.io.File
import net.jcazevedo.moultingyaml._
import org.apache.commons.io.FileUtils

import java.nio.charset.StandardCharsets

class JVisor {

  val defaultLocation = "jvisor.conf"
  val yaml = new Yaml()

  def start(): Unit = {
    val fileContent = FileUtils.readFileToString(new File("/Users/mridul/git/jvisor/src/main/scala/com/jvisor/jvisor.conf"), StandardCharsets.UTF_8)
    val jVisorConf = fileContent.parseYaml.convertTo[JVisorConf]
    new JProcessManager(jVisorConf).startProcess()
  }

}
