package tableaccess

import com.github.dwhjames.awswrap.dynamodb._

case class FileMetrics(
                        buildFail: Long,
                        buildSuccess: Long,
                        downloads: Long,
                        filename: String,
                        lastBuildAttempt: String,
                        lastResult: String,
                        packageName: String,
                        revision: Long,
                        timestamp: String
                      )


object FileMetrics {

  val tableName = "BigBang.staging.FileMetrics"

  object Attributes {
    val buildFail = "BuildFail"
    val buildSuccess = "BuildSuccess"
    val downloads = "Downloads"
    val filename = "Filename"
    val lastBuildAttempt = "LastBuildAttempt"
    val lastResult = "LastResult"
    val packageName = "PackageName"
    val revision = "Revision"
    val timestamp = "Timestamp"
  }

  implicit object fileMetricsSerializer extends DynamoDBSerializer[FileMetrics] {

    override val tableName = FileMetrics.tableName
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
        Attributes.lastResult -> score.lastResult,
        Attributes.packageName -> score.packageName,
        Attributes.revision -> score.revision,
        Attributes.timestamp -> score.timestamp
      )

    override def fromAttributeMap(item: collection.mutable.Map[String, AttributeValue]) =
      FileMetrics(
        buildFail = item(Attributes.buildFail),
        buildSuccess = item(Attributes.buildSuccess),
        downloads = item(Attributes.downloads),
        filename = item(Attributes.filename),
        lastBuildAttempt = item(Attributes.lastBuildAttempt),
        lastResult = item(Attributes.lastResult),
        packageName = item(Attributes.packageName),
        revision = item(Attributes.revision),
        timestamp = item(Attributes.timestamp)
      )
  }

}
