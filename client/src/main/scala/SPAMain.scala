
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import page.DashboardPage
import spa.client.logger._
import buildresults.router.AppRouter
import spa.client.elemental.buttons.Button

@JSExportTopLevel("SPAMain")
object SPAMain {

  @JSExport
  def main(args: Array[String]): Unit = {
    log.info("Application start")
    println("Hello world!")
//    println(Button.ElementalCss)
//    val aceTarget = dom.document.getElementById("ace")
//    Page().renderIntoDOM(aceTarget)

    val routerTarget = dom.document.getElementById("root")
    AppRouter.router().renderIntoDOM(routerTarget)
  }

}

