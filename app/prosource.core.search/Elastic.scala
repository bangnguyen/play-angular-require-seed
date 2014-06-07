package prosource.core.search

import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.{FilterBuilders, QueryBuilders}
import scala.collection.JavaConversions._
import org.elasticsearch.common.settings.ImmutableSettings
import com.sksamuel.elastic4s._

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType.{DateType, LongType, IntegerType, StringType}
import scala.concurrent.duration.Duration

object Elastic {
  implicit val duration = Duration(10000, "millis")
  val defaultIndex = "prosource"
  val profileType = "profile"
  val settings = ImmutableSettings.settingsBuilder()
    .put("http.enabled", false)
    .put("path.home", "/tmp/elastic/")

  val esClient = ElasticClient.local(settings.build)



  def createIndex = {
    esClient.execute {
      create index defaultIndex mappings (
        profileType as(
          "id" typed StringType,
          "fullName" typed StringType index "not_analyzed",
          "email" typed StringType,
          "phone" typed StringType,
          "firstName" typed StringType,
          "lastName" typed StringType,
          "address" typed StringType,
          "birthday" typed DateType
          )
        )
    }
  }

  def url(indexType: String) = defaultIndex.concat("/").concat(indexType)


}

object ElasticDataHelper {

  def getData(r: SearchResponse): Map[String, Any]= {
    val hits = r.getHits
    val iterator = hits.iterator()
    var result: List[Map[String, Any]] = List()
    while (iterator.hasNext) {
      val searchHit = iterator.next()
      var source = searchHit.getSource
      source = source + ("type" -> searchHit.getType)
      source = source + ("id" -> searchHit.getId)
      result ::= source.toMap
    }
    result.reverse
    Map("total" -> hits.totalHits(),"data" ->result)
  }


}
