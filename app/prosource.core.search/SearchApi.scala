package prosource.core.search

import com.sksamuel.elastic4s.ElasticDsl
import Elastic._
import com.sksamuel.elastic4s.ElasticDsl._
import scala.concurrent.duration.Duration

/**
 * Created by Marco Chu on 6/4/14.
 */
object SearchApi {
  implicit val duration = Duration(10000, "millis")

  def search(keyword: String, page: Int, pageSize: Int, entityTypes: String*): Map[String, Any] = {
    val query = ElasticDsl.search in defaultIndex types (entityTypes: _*)
    if (keyword.length > 0) {
      println(keyword)
      query.query(keyword)
    }
    println(keyword + " " + page + " " + pageSize)
    query.from((page - 1) * pageSize).limit(pageSize)
    println(query.toString)
    val response = esClient.sync.search(query)
    ElasticDataHelper.getData(response)
  }


  def searchAllTeacher(): Map[String, Any] = {
    val query = (
      ElasticDsl.search in defaultIndex types profileType
        query {
        matches("position", "Teacher")
      }
      )
    val response = esClient.sync.search(query)

    println(ElasticDataHelper.getData(response).get("total").get)
    ElasticDataHelper.getData(response)
  }


}
