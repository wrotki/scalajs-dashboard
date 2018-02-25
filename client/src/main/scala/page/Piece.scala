package page

import japgolly.scalajs.react.vdom.{TagOf, TopNode}
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import org.scalajs.dom.html.{Div, TableSection}


object Piece {

  val root: TagOf[Div] = <.div(
      <.div("ROOT")
  )


  val one: TagOf[TableSection] = <.thead(
    <.tr(
      <.th("One"),
      <.th("Two"),
      <.th("Three")
    )
  )

  val two: TagOf[TableSection] = <.thead(
    <.tr(
      <.th("Four"),
      <.th("Five"),
      <.th("Six")
    )
  )
}