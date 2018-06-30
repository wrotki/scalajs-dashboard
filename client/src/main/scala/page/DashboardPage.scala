package page

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

object DashboardPage {

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
      log.info("Dashboard rendering")
      val proxy = p.proxy()
      <.div(
        //    <.div("BEFORE"),
        //    ace,
        //    <.div("AFTER"),
        //    row,
        Card.component(Card.props(""))(
          // ,onClick = Callback { $.setState(State(fileMetrics))})
          Button.component(Button.props(
            size = "lg",
            `type`="primary",
            onClick =
              Callback { AppCircuit.dispatch(SetPage(0)) } >> Callback.log("Build clicked")
          ))(<.div("Build")),
          Button.component(Button.props(
            size = "lg",
            `type`="primary",
            onClick =
              Callback { AppCircuit.dispatch(SetPage(0)) } >> Callback.log("Indices clicked")
          ))(<.div("Indices")),
          Button.component(Button.props(size = "lg", `type`="primary"))(<.div("About"))
        ),
        if (proxy.page == 0) { Table.Component(Table.Props(p.proxy, p.ctl)) } else { <.div("Nothing")}
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Page")
      .renderBackend[PageBackend]
      .build

}

