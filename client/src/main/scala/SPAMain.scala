
import org.scalajs.dom
import page.Page
import spa.client.logger._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport


@JSExport("SPAMain")
object SPAMain extends js.JSApp {

  @JSExport
  def main(): Unit = {
    log.warn("Application starting")

    Page().renderIntoDOM(dom.document.getElementById("ace"))
  }

}

