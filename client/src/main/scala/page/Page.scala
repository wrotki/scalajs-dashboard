package page

import config.ConfigApi
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import org.scalajs.dom
import spa.client.elemental.buttons.Button
import spa.client.elemental.misc.Card
import state.State
import tableaccess.{ConfigServer, FileMetrics}

object Page {

  def page = {

    val content = ScalaComponent.builder[Unit]("Page")
      .initialState(State(Seq()))
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
      Card.component(Card.props("dummy"))(
        // ,onClick = Callback { $.setState(State(fileMetrics))})
        Button.component(Button.props(
          size = "lg",
          `type`="primary",
          onClick = Callback { dom.window.alert("Build clicked") }
        ))(<.div("Build")),
        Button.component(Button.props(size = "lg", `type`="primary"))(<.div("Indices")),
        Button.component(Button.props(size = "lg", `type`="primary"))(<.div("About"))
      ),
      Table.component
    )
    page
  }
}