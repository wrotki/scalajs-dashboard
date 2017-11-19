package tableaccess

import java.nio.ByteBuffer

import boopickle.Default._

// client-side implementation, and call-site
object ConfigApi extends autowire.Client[ByteBuffer, Pickler, Pickler]{

  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)

//  def write[Result: Pickler](r: Result) = Unpickle[Result].fromBytes(r)
//  def read[Result: Pickler](p: String) = upickle.read[Result](p)

  override def doCall(req: Request) = {
    println(req)
    MyServer.routes.apply(req)
  }
}

