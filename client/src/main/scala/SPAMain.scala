
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSImport}
import org.scalajs.dom
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.Js.{MountedWithRawType, RawMounted}
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
import tableaccess.{ConfigServer, FileMetrics}

import scala.concurrent.Future


@JSExport("SPAMain")
object SPAMain extends js.JSApp {

  case class State(fileMetrics: Seq[FileMetrics])

  class Backend($: BackendScope[Unit, State]) {

    def updateFileMetrics (fileMetrics: Seq[FileMetrics]): Callback = {
      $.setState(State(fileMetrics))
    }

    def start = Callback.future {
      println(s"calling getFileMetrics ...")
      val ret = ConfigServer[ConfigApi].getFileMetrics().call() map {
        res => {
          println(s"calling getFileMetrics complete!!!: $res")
          updateFileMetrics(res)
        }
      }
      ret
    }

    def render(s: State) = {
      // <.div("Seconds elapsed: ", s.secondsElapsed)
      println(s"render: $s")
      val rows = fileMetricsRows(s.fileMetrics)
      <.tbody(
        rows: _*
      )
    }
  }

  val Rows = ScalaComponent.builder[Unit]("Rows")
    .initialState(State(Seq()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

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

  val table = Table.component(Table.props(size = "0"))(
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
        <.th("Package"),
        <.th("BatchID"),
        <.th("RequestID"),
        <.th("Error"),
        <.th("Log")
      )
    ),
    Rows()
//    <.tbody(
//      trows:_*
//    )
  )

  val component = <.div(

    <.div("BEFORE"),
    ace,
    <.div("AFTER"),
    row,
    table
  )

  @JSExport
  def main(): Unit = {
    log.warn("Application starting")
    component.renderIntoDOM(dom.document.getElementById("ace"))
  }

  def fileMetricsRows(sfm: Seq[FileMetrics]): Seq[TagOf[TableRow]] = {
//    val data = Seq( 0 -> "python2.7",1 -> "gcc5.5", 2 ->"mongodb", 3 ->"rails", 4 -> "ruby", 5 ->"mysql").seq


//    val data = fileMetrics zip Stream.from(0) map { fm => (fm._2, fm._1.filename) } take 10
    println(s"foreach sfm:")
    sfm foreach println

    val data = sfm take 100 zip (0 to 100) map { fm => (fm._2, fm._1.filename) }
    println(s"foreach data:")
    data foreach println
    println(s"getdatarows: $data")

    var rows = data map { t =>
      val id = t._1
      val pack = t._2
      <.tr(
        <.td(
          <.label(
            <.input(^.`type` := "checkbox")
          )
        ),
        <.td(
          <.a(^.href := s"javascript:alert('$pack');")(pack)
        ),
        <.td("orange_centos_3Rb-4qYf"),
        <.td(id),
        <.td("Success"),
        <.td("https://s3-us-west-2.amazonaws.com/scrambled-binaries-logs/logs/2016-09-20-11-40-46-2E3242BCDF4C7CE5")
      )
    }
    rows
  }
}

