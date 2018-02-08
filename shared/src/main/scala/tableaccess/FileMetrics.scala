package tableaccess

case class FileMetrics(
                        buildFail: Long,
                        buildSuccess: Long,
                        downloads: Long,
                        filename: String,
                        lastBuildAttempt: String,
                        lastResult: String,
                        lastError: String,
                        lastBatchID: String,
                        lastRequestID: Long,
                        packageName: String,
                        revision: Long,
                        timestamp: String
                      )

