package buildresults.diode

import diode._
import diode.react.ReactConnector

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      page = 0,
      fileMetrics = Seq(),
      isLoading = false
    )
  )

  override val actionHandler = composeHandlers(
    new PageHandler(zoomTo(_.state)),
    new AppHandler(zoomTo(_.state))
  )
}

class PageHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetPage(page) => updated(value.copy(page = page))
  }
}

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetLoadingState() => updated(value.copy(isLoading = true))
    case ClearLoadingState() => updated(value.copy(isLoading = false))
    case SetFileMetrics(fileMetrics) => updated(value.copy(fileMetrics = fileMetrics))
    case SetPage(page) => updated(value.copy(page = page))
  }
}
