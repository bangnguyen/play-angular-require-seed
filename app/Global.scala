import _root_.db.Cassandra
import _root_.db.ColumnFamilyParams._
import com.github.tototoshi.csv.CSVReader
import com.netflix.astyanax.util.SingletonEmbeddedCassandra
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._
import java.io.File
import java.text.SimpleDateFormat
import models.User
import play.api._
import models._
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.mvc.{SimpleResult, RequestHeader}
import prosource.core.search.Elastic
import scala.concurrent.Future
import play.api.mvc.Results.BadRequest
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._
import prosource.core.search.Elastic._
import utils.Position


object Global extends GlobalSettings {



  override def onStart(app: Application) {
  //  println("--------------------------- SingletonEmbeddedCassandra.getInstance ")
    SingletonEmbeddedCassandra.getInstance
    Thread.sleep(1000 * 2)
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = {
    println("onError " + ex.getMessage)
    if (ex.getMessage.contains("PsException")) {
      println("1")
      Future.successful(BadRequest(Json.obj(
        "message" -> ex.getCause.getMessage
      )))

    }
    else {
      println("2")
      super.onError(request, ex)
    }
  }

}
