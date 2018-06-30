package buildresults.models

import tableaccess.FileMetrics

case class PageContent(
                        text: String = "PageContent",
                        fileMetrics: Seq[FileMetrics] = Seq()
                      )
