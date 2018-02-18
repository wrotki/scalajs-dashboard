
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

    def filterByDistro (sfm: Seq[FileMetrics]): Seq[FileMetrics] = {
      sfm filter {
        _.filename endsWith ".rpm"
      }
    }

    def render(s: State) = {
      val rows = fileMetricsRows(filterByDistro(s.fileMetrics))
      val stats = fileMetricsStats(filterByDistro(s.fileMetrics))
      Table.component(Table.props(size = "0"))(
        <.thead(
          <.tr(
            <.th(
              <.label(
                <.input(^.`type` := "checkbox")
              )
            ),
            <.th(""),
            <.th(""),
            <.th("Success"),
            <.th(stats._1),
            <.th("Fail"),
            <.th(stats._2)
          )
        ),
        <.colgroup(
          <.col(^.width := "10"),
          <.col(^.width := "3%"),
          <.col(^.width := "20%"),
          <.col(^.width := "5%"),
          <.col(^.width := "5%"),
          <.col(^.width := "5%"),
          <.col(^.width := "5%"),
          <.col(^.width := "5%"),
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
            <.th("LastBuildResult"),
            <.th("LastBatchID"),
            <.th("LastRequestID"),
            <.th("Last Error")
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

  def fileMetricsStats(sfm: Seq[FileMetrics]) = {
    val successCount = sfm count{ _.buildSuccess > 0 }
    val failCount = sfm count{ _.buildSuccess == 0 }
    (successCount, failCount)
  }

  def fileMetricsRows(sfm: Seq[FileMetrics]): Seq[TagOf[TableRow]] = {
    val debianPkgs =  sfm sortBy {
      _.filename
    }
    val data = Stream.from(0) zip debianPkgs map { fm =>
      (fm._1, fm._2.filename, fm._2.buildSuccess, fm._2.buildFail,
        if(fm._2.buildSuccess>0) fm._2.lastBuildResult else "failred",
        fm._2.lastError, fm._2.lastBatchID, fm._2.lastRequestID)
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
      val lastError = t._6
      val lastBatchID = t._7
      val lastRequestID = t._8
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
        <.td(lastBatchID),
        <.td(lastRequestID),
        <.td(lastError)
      )
    }
    rows
  }
}

