package state

import tableaccess.FileMetrics

case class State(
                  page: Int = 0,
                  fileMetrics: Seq[FileMetrics]
                )
