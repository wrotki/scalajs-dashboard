package page

import config.ConfigApi
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import org.scalajs.dom
import spa.client.elemental.buttons.Button
import spa.client.elemental.misc.Card
import state.State
import tableaccess.{ConfigServer, FileMetrics}

object Page {

  def page = {

    val content = ScalaComponent.builder[Unit]("Page")
      .initialState(State(page=0, fileMetrics=Seq()))
      .renderBackend[PageBackend]
//      .componentDidMount(_.backend.start)  // This fires off request to server for data to render
      .build
    content
  }

  def component = page()
}


class PageBackend($: BackendScope[Unit, State]) {

  def render(s: State) = {
    val page = <.div(
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
      if (s.page == 0) { Table.component } else { <.div("Nothing")}
    )
    page
  }
}