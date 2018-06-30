package buildresults.diode

import diode.Action
import buildresults.models.PageContent
import tableaccess.FileMetrics

case class AppState (
                      pageContent: Option[PageContent],
                      page: Int = 0,
                      fileMetrics: Seq[FileMetrics] = Seq(),
                      isLoading: Boolean
                    )

case class AppModel(
                     state: AppState
                   )


case class GetPageContent(content: Option[PageContent]) extends Action

case class SetFileMetrics(fileMetrics: Seq[FileMetrics]) extends Action

case class SetLoadingState() extends Action

case class ClearLoadingState() extends Action

case class SetPage(no: Int) extends Action


