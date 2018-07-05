package buildresults.pages

import buildresults.components.HeaderNav
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import config.ConfigApi
import buildresults.diode.{AppCircuit, AppState, SetFileMetrics, SetPage}
import buildresults.router.AppRouter
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import tableaccess.{ConfigServer, FileMetrics}
import spa.client.elemental.buttons.Button
import spa.client.elemental.misc.Card
import spa.client.logger.log

object InstancesPage {

  case class Props (
                     proxy: ModelProxy[AppState],
                     ctl: RouterCtl[AppRouter.Page]
                   )

  case class PageState(
                        var isLoading: Boolean,
                        var fileMetrics: Seq[FileMetrics]
                      )

  class PageBackend($: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      log.info("InstancesPage rendering")
      val proxy = p.proxy()
      <.div(
        //    <.div("BEFORE"),
        //    ace,
        //    <.div("AFTER"),
        //    row,
        HeaderNav(HeaderNav.Props(p.proxy, p.ctl)),
        <.div("Nothing")
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Page")
    .renderBackend[PageBackend]
    .build

}

