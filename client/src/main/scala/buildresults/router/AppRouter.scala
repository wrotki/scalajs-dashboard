package buildresults.router

import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import buildresults.components.Layout
import buildresults.diode.AppCircuit
import buildresults.pages.InstancesPage
import page.DashboardPage

object AppRouter {
  sealed trait Page
  case object HomeRoute extends Page
  case object InstancesRoute extends Page

  val connection = AppCircuit.connect(_.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, HomeRoute) ~> renderR(renderDashboardPage)
      | staticRoute("instances", InstancesRoute) ~> renderR(renderInstancesPage)
      )
      .notFound(redirectToPage(HomeRoute)(Redirect.Replace))
      .renderWith(layout)
  }

  def renderDashboardPage(ctl: RouterCtl[Page]) = {
    connection(proxy => DashboardPage.Component(DashboardPage.Props(proxy, ctl)))
  }

  def renderInstancesPage(ctl: RouterCtl[Page]) = {
    connection(proxy => InstancesPage.Component(InstancesPage.Props(proxy, ctl)))
  }

  def layout (c: RouterCtl[Page], r: Resolution[Page]) = connection(proxy => Layout(Layout.Props(proxy, c, r)))

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
