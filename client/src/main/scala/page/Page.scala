package page

import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import org.scalajs.dom.html.Div

object Page {

  def apply: TagOf[Div]  =
      <.div(
        //    <.div("BEFORE"),
        //    ace,
        //    <.div("AFTER"),
        //    row,
        Table()
      )
}
