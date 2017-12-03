package config


object ConfigApiImpl extends ConfigApi {

  def getConfig(service: String, deployment: String) =
    """{
    |  "foo": "bar",
    |  "active": false
    |}
    |"""
      .stripMargin
}


