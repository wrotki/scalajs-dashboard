
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel,JSExport}

import page.Page
import spa.client.logger._
import buildresults.router.AppRouter

@JSExportTopLevel("SPAMain")
object SPAMain {

  @JSExport
  def main(args: Array[String]): Unit = {
    log.info("Application starting")
    println("Hello world!")

//    val aceTarget = dom.document.getElementById("ace")
//    Page.component.renderIntoDOM(aceTarget)

    val routerTarget = dom.document.getElementById("root")
    AppRouter.router().renderIntoDOM(routerTarget)

    // val lft: Int => Option[Int] = Seq(1,2,3).lift
  }

}

