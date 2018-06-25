package buildresults.diode

import diode._
import diode.react.ReactConnector
import buildresults.models.{PageContent}

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      pageContent = None: Option[PageContent],
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
    case GetPageContent(pageContent) => updated(value.copy(pageContent = pageContent))
  }
}

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetLoadingState() => updated(value.copy(isLoading = true))
    case ClearLoadingState() => updated(value.copy(isLoading = false))
    case GetPageContent(pageContent) => updated(value.copy(pageContent = pageContent))
  }
}
