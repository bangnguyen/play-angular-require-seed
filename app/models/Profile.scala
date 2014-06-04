package models

import java.util.{UUID, Date}
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import models.Profile
import com.sksamuel.elastic4s.{IndexesTypes, ElasticDsl}
import search.Elastic._
import search.{ElasticDataHelper, Elastic}
import utils.JsonHelper._
import models.Profile
import play.api.db.slick.DBAction


case class Profile(
                    id: String = UUID.randomUUID().toString ,
                    email: String,
                    phone: String,
                    firstName: String = "",
                    lastName: String = "",
                    address: String = "",
                    birthday: Date = new Date(),
                    created: Date = new Date()
                    ) extends BaseEntity {
  var fullName = firstName + " " + lastName

  override def getData: Map[String, Any] = Map(
    "id" -> id,
    "fullName" -> fullName,
    "email" -> email,
    "phone" -> phone,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "address" -> address,
    "birthday" -> birthday
  )
}


class Profiles(tag: Tag) extends Table[Profile](tag, "PROFILE") {
  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

  def id = column[String]("id", O.PrimaryKey, O.NotNull)


  def firstName = column[String]("firstName", O.Nullable)

  def lastName = column[String]("lastName", O.Nullable)

  def email = column[String]("email", O.NotNull)

  def phone = column[String]("phone", O.NotNull)

  def address = column[String]("address", O.Nullable)

  def birthday = column[Date]("birthday", O.Nullable)

  def created = column[Date]("created", O.Nullable)

  def * = (id, email, phone, firstName, lastName, address, birthday, created) <>(Profile.tupled, Profile.unapply _)

}

object Profiles {

  val profiles = TableQuery[Profiles]

  def findById(id: String)(implicit s: Session): Option[Profile] =
    profiles.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(profiles.length).first

  def insert(profile: Profile)(implicit s: Session) {
    profiles.insert(profile)
    Elastic.client.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData
    }
  }

  def update(id: String, profile: Profile)(implicit s: Session) {
    val profileUpdate: Profile = profile.copy(id)
    profiles.where(_.id === id).update(profileUpdate)
    Elastic.client.sync.execute {
      ElasticDsl.update(profile.id) in IndexesTypes(defaultIndex, profileType) doc(profile.getData)
    }
  }


  def delete(id: String)(implicit s: Session) {
    profiles.where(_.id === id).delete
    client.sync.execute {
      ElasticDsl.delete id id from url(profileType)
    }
  }

  def list()(implicit s: Session)  {
    //Query(profiles) foreach { case (id, email, phone,firstName, lastName, address, birthday, created ) =>println(phone) }
    val r =  for (p <- profiles) yield (p.id, p.firstName)
    r.foreach(p=>{
      println(p._1+" "+p._2)
    })
    println("sfsf "+r)


  }




}
