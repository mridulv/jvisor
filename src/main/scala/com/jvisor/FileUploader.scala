package com.jvisor

import com.jvisor.integrations.S3Integration

import java.io.File
import scala.jdk.CollectionConverters._
import java.nio.file.FileSystems
import java.nio.file.WatchService
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.Path
import java.nio.file.Paths

class FileUploader(location: String, s3Integration: S3Integration) {

  def start(): Unit = {
    val watchService: WatchService = FileSystems.getDefault.newWatchService

    val path: Path = Paths.get(location)
    path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE)

    var poll = true

    while (poll) {
      val key = watchService.take
      key.pollEvents().asScala.map{ event =>
        val path = event.asInstanceOf[WatchEvent[Path]]
        val file = new File(path.toString)
        s3Integration.upload(file.getParentFile.getName, file)
        file.delete()
      }
      poll = key.reset()
    }
  }

}
