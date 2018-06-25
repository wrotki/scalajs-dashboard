package buildresults.pages

import scala.scalajs.js
import js.JSConverters._
import js.annotation._
import org.scalajs.dom

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import buildresults.models.PageContent
import buildresults.router.AppRouter
import buildresults.diode.AppState
import buildresults.diode.AppCircuit



object WeatherPage {
  @js.native
  @JSImport("lodash.throttle", JSImport.Default)
  private object _throttle extends js.Any
  def throttle: js.Dynamic = _throttle.asInstanceOf[js.Dynamic]


  case class Props (
                     proxy: ModelProxy[AppState],
                     ctl: RouterCtl[AppRouter.Page]
                   )

  case class State(
                    var isLoading: Boolean,
                    var pageContent: Option[PageContent]
                  )

  class Backend($: BackendScope[Props, State]) {
    def getSelectOptions(data: List[WeatherResponse], intputValue: String) = {
      data.zipWithIndex.map { case (item, index) => Select.Options(
        value = s"$intputValue::$index",
        label = s"${item.name}, ${item.sys.country} ${item.weather.head.main} ${(math rint item.main.temp * 10) / 10} °C"
      )}
    }

    def loadWeatherInfo(city: String): Callback = {
      val host = Config.AppConfig.apiHost
      val setLoading = $.modState(s => s.copy(isLoading = true))

      def getData(): Future[List[WeatherResponse]] = {
        dom.ext.Ajax.get(url=s"$host/weather?city=$city").map(xhr => {
          val option = decode[List[WeatherResponse]](xhr.responseText)
          option match {
            case Left(failure) => List.empty[WeatherResponse]
            case Right(data) => data
          }
        })
      }

      def updateState: Future[Callback] = {
        getData().map {weather =>
          AppCircuit.dispatch(GetWeatherSuggestions(weather))
          $.modState(s => s.copy(
            isLoading =  false,
            weatherData = weather,
            selectOptions = getSelectOptions(weather, s.inputValue))
          )
        }
      }

      setLoading >> Callback.future(updateState)
    }

    val throttleInputValueChange: js.Dynamic = {
      throttle(() => {
        $.state.map { state =>
          val city = state.inputValue
          if (city.nonEmpty) {
            loadWeatherInfo(city).runNow()
          }
        }.runNow()
      }, 400)
    }

    def onInputValueChange(value: String): Unit = {
      val selectedValue = try {
        Some(value)
      } catch {
        case e: Exception => None : Option[String]
      }
      $.modState(s => s.copy(inputValue = selectedValue.getOrElse(""))).runNow()
      throttleInputValueChange()
    }

    def onSelectChange(option: Select.Options) = {
      val selectedValue = try {
        Some(option.value)
      } catch {
        case e: Exception => None : Option[String]
      }
      $.modState(s => {
        s.inputValue = selectedValue.getOrElse("")
        if (s.inputValue == "") {
          s.selectOptions = List.empty[Select.Options]
          s.selectedWeather = None: Option[WeatherResponse]

        } else {
          val arr = option.value.split("::")
          val index = if (arr.length == 2) arr(1).toInt else -1
          s.selectedWeather = if (index == -1) None else Some(s.weatherData(index))
        }
        AppCircuit.dispatch(SelectWeather(s.selectedWeather))
        s
      }).runNow()
    }

    def render(p: Props, s: State) = {
      val proxy = p.proxy()
      val weatherData = proxy.weatherSuggestions
      val userInfo = proxy.userInfo
      val select = Select(
        "form-field-name",
        s.selectOptions.toJSArray,
        s.inputValue,
        onInputValueChange,
        onSelectChange,
        pIsLoading = s.isLoading
      )
      <.div(
        ^.margin := "0 auto",
        ^.className := "weather-page",
        <.div(
          ^.className := "weather-page__label",
          "Enter city to get weather: "
        ),
        <.div(
          ^.marginBottom := 10.px,
          select
        ),
        <.div(
          WeatherBox(WeatherBox.Props(s.selectedWeather, p.ctl, userInfo, isSaveBtn = true))
        )
      )
    }
  }

    val Component = ScalaComponent.builder[Props]("WeatherPage")
      .initialState(State(
        isLoading = false,
        pageContent = None : Option[PageContent]
      ))
      .renderBackend[Backend]
      .build
}
