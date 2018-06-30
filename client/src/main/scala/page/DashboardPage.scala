package page

import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import config.ConfigApi
import buildresults.diode.AppState
import buildresults.models.PageContent
import buildresults.router.AppRouter
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import state.State
import tableaccess.{ConfigServer, FileMetrics}

import spa.client.elemental.buttons.Button
import spa.client.elemental.misc.Card

object DashboardPage {

  case class Props (
                     proxy: ModelProxy[AppState],
                     ctl: RouterCtl[AppRouter.Page]
                   )

  case class PageState(
                    var isLoading: Boolean,
                    var fileMetrics: Seq[FileMetrics]
                  )

  class PageBackend($: BackendScope[Props, State]) {

    def render(p: Props, s: State) = {
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
              $.setState(State(page=0, fileMetrics=Seq())) >>
                Callback.log("Build clicked")
          ))(<.div("Build")),
          Button.component(Button.props(
            size = "lg",
            `type`="primary",
            onClick =
              $.setState(State(page=1, fileMetrics=Seq())) >>
                Callback.log("Indices clicked")
          ))(<.div("Indices")),
          Button.component(Button.props(size = "lg", `type`="primary"))(<.div("About"))
        ),
        if (s.page == 0) { Table.Component(Table.Props(p.proxy, p.ctl)) } else { <.div("Nothing")}
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Page")
      .initialState(State(page=0, fileMetrics=Seq()))
      .renderBackend[PageBackend]
//      .componentDidMount(_.backend.start)  // This fires off request to server for data to render
      .build

}

