package models

import java.util.Date
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag


case class Profile(
                    id: Option[Long] = None,
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
    "id" -> id.getOrElse(0),
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

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)


  def firstName = column[String]("firstName", O.Nullable)

  def lastName = column[String]("lastName", O.Nullable)

  def email = column[String]("email", O.NotNull)

  def phone = column[String]("phone", O.NotNull)

  def address = column[String]("address", O.Nullable)

  def birthday = column[Date]("birthday", O.Nullable)

  def created = column[Date]("created", O.Nullable)

  def * = (id.?, email, phone, firstName, lastName, address, birthday, created) <>(Profile.tupled, Profile.unapply _)

}

object Profiles {

  val profiles = TableQuery[Profiles]

  def findById(id: Long)(implicit s: Session): Option[Profile] =
    profiles.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(profiles.length).first

  def insert(profile: Profile)(implicit s: Session) {
    profiles.insert(profile)
  }

  def update(id: Long, profile: Profile)(implicit s: Session) {
    val profileUpdate: Profile = profile.copy(Some(id))
    profiles.where(_.id === id).update(profileUpdate)
  }


  def delete(id: Long)(implicit s: Session) {
    profiles.where(_.id === id).delete
  }

  /*def list()(implicit s: Session):List[Profiles] = {
    Query(profiles)
  }*/

}
