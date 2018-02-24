package page

import autowire._
import config.ConfigApi
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom.html.TableRow
import spa.client.elemental.css.{Table => ElementalTable}
import state.State
import tableaccess.{ConfigServer, FileMetrics}

import scala.concurrent.ExecutionContext.Implicits.global


object Table {
  def table = {
    val rows = ScalaComponent.builder[Unit]("Rows")
      .initialState(State(Seq()))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)  // This fires off request to server for data to render
      .build

    rows
  }
  def apply = table()
}

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
    ElementalTable.component(ElementalTable.props(size = "0"))(
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
          <.th(stats._2),
          <.th("LastBuildFail"),
          <.th(stats._3)
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

  def fileMetricsStats(sfm: Seq[FileMetrics]) = {
    val successCount = sfm count{ _.buildSuccess > 0 }
    val failCount = sfm count{ _.buildSuccess == 0 }
    val lastFailCount = sfm count{ _.lastBuildResult != "success" }
    (successCount, failCount, lastFailCount)
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
