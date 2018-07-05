package tableaccess

import com.github.dwhjames.awswrap.dynamodb
import com.github.dwhjames.awswrap.dynamodb.{AttributeValue, _}

// https://www.scanamo.org/

object FileMetricsJson {
  val exampleRow =
    """
      |{
      |  "Arch": "x86_64",
      |  "Component": "os-templates",
      |  "CreateDate": "2018-06-25T23:02:36.468810651Z",
      |  "Format": "rpm",
      |  "ID": "staging_centos_rpm_7_x86--64_os-templates_NWC",
      |  "LastMaintenance": "2018-07-03T01:34:25Z",
      |  "Project": "centos",
      |  "Release": "7",
      |  "Revision": 4,
      |  "State": "indexing",
      |  "Timestamp": "2018-07-03T01:34:26Z",
      |  "TTLExpire": "2018-06-25T23:02:36.468812823Z",
      |  "Type": "centos_rpm_7_x86--64_os-templates"
      |}
    """.stripMargin
}

case class FileMetrics(
                        buildFail: Long,
                        buildSuccess: Long,
                        downloads: Long,
                        filename: String,
                        lastBuildAttempt: String,
                        lastBuildResult: String,
                        lastError: String,
                        lastBatchID: String,
                        lastRequestID: Long,
                        packageName: String,
                        revision: Long,
                        timestamp: String
                      )


object FileMetrics {

  //val tableName = "BigBang.mpo-18.FileMetrics"

  object Attributes {
    val buildFail = "BuildFail"
    val buildSuccess = "BuildSuccess"
    val downloads = "Downloads"
    val filename = "Filename"
    val lastBuildAttempt = "LastBuildAttempt"
    val lastBuildResult = "LastBuildResult"
    val lastError = "LastError"
    val lastBatchID = "LastBatchID"
    val lastRequestID = "LastRequestID"
    val packageName = "PackageName"
    val revision = "Revision"
    val timestamp = "Timestamp"
  }

  implicit object fileMetricsSerializer extends DynamoDBSerializer[FileMetrics] {

    //override val tableName = "BigBang.mpo-18.FileMetrics"
    override val tableName = "BigBang.staging.FileMetrics"
    override val hashAttributeName = Attributes.packageName
    override val rangeAttributeName = Some(Attributes.filename)

    override def primaryKeyOf(score: FileMetrics) =
      Map(
        Attributes.packageName -> score.packageName,
        Attributes.filename -> score.filename
      )

    override def toAttributeMap(score: FileMetrics) =
      Map(
        Attributes.buildFail -> score.buildFail,
        Attributes.buildSuccess -> score.buildSuccess,
        Attributes.downloads -> score.downloads,
        Attributes.filename -> score.filename,
        Attributes.lastBuildAttempt -> score.lastBuildAttempt,
        Attributes.lastBuildResult -> score.lastBuildResult,
        Attributes.lastError -> score.lastError,
        Attributes.lastBatchID -> score.lastBatchID,
        Attributes.lastError -> score.lastRequestID,
        Attributes.packageName -> score.packageName,
        Attributes.revision -> score.revision,
        Attributes.timestamp -> score.timestamp
      )

    override def fromAttributeMap(item: collection.mutable.Map[String, AttributeValue]) = {
      val lastBuildResultVal: String = item.get(Attributes.lastBuildResult) match {
        case Some(av) => av
        case _ => dynamodb.stringToAttributeValue("")
      }
      val lastErrorVal: String = item.get(Attributes.lastError) match {
        case Some(av) => av
        case _ => dynamodb.stringToAttributeValue("")
      }
      val lastBatchIDVal: String = item.get(Attributes.lastBatchID) match {
        case Some(av) => av
        case _ => dynamodb.stringToAttributeValue("")
      }
      val lastRequestIDVal = item.get(Attributes.lastRequestID) match {
        case Some(av) => av
        case _ => dynamodb.longToAttributeValue(0)
      }
      FileMetrics(
        buildFail = item(Attributes.buildFail),
        buildSuccess = item(Attributes.buildSuccess),
        downloads = item(Attributes.downloads),
        filename = item(Attributes.filename),
        lastBuildAttempt = item(Attributes.lastBuildAttempt),
        lastBuildResult = lastBuildResultVal,
        lastError = lastErrorVal,
        lastBatchID = lastBatchIDVal,
        lastRequestID = lastRequestIDVal,
        packageName = item(Attributes.packageName),
        revision = item(Attributes.revision),
        timestamp = item(Attributes.timestamp)
      )
    }
  }

}
