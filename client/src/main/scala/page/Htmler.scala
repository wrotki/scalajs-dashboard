package page

// https://typelevel.org/cats/
import cats.Monoid
import japgolly.scalajs.react.vdom.{TagOf, TopNode}
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import org.scalajs.dom.html.{Div, TableSection}
import org.scalajs.dom.raw.HTMLElement
import page.Htmler.htmlerAdditionMonoid


class Htmler( val tag: Option[TagOf[HTMLElement]] ){

  import Htmler.Tag

  val root: TagOf[Div] = <.div(
    <.div("ROOT")
  )

  val one: TagOf[TableSection] = <.thead(
    <.tr(
      <.th("One"),
      <.th("Two"),
      <.th("Three")
    )
  )

  val two: TagOf[TableSection] = <.thead(
    <.tr(
      <.th("Four"),
      <.th("Five"),
      <.th("Six")
    )
  )

  def ++ (y: Htmler): Htmler = Htmler(htmlerAdditionMonoid.combine(this.tag,y.tag))
}

object Htmler {
  type Tag = Option[TagOf[HTMLElement]]

  def apply[T](tag: Tag): Htmler = new Htmler(tag)

  val htmlerAdditionMonoid: Monoid[Tag] = new Monoid[Tag] {
    def empty: Htmler.Tag = None
    def combine(x: Tag, y: Tag): Tag = {
      val root = <.div(
        x.get,
        y.get
      )
      Some(root)
    }
  }

}