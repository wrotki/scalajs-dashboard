package config

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2._
import com.github.dwhjames.awswrap.dynamodb.{AmazonDynamoDBScalaClient, AmazonDynamoDBScalaMapper}
import tableaccess.FileMetrics

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object ConfigApiImpl extends ConfigApi {

  val client = {
    val jClient = new AmazonDynamoDBAsyncClient(new BasicAWSCredentials("FAKE_ACCESS_KEY", "FAKE_SECRET_KEY"))
    jClient.setEndpoint("http://localhost:8000")

    new AmazonDynamoDBScalaClient(jClient)
  }

  val mapper = AmazonDynamoDBScalaMapper(client)

  val fileMetrics =  mapper.scan[FileMetrics]()

//  val queryResult =       mapper.query[FileMetrics](
//    FileMetrics.globalSecondaryIndexName,
//    FileMetrics.Attributes.gameTitle,
//    "Galaxy Invaders",
//    Some(FileMetrics.Attributes.topScore -> QueryCondition.greaterThan(0)),
//    false,
//    10 // top ten high scores
//  )

  override def getConfig(service: String, deployment: String): String =
    """{
    |  "foo": "bar",
    |  "active": false
    |}
    |"""
      .stripMargin

  override def getFileMetrics(): Seq[FileMetrics] = Await.result(fileMetrics, 300 seconds)

}


