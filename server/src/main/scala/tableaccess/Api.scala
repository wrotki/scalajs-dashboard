package tableaccess

import java.nio.ByteBuffer
import boopickle.Default._
import scala.concurrent.ExecutionContext.Implicits.global


object ConfigApiImpl extends ConfigApi {

  def getConfig(service: String, deployment: String) =
    """{
    |  "foo": "bar",
    |  "active": false
    |}
    |"""
      .stripMargin
}

object ConfigServer extends autowire.Server[ByteBuffer, Pickler, Pickler]{

  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)

  val routes = ConfigServer.route[ConfigApi](ConfigApiImpl)
}

