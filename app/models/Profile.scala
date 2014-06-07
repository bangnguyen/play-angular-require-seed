package models

import java.util.{UUID, Date}
import play.api.db.slick.Config.driver.simple._
import prosource.core.search.Elastic
import prosource.core.search.Elastic._
import scala.slick.lifted.Tag
import com.sksamuel.elastic4s.{IndexesTypes, ElasticDsl}
import utils.Position
import utils.Position.Position
import utils.JsonHelper._
import exception.PsException


case class Profile(
                    id: String = UUID.randomUUID().toString ,
                    email: String= "",
                    phone: String= "",
                    firstName: String = "",
                    lastName: String = "",
                    address: String = "",
                    position : Position = Position.Student,
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
    "birthday" -> birthday,
    "created" -> created,
    "position" -> position.toString
  )
}


class Profiles(tag: Tag) extends Table[Profile](tag, "PROFILE") {
  implicit val clType1 = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))
  implicit val clType2 = MappedColumnType.base[Position, String](v1 => v1.toString, v1 => Position.withName(v1))
  def id = column[String]("id", O.PrimaryKey, O.NotNull)


  def firstName = column[String]("firstName", O.NotNull)

  def lastName = column[String]("lastName", O.NotNull)

  def email = column[String]("email", O.NotNull)

  def phone = column[String]("phone", O.NotNull)

  def address = column[String]("address", O.Nullable)

  def birthday = column[Date]("birthday", O.Nullable)

  def created = column[Date]("created", O.Nullable)

  def position = column[Position]("position",O.Nullable)

  def * = (id, email, phone, firstName, lastName, address,position, birthday, created) <>(Profile.tupled, Profile.unapply _)

}

object Profiles {

  val profiles = TableQuery[Profiles]

  def findById(id: String)(implicit s: Session): Option[Profile] =
    profiles.where(_.id === id).firstOption

  def findByEmail(email: String)(implicit s: Session): Option[Profile] =
    profiles.where(_.email.toLowerCase === email.toLowerCase()).firstOption

  def findByPhone(phone: String)(implicit s: Session): Option[Profile] =
    profiles.where(_.phone.toLowerCase === phone.toLowerCase()).firstOption

  def count(implicit s: Session): Int =
    Query(profiles.length).first

  def insert(profile: Profile)(implicit s: Session) {
    println(profile.email)
    println(findByEmail(profile.email))
    if(!findByEmail(profile.email).equals(None) ) {
      println("1")
      throw  new PsException(String.format("Can't create a profile. this email %s existed already",profile.email))
    }
    if(!findByPhone(profile.phone).equals(None)) {
      throw  new PsException(String.format("Can't create a profile. this phone %s existed already",profile.phone))
    }
    println("2")
    profiles.insert(profile)
    Elastic.esClient.sync.execute {
      ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData
    }


  }

  def update(id: String, profile: Profile)(implicit s: Session) {
    val profileUpdate: Profile = profile.copy(id)
    profiles.where(_.id === id).update(profileUpdate)
    Elastic.esClient.sync.execute {
      ElasticDsl.update(profile.id) in IndexesTypes(defaultIndex, profileType) doc(profile.getData)
    }
  }


  def delete(id: String)(implicit s: Session) {
    profiles.where(_.id === id).delete
    esClient.sync.execute {
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
