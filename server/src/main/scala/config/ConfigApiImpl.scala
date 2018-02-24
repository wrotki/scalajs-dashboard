package config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.dynamodbv2._
import com.github.dwhjames.awswrap.dynamodb.{AmazonDynamoDBScalaClient, AmazonDynamoDBScalaMapper}
import tableaccess.FileMetrics

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object ConfigApiImpl extends ConfigApi {

  val client = {
    val jClient = new AmazonDynamoDBAsyncClient(new DefaultAWSCredentialsProviderChain())

    jClient.setEndpoint("https://dynamodb.us-west-2.amazonaws.com")

    new AmazonDynamoDBScalaClient(jClient)
  }

  val mapper = AmazonDynamoDBScalaMapper(client)


  override def getConfig(service: String, deployment: String): String =
    """{
      |  "foo": "bar",
      |  "active": false
      |}
      |"""
      .stripMargin

  override def getFileMetrics(): Seq[FileMetrics] = {
    val fileMetrics = mapper.scan[FileMetrics]()
    Await.result(fileMetrics, 300 seconds)
  }

}


