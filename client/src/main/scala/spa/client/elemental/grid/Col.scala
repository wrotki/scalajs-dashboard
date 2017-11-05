package spa.client.elemental.grid

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Col {

  @JSImport("!style-loader!css-loader!less-loader!elemental/less/elemental.less", JSImport.Default)
  @js.native
  object ElementalCss extends js.Object

  @JSImport("elemental", "Col")
  @js.native
  object RawComponent extends js.Object


  @js.native
  trait Props extends js.Object {
    var sm: String = js.native

  }

  def props(
             sm: String
           ): Props = {
    val p = (new js.Object).asInstanceOf[Props]
    p.sm = sm
    p
  }

  val component = JsComponent[Props, Children.Varargs, Null](RawComponent)
  val css2 = ElementalCss
}
