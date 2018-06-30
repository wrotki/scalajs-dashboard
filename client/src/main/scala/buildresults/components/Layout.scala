package buildresults.components


import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import diode.react.ModelProxy
import buildresults.diode.AppState
import buildresults.config.Config
import buildresults.diode.AppCircuit.connect
import buildresults.diode._
import buildresults.models.PageContent
import buildresults.router.AppRouter.Page
import page.DashboardPage
import spa.client.logger.log

object Layout {
  val connection = connect(_.state)

  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[Page],
                    resolution: Resolution[Page]
                  )

  class Backend(bs: BackendScope[Props, Unit]) {
    //    val host: String = Config.AppConfig.apiHost

    //    def getUserResponse = CallbackTo[Future[UserResponse]] {
    //      AppCircuit.dispatch(SetLoadingState())
    //      dom.ext.Ajax
    //        .get(url=s"$host/user-info", withCredentials=true)
    //        .map {xhr =>
    //          val option = decode[UserResponse](xhr.responseText)
    //          option match {
    //            case Left(failure) => UserResponse(GithubUser(), List.empty[OpenWeatherBaseCity])
    //            case Right(data) => data
    //          }
    //        }
    //    }
    //
    //    def dispatchUserInfo(userInfoFuture: Future[UserResponse]) = CallbackTo[Future[UserResponse]] {
    //      userInfoFuture.map {userInfo =>
    //        val userInfoOption = if (userInfo.user.id != -1) Some(userInfo) else None
    //        AppCircuit.dispatch(ClearLoadingState())
    //        AppCircuit.dispatch(GetUserInfo(userInfoOption))
    //        userInfo
    //      }
    //    }
    //
    //    def loadAndDispatchCitiesWeather(userInfoFuture: Future[UserResponse]) = Callback {
    //      userInfoFuture.map { userInfo =>
    //        userInfo.cities.map {city =>
    //          dom.ext.Ajax.get(url=s"$host/weather-city?id=${city.id}").map {xhr =>
    //            val option = decode[WeatherResponse](xhr.responseText)
    //            option match {
    //              case Left(_) => None
    //              case Right(data) => AppCircuit.dispatch(GetWeatherForFavCity(data))
    //            }
    //          }
    //        }
    //      }
    //    }

    def getPage = Callback {
      AppCircuit.dispatch(SetLoadingState())
      AppCircuit.dispatch(GetPageContent(Some(PageContent("Some page content"))))
      log.info("Layout::getPage")

      println("In getPage")
    }

    //    def mounted: Callback = getUserResponse >>= dispatchUserInfo >>= loadAndDispatchCitiesWeather
    def mounted: Callback = getPage

    def render(props: Props): VdomElement = {
      <.div(
//        <.div(
//          ^.cls := "container",
//          connection(proxy => DashboardPage.Component(DashboardPage.Props(proxy, props.ctl)))
//        ),
        <.div(^.cls := "container", props.resolution.render()),
        connection(proxy => LoadingIndicator(LoadingIndicator.Props(proxy)))
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("Layout")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
