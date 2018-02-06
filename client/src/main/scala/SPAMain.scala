
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSImport}
import org.scalajs.dom
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.Js.{MountedWithRawType, RawMounted, UnmountedWithRawType}
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.TableRow
import org.scalajs.dom.raw.HTMLTableRowElement
import spa.client.ace.Ace
import spa.client.elemental.buttons.Button
import spa.client.elemental.css.Table
import spa.client.elemental.grid.{Col, Row}
import spa.client.logger._

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import boopickle.Default._
import autowire._
import config.ConfigApi
import japgolly.scalajs.react.component.Scala.Component
import org.scalajs.dom.html
import tableaccess.{ConfigServer, FileMetrics}

import scala.concurrent.Future


@JSExport("SPAMain")
object SPAMain extends js.JSApp {

  // Test going private
  @JSExport
  def main(): Unit = {
    log.warn("Application starting")

    page.renderIntoDOM(dom.document.getElementById("ace"))
  }

  val page = {
    <.div(
      //    <.div("BEFORE"),
      //    ace,
      //    <.div("AFTER"),
      //    row,
      table()
    )
  }

  def table = {
    val rows = ScalaComponent.builder[Unit]("Rows")
      .initialState(State(Seq()))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build

      rows
  }


  case class State(fileMetrics: Seq[FileMetrics])

  class Backend($: BackendScope[Unit, State]) {

    def updateFileMetrics(fileMetrics: Seq[FileMetrics]): Callback = {
      $.setState(State(fileMetrics))
    }

    def start = Callback.future {
      val ret = ConfigServer[ConfigApi].getFileMetrics().call() map {
        res => {
          updateFileMetrics(res)
        }
      }
      ret
    }

    def render(s: State) = {
      val rows = fileMetricsRows(s.fileMetrics)
      Table.component(Table.props(size = "0"))(
        <.colgroup(
          <.col(^.width := "10"),
          <.col(^.width := "10%"),
          <.col(^.width := "10%"),
          <.col(^.width := "10%"),
          <.col(^.width := "10%"),
          <.col(^.width := "")
        ),
        <.thead(
          <.tr(
            <.th(
              <.label(
                <.input(^.`type` := "checkbox")
              )
            ),
            <.th("ID"),
            <.th("Filename"),
            <.th("Success"),
            <.th("Fail"),
            <.th("LastResult"),
            <.th("Error Message")
          )
        ),
        <.tbody(
          rows: _*
        )
      )
    }
  }

  val ace = Ace.component(Ace.props(
    mode = "scala",
    theme = "gob",
    name = "ace", // Unique id of <div>
    width = "100%",
    maxLines = 50,
    fontSize = 12,
    value = "function main(){ console.log(\"FooBar\"); }"
  ))()

  val button = Button.component(Button.props(size = "lg"))(<.div("Large button"))
  val col = Col.component(Col.props(sm = "1/3"))(button)
  val row = Row.component(Row.props(size = "0"))(col, col, col)



  def fileMetricsRows(sfm: Seq[FileMetrics]): Seq[TagOf[TableRow]] = {
    val debianPkgs = sfm filter {
      _.filename endsWith ".rpm"
    } sortBy {
      _.filename
    }
    val data = Stream.from(0) zip debianPkgs map { fm =>
      (fm._1, fm._2.filename, fm._2.buildSuccess, fm._2.buildFail, fm._2.lastResult)
    }

    val cls = (success: Long) => if (success > 0) {
      "default"
    } else {
      "danger"
    }
    var rows = data map { t =>
      val id = t._1
      val file = t._2
      val success = t._3
      val fail = t._4
      val lastResult = t._5
      val bgcolor = if (success > 0) {
        "white"
      } else {
        "red"
      }
      <.tr(
        ^.backgroundColor := bgcolor,
        ^.color := "darkbrown",
        <.td(
          <.label(
            <.input(^.`type` := "checkbox")
          ),
          ^.cls := cls(success)
        ),
        <.td(id),
        <.td(
          //          <.a(^.href := s"javascript:alert('$file');")(file)
          file
        ),
        <.td(success),
        <.td(fail),
        <.td(lastResult),
        <.td("TODO: Error message goes here")
      )
    }
    rows
  }
}

