import com.github.tototoshi.csv.CSVReader
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._
import java.io.File
import java.text.SimpleDateFormat
import play.api._
import models._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.mvc.{SimpleResult, RequestHeader}
import play.api.Play.current
import prosource.core.search.Elastic
import prosource.core.search.Elastic._
import scala.concurrent.Future
import play.api.mvc.Results.BadRequest



object Global extends  GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }



  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] =  {
    println("onError " +ex.getMessage)
    if(ex.getMessage.contains("PsException"))  {
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

/** Initial set of data to be imported into the sample application. */
object InitialData {

  val sdf = new SimpleDateFormat("yyyy-MM-dd")


  def insert(): Unit = {
    println("insert Users")
    DB.withSession {
      implicit s: Session =>
        if (Users.count == 0) {
          Elastic.esClient.execute { deleteIndex(Elastic. defaultIndex) }
          Elastic.createIndex
          Seq(
            User(username = "user1", password = "secret"),
            User(username = "user2", password = "secret"),
            User(username = "user3", password = "secret"),
            User(username = "user4", password = "secret"),
            User(username = "user5", password = "secret"),
            User(username = "user6", password = "secret")
          ).foreach(Users.insert)

          val newFile = new File("/home/bangnv/Desktop/data1.csv")
          val reader = CSVReader.open(newFile)
          reader.foreach(fields => {
            val profile = models.Profile(firstName = fields(0), lastName = fields(1),
              email = fields(2), phone = fields(3), address = fields(4)
            )
            import play.api.db.slick.Config.driver.simple._
            Profiles.profiles.insert(profile)
            Elastic.esClient.sync.execute {
              ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData
            }

          })
        }
        // create profile

    }
  }
}