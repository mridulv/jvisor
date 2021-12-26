package com.jvisor.model

case class JIntegrations(s3: Option[S3IntegrationConf], local: LocalIntegrationConf) {

  def location(): String = {
    local.location
  }

}
