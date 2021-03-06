package spa.client.elemental.misc

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Card {

  //  Commented outJSImport("./elemental/less/elemental.less", JSImport.Default)
  @JSImport("./node_modules/elemental/less/elemental.less", JSImport.Default)
  @js.native
  object ElementalCss extends js.Object

  @JSImport("elemental", "Card")
  @js.native
  object RawComponent extends js.Object


  @js.native
  trait Props extends js.Object {
    var className: String = js.native

  }

  def props(
             className: String
           ): Props = {
    val p = (new js.Object).asInstanceOf[Props]
    p.className = className
    p
  }

  val component = JsComponent[Props, Children.Varargs, Null](RawComponent)
  val css2 = ElementalCss
}
