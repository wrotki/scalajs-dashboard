package buildresults.components

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import japgolly.scalajs.react.{Callback, _}
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._

import autowire._

import boopickle.Default._

import diode.react.ModelProxy

import spa.client.logger.log

import buildresults.diode._
import buildresults.diode.AppState
import buildresults.diode.AppCircuit.connect
import buildresults.config.Config
import buildresults.router.AppRouter.Page
import page.DashboardPage
import config.ConfigApi
import tableaccess.{ConfigServer, FileMetrics}


object Layout {
  val connection = connect(_.state)

  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[Page],
                    resolution: Resolution[Page]
                  )

  class Backend(bs: BackendScope[Props, Unit]) {

    def fetchFileMetrics = CallbackTo[Future[Seq[FileMetrics]]] {
      log.info("FileMetrics requested")
      AppCircuit.dispatch(SetLoadingState())
      ConfigServer[ConfigApi].getFileMetrics().call()
    }

    def dispatchFileMetrics(fileMetrics: Future[Seq[FileMetrics]]) = Callback {
      fileMetrics map { fm =>
        log.info("FileMetrics received")
        AppCircuit.dispatch(SetFileMetrics(fm))
        AppCircuit.dispatch(ClearLoadingState())
      }
    }

    def mounted: Callback = fetchFileMetrics >>= dispatchFileMetrics

    def render(props: Props): VdomElement = {
      <.div(
        <.div(^.cls := "container", props.resolution.render()),
        connection(proxy => LoadingIndicator(LoadingIndicator.Props(proxy)))
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Layout")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
