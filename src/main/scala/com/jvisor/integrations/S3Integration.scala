package com.jvisor.integrations

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.jvisor.model.S3IntegrationConf

import java.io.File

class S3Integration(s3IntegrationConf: S3IntegrationConf) {

  import s3IntegrationConf._

  private val s3Client = AmazonS3ClientBuilder
    .standard()
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessId, accessSecret)))
    .withRegion(region)
    .build()

  def upload(id: String, file: File): Unit = {
    s3Client.putObject(bucket, s3IntegrationConf.location + "/" + id + "/" + file.getName, file)
  }

  def download(key: String): Unit = {
    s3Client.getObject(bucket, key)
  }
}
