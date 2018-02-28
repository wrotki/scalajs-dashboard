package spa.client.elemental.misc

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Card {

  @JSImport("!style-loader!css-loader!less-loader!elemental/less/elemental.less", JSImport.Default)
  @js.native
  object ElementalCss extends js.Object

  @JSImport("elemental", "Card")
  @js.native
  object RawComponent extends js.Object


  @js.native
  trait Props extends js.Object {
    var dummy: String = js.native

  }

  def props(
             sm: String
           ): Props = {
    val p = (new js.Object).asInstanceOf[Props]
    p.dummy = sm
    p
  }

  val component = JsComponent[Props, Children.Varargs, Null](RawComponent)
  val css2 = ElementalCss
}
