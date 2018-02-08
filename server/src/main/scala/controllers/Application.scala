package controllers

import java.nio.ByteBuffer

import boopickle.Default._
import com.google.inject.Inject
import config.{ConfigApi, ConfigApiImpl}
import play.api.{Configuration, Environment}
import play.api.mvc._
//import services.ApiService
//import spatutorial.shared.Api

import scala.concurrent.ExecutionContext.Implicits.global

object Router extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)
}

class Application @Inject() (implicit val config: Configuration, env: Environment) extends Controller {
//  val apiService = new ApiService()

  def index = Action {
    val scriptUrl = bundleUrl("client")
    val cssUrl = cssBundleUrl
    Ok(views.html.index("SPA Boilerplate",scriptUrl, cssUrl))
  }

  // https://github.com/scalacenter/scalajs-bundler/blob/master/sbt-web-scalajs-bundler/src/sbt-test/sbt-web-scalajs-bundler/play/server/src/main/scala/example/ExampleController.scala#L25-L30
  def bundleUrl(projectName: String): Option[String] = {
    val name = projectName.toLowerCase
    Seq(s"$name-opt-bundle.js", s"$name-fastopt-bundle.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
  }

  def cssBundleUrl: Option[String] = {
    Seq(s"elementalcss-bundle.js")
      .find(name => getClass.getResource(s"/public/elementalcss-bundle.js") != null)
      .map(controllers.routes.Assets.versioned(_).url)
    //Some(s"/assets/elementalcss-bundle.js")
  }

  def autowireApi(path: String) = Action.async(parse.raw) {
    implicit request =>
      println(s"Request path: $path")

      // get the request body as ByteString
      val b = request.body.asBytes(parse.UNLIMITED).get

      // call Autowire route
      ConfigServer.routes(
        autowire.Core.Request(path.split("/"), Unpickle[Map[String, ByteBuffer]].fromBytes(b.asByteBuffer))
      ).map(buffer => {
        val data = Array.ofDim[Byte](buffer.remaining())
        buffer.get(data)
        Ok(data)
      })
  }

  object ConfigServer extends autowire.Server[ByteBuffer, Pickler, Pickler]{

    override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
    override def write[R: Pickler](r: R) = Pickle.intoBytes(r)

    val routes = ConfigServer.route[ConfigApi](ConfigApiImpl)
  }


  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }
}
