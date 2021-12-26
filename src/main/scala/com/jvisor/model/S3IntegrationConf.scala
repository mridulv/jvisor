package com.jvisor.model

case class S3IntegrationConf(bucket: String,
                             region: String,
                             accessId: String,
                             accessSecret: String,
                             location: String)

case class LocalIntegrationConf(location: String)