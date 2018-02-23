package page

import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.<
import org.scalajs.dom.html.Div
import state.State
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.Scala
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import org.scalajs.dom.html.{Div, Table, TableRow}
import spa.client.elemental.css.{ Table => ElementalTable }
import tableaccess.{ConfigServer, FileMetrics}
import config.ConfigApi
import scala.concurrent.ExecutionContext.Implicits.global
import boopickle.Default._
import autowire._

object Page {

  def get: TagOf[Div]  =
      <.div(
        //    <.div("BEFORE"),
        //    ace,
        //    <.div("AFTER"),
        //    row,
        Table.get
      )
}
