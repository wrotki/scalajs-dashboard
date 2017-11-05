package spa.client.elemental.buttons

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.{UndefOr, Function0 => JFn0}

object Button {

  @JSImport("!style-loader!css-loader!less-loader!elemental/less/elemental.less", JSImport.Default)
  @js.native
  object ElementalCss extends js.Object

  @JSImport("elemental", "Button")
  @js.native
  object RawComponent extends js.Object

  @js.native
  trait Measures extends js.Object {
    val height: Double = js.native
    val width: Double = js.native
  }

  type OnMeasure = js.Function1[Measures, Unit]
  type OnRest = UndefOr[JFn0[Unit]]

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


