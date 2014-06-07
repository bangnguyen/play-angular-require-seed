package prosource.core.search

import com.sksamuel.elastic4s.ElasticDsl
import Elastic._

/**
 * Created by Marco Chu on 6/4/14.
 */
object SearchApi {

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

}
