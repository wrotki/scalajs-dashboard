package tableaccess

trait ConfigApi{
  def getConfig(service: String, deployment: String): String
}
