package buildresults.diode

import diode.Action
import tableaccess.FileMetrics

case class AppState (
                      page: Int = 0,
                      fileMetrics: Seq[FileMetrics] = Seq(),
                      isLoading: Boolean
                    )

case class AppModel(
                     state: AppState
                   )

case class SetFileMetrics(fileMetrics: Seq[FileMetrics]) extends Action

case class SetLoadingState() extends Action

case class ClearLoadingState() extends Action

case class SetPage(no: Int) extends Action


