package buildresults.config

import scala.scalajs.js

object Config {
  @js.native
  @JSGlobal("appConfig")
  object AppConfig extends js.Object {
    val apiHost: String = js.native
  }
}
