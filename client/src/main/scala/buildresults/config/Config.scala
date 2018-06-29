package buildresults.config

import scala.scalajs.js
import js.annotation.JSGlobal

object Config {
  @js.native
  @JSGlobal("appConfig")
  object AppConfig extends js.Object {
    val apiHost: String = js.native
  }
}
