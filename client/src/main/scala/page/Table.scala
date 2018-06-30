package page

import autowire._
import boopickle.Default._
import buildresults.diode._
import buildresults.models.PageContent
import buildresults.router.AppRouter
import config.ConfigApi
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom
import org.scalajs.dom.html.{TableRow, TableSection}
import spa.client.elemental.buttons.Button
import spa.client.elemental.css.{Table => ElementalTable}
import spa.client.elemental.misc.Card
import spa.client.elemental.grid.{Col, Row}
import spa.client.logger.log
import state.State
import tableaccess.{ConfigServer, FileMetrics}

import scala.concurrent.ExecutionContext.Implicits.global

object Table {

  case class Props (
                     proxy: ModelProxy[AppState],
                     ctl: RouterCtl[AppRouter.Page]
                   )

  case class TableState(
                         var isLoading: Boolean,
                         var fileMetrics: Seq[FileMetrics]
                       )

  class Backend($: BackendScope[Table.Props, State]) {

    def updateFileMetrics(fileMetrics: Seq[FileMetrics]): Callback = {
      $.setState(State(page=0, fileMetrics))
    }

    def start = Callback.future {
      val ret = ConfigServer[ConfigApi].getFileMetrics().call() map {
        fm => {
          log.info("FileMetrics received")

          AppCircuit.dispatch(ClearLoadingState())
          AppCircuit.dispatch(SetFileMetrics(fm))
          updateFileMetrics(fm)
        }
      }
      ret
    }

    def filterByDistro (sfm: Seq[FileMetrics]): Seq[FileMetrics] = {
      sfm filter { fm =>
        (fm.filename endsWith ".rpm") &&
          //        fm.lastBatchID.contains("rhel") &&
          //        fm.filename.contains(".el6.")
          (! fm.filename.contains("i686")) &&
          fm.filename.contains(".el7.")
        //        fm.filename.contains(".fc23.")
        //fm.filename.contains(".fc25.")
      }
    }

    def render(p: Table.Props, s: State) = {

      val proxy = p.proxy()

      //    val root = Htmler(Some(Piece.root))
      //    val one = Htmler(Some(Piece.one))
      //    val two = Htmler(Some(Piece.two))
      //
      //    val combined = root ++ one ++ two

      import Htmler._ // implicit converter Htmler => TagOf[HTMLElement]

      val statsHead = Card.component(Card.props(""))(<.div("Stats"))
      val buildHead = Card.component(Card.props(""))(<.div("Build"))

      //    val build = ElementalTable.component(ElementalTable.props(size = "0"))(
      ////      combined,
      //      build,
      //      renderStats(s),
      //      renderPackagesHead(s),
      //      renderPackagesHeadSizes(s),
      //      renderPackages(s)
      //    )
      val page = Card.component(Card.props(""))(
        Row.component(Row.props(size = ""))(
          Col.component(Col.props(sm = "1/4"))(),
          Col.component(Col.props(sm = "1/2"))(
            statsHead
          ),
          Col.component(Col.props(sm = "1/4"))()
        ),
        Row.component(Row.props(size = ""))(
          Col.component(Col.props(sm = ""))(
            Card.component(Card.props(""))(
              renderStats(s)
            )
          )
        ),
        Row.component(Row.props(size = ""))(
          Col.component(Col.props(sm = "1/4"))(),
          Col.component(Col.props(sm = "1/2"))(
            buildHead
          ),
          Col.component(Col.props(sm = "1/4"))()
        ),
        Row.component(Row.props(size = ""))(
          Col.component(Col.props(sm = ""))(
            renderPackagesHead(s),
            renderPackagesHeadSizes(s),
            renderPackages(s)
          )
        )
      )
      page
    }

    def renderStats(s: State) = {
      val stats = fileMetricsStats(filterByDistro(s.fileMetrics))
      <.div(
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
            <.th(""),
            <.th(""),
            <.th("Success"),
            <.th(stats._1),
            <.th("Fail"),
            <.th(stats._2),
            <.th("LastBuildFail"),
            <.th(stats._3)
          )
        )
      )
    }

    def renderPackagesHeadSizes(s: State) = {
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
      )
    }

    def renderPackagesHead(s: State) = {
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
      )
    }

    def renderPackages(s: State) = {
      val rows = fileMetricsRows(filterByDistro(s.fileMetrics))
      <.tbody(
        rows: _*
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

  def Component = ScalaComponent.builder[Props]("Rows")
      .initialState(State(page=0, fileMetrics=Seq()))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)  // This fires off request to server for data to render
      .build

}

