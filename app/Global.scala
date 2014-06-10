import _root_.db.Cassandra
import com.github.tototoshi.csv.CSVReader
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._
import java.io.File
import java.text.SimpleDateFormat
import play.api._
import models._
import play.api.libs.json.Json
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
   /* Cassandra.firstTime
    InitialData.insert()*/
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

/** Initial set of data to be imported into the sample application. */
object InitialData {

  val sdf = new SimpleDateFormat("yyyy-MM-dd")



  def insert(): Unit = {

    if (Users.getAll.length == 0) {
      /*Elastic.esClient.execute { deleteIndex(Elastic. defaultIndex) }
      Elastic.createIndex*/
      Seq(
        User(username = "user1", password = "secret"),
        User(username = "user2", password = "secret"),
        User(username = "user3", password = "secret"),
        User(username = "user4", password = "secret"),
        User(username = "user5", password = "secret"),
        User(username = "user6", password = "secret")
      ).foreach(Users.put(_))
    }

    if (Profiles.getAll.length == 0) {
      val newFile = new File("/home/bangnv/Desktop/data1.csv")
      val reader = CSVReader.open(newFile)
      reader.foreach(fields => {
        val profile = models.Profile(firstName = fields(0), lastName = fields(1),
          email = fields(2), phone = fields(3), address = fields(4)
        )
        //create profile for teacher


        Profiles.put(profile)
          Elastic.esClient.sync.execute {
            ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData
          }
      })


    }
    println("-------------------")

    val profile1 = models.Profile(firstName = "Nguyễn", lastName = "minh anh",
      email = "nguyenminhanh@yahoo.com", phone = "0988712993", address = "31 hang buom",position =  Position.Teacher
    )
    Profiles.put(profile1)

    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile1.id fields profile1.getData
    }
    val profile2 = models.Profile(firstName = "phạm", lastName = " huy hùng",
      email = "phamhuyhung@gmail.com", phone = "0988787657", address = "30 hang duong",position =  Position.Teacher
    )
    Profiles.put(profile2)
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile2.id fields profile2.getData
    }

    val profile3 = models.Profile(firstName = "nguyễn", lastName = " hồng ngọc",
      email = "nguyenhongngoc@yahoo.co.uk", phone = "0977230234", address = "30 hang phen",position =  Position.Teacher
    )
    Profiles.put(profile3)
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile3.id fields profile3.getData
    }



  }
}