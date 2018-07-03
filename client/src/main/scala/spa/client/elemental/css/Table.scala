package spa.client.elemental.css

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Table {

  //  Commented outJSImport("./elemental/less/elemental.less", JSImport.Default)
  @JSImport("./node_modules/elemental/less/elemental.less", JSImport.Default)
  @js.native
  object ElementalCss extends js.Object

  @JSImport("elemental", "Table")
  @js.native
  object RawComponent extends js.Object


  @js.native
  trait Props extends js.Object {
    var size: String = js.native

  }

  def props(
             size: String
           ): Props = {
    val p = (new js.Object).asInstanceOf[Props]
    p.size = size
    p
  }

  val component = JsComponent[Props, Children.Varargs, Null](RawComponent)
  val css2 = ElementalCss
}
