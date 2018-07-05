package buildresults.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import spa.client.elemental.buttons.Button
import spa.client.elemental.misc.Card
import page.Table
import buildresults.diode.{AppCircuit, AppState, SetPage}
import buildresults.router.AppRouter.{HomeRoute, InstancesRoute, Page}

object HeaderNav {

  case class Props(proxy: ModelProxy[AppState], ctl: RouterCtl[Page])

  val Component = ScalaFnComponent[Props](props => {
    val proxy = props.proxy()

    <.div(
      ^.display := "flex",
      Card.component(Card.props(""))(
        // ,onClick = Callback { $.setState(State(fileMetrics))})
        Button.component(Button.props(
          size = "lg",
          `type` = "primary",
          onClick =
            props.ctl.set(HomeRoute) >>
//              Callback {AppCircuit.dispatch(SetPage(0))} >>
              Callback.log("Build clicked")
        ))(<.div("Build")),
        Button.component(Button.props(
          size = "lg",
          `type` = "primary",
          onClick =
            props.ctl.set(InstancesRoute) >>
//              Callback {AppCircuit.dispatch(SetPage(1))} >>
              Callback.log("Instances clicked")
        ))(<.div("Instances")),
        Button.component(Button.props(size = "lg", `type` = "primary"))(<.div("About"))
      )
    )
  })

  def apply(props: Props) = Component(props)
}
