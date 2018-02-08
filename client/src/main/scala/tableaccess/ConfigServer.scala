package tableaccess

import java.nio.ByteBuffer

import boopickle.Default._
import org.scalajs.dom

import scala.concurrent.Future
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import scala.concurrent.ExecutionContext.Implicits.global

// https://github.com/lihaoyi/workbench-example-app/blob/autowire/example/js/src/main/scala/example/ScalaJSExample.scala
// https://github.com/ochrons/scalajs-spa-tutorial/blob/master/doc/en/autowire-and-boopickle.md
// client-side implementation, and call-site
object ConfigServer extends autowire.Client[ByteBuffer, Pickler, Pickler] {

  override def doCall(req: Request): Future[ByteBuffer] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream")
    ).map(r => TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer]))
  }

  override def read[Result: Pickler](p: ByteBuffer) = Unpickle[Result].fromBytes(p)

  override def write[Result: Pickler](r: Result) = Pickle.intoBytes(r)
}


