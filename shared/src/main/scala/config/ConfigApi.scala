package config

import tableaccess.FileMetrics

trait ConfigApi {
  def getConfig(service: String, deployment: String): String

  def getFileMetrics(): Seq[FileMetrics]
}
