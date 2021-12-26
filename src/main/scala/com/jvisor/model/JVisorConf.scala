package com.jvisor.model

import net.jcazevedo.moultingyaml.DefaultYamlProtocol

case class JVisorConf(process: JProcessArgs, integrations: JIntegrations)

object JVisorConf extends DefaultYamlProtocol {
  implicit val JarExecutionFormatter = yamlFormat2(JarExecution.apply)
  implicit val ClasspathExecutionFormatter = yamlFormat2(ClasspathExecution.apply)
  implicit val JEnvFormatter = yamlFormat4(JEnv.apply)

  implicit val ThreadDumpConfFormatter = yamlFormat2(ThreadDumpMonitorConf.apply)
  implicit val HeapDumpConfFormatter = yamlFormat1(HeapDumpMonitorConf.apply)
  implicit val GCConfFormatter = yamlFormat1(GCMonitorConf.apply)
  implicit val JMonitorConfFormatter = yamlFormat2(JMonitorConf.apply)

  implicit val JProcessFormatter = yamlFormat5(JProcessArgs.apply)
  implicit val S3ConfFormatter = yamlFormat5(S3IntegrationConf.apply)
  implicit val LocalIntegrationConfFormatter = yamlFormat1(LocalIntegrationConf.apply)
  implicit val JIntegrationsFormatter = yamlFormat2(JIntegrations.apply)
  implicit val JVisorConfFormatter = yamlFormat2(JVisorConf.apply)
}