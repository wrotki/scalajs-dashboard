package config

trait ConfigApi{
  def getConfig(service: String, deployment: String): String
}
