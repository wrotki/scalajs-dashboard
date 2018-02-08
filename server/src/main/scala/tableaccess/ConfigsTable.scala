package tableaccess

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, GetItemRequest}
import java.util._


object ConfigsTable {

  val table_name = "BigBang-Configs"
  val key_to_get = new HashMap[String, AttributeValue]

  key_to_get.put("ServiceName", new AttributeValue("manager"))
  key_to_get.put("Deployment", new AttributeValue("mborsa"))

  val request = new GetItemRequest()
    .withKey(key_to_get)
    .withTableName(table_name)
  val ddb = AmazonDynamoDBClientBuilder.defaultClient()
  val item = ddb.getItem(request).getItem()

}
