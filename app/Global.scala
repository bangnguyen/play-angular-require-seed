import com.github.tototoshi.csv.CSVReader
import java.io.File
import java.text.SimpleDateFormat
import play.api._
import models._
import play.api.db.slick._
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
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
            Profiles.insert(profile)

          })
        }
        // create profile

    }
  }
}