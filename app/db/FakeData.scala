package db

import models._
import db.ColumnFamilyParams._
import prosource.core.search.Elastic
import com.sksamuel.elastic4s.ElasticDsl._
import java.text.SimpleDateFormat
import java.io.File
import com.github.tototoshi.csv.CSVReader
import com.sksamuel.elastic4s.ElasticDsl
import prosource.core.search.Elastic._
import models.User
import utils.Position
import utils.Constant._
import java.util.UUID

/**
 * Created by Marco Chu on 6/10/14.
 */

/** Initial set of data to be imported into the sample application. */
object FakeData {

  def initData() = {
    println("initData")
    Cassandra.dropKeySpace
    Thread.sleep(2000)
    Cassandra.createKeyspace
    Users.createStorage(null)
    Profiles.createStorage(profileOptions)
    Courses.createStorage(courseOptions)
    CourseStudents.createStorage(courseStudents)
    Elastic.esClient.execute {
      deleteIndex(Elastic.defaultIndex)
    }
    Elastic.createIndex
    insert()
  }

  val sdf = new SimpleDateFormat("yyyy-MM-dd")


  def insert(): Unit = {
    //insert User
    Seq(
      User(username = "user1", password = "secret"),
      User(username = "user2", password = "secret"),
      User(username = "user3", password = "secret"),
      User(username = "user4", password = "secret"),
      User(username = "user5", password = "secret"),
      User(username = "user6", password = "secret")
    ).foreach(Users.put(_))

    //insert Profile


    val newFile = new File(String.format(".%sdata1.csv",File.separator))
    val reader = CSVReader.open(newFile)
    reader.foreach(fields => {
      val profile = models.Profile(firstName = fields(0), lastName = fields(1),
        email = fields(2), phone = fields(3), address = fields(4)
      )
      Profiles.put(profile)
      Elastic.esClient.sync.execute {
        ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData(forEs)
      }
    })

    println("-------------------")

    val profile1 = models.Profile(firstName = "Nguyễn", lastName = "minh anh",
      email = "nguyenminhanh@yahoo.com", phone = "0988712993", address = "31 hang buom", position = Position.Teacher
    )
    Profiles.put(profile1)

    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile1.id fields profile1.getData(forEs)
    }
    val profile2 = models.Profile(firstName = "phạm", lastName = " huy hùng",
      email = "phamhuyhung@gmail.com", phone = "0988787657", address = "30 hang duong", position = Position.Teacher
    )
    Profiles.put(profile2)
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile2.id fields profile2.getData(forEs)
    }

    val profile3 = models.Profile(firstName = "nguyễn", lastName = " hồng ngọc",
      email = "nguyenhongngoc@yahoo.co.uk", phone = "0977230234", address = "30 hang phen", position = Position.Teacher
    )
    Profiles.put(profile3)
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile3.id fields profile3.getData(forEs)
    }

    // create course
    val course1 = Course(id = "ielt1",title = "Ielt communication", teacher1 = profile1.id, days = "monday, tuesday", hours ="14h-16h",
    price=23000000,room="room1")
    val course2 = Course(id = "tofle",title = "Tofl communication", teacher1 = profile2.id, days = "monday, tuesday", hours ="14h-16h",
      price=300000000,room="room1")
    val course3 = Course(id = "tofx",title = "tofx communication", teacher1 = profile3.id, days = "monday, tuesday", hours ="14h-16h",
      price=23000000,room="room1")
    Courses.put(List(course1,course2,course3))

    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, courseType) id course1.getId fields course1.getData(forEs)
    }
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, courseType) id course2.getId fields course2.getData(forEs)
    }
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, courseType) id course3.getId fields course3.getData(forEs)
    }












  }
}