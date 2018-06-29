package buildresults.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._
import buildresults.diode.AppState
import buildresults.router.AppRouter.Page

object Header {
  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[Page],
                    resolution: Resolution[Page]
                  )

  val Component = ScalaFnComponent[Props](props => {
    val proxy = props.proxy()
    val pageContent = proxy.pageContent
    <.div(
      ^.display := "flex",
      ^.justifyContent := "space-between",
      proxy.pageContent.map(pageContent =>
        <.div(
          ^.display := "flex",
          ^.justifyContent := "flex-end",
          pageContent.text
        )
      ).whenDefined
    )
  })

  def apply(props: Props) = Component(props)
}
