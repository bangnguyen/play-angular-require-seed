package models
import java.util.{UUID, Date}
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import scala.util.Random

/**
 * Created by Marco Chu on 5/15/14.
 */
case class User(
                 id: String = UUID.randomUUID().toString ,
                username: String,
                password: String,
                created: Date = new Date()
                 )

class Users(tag: Tag) extends Table[User](tag,"USER") {
  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))
  def id = column[String]("id", O.PrimaryKey, O.NotNull)
  def username = column[String]("username", O.NotNull)
  def created = column[Date]("created", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def * = (id, username,password,created) <> (User.tupled, User.unapply _)

}

object Users {

  val users = TableQuery[Users]

  def findById(id: String)(implicit s: Session): Option[User] =
    users.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(users.length).first

  def insert(user: User)(implicit s: Session) {
    users.insert(user)
  }

  def update(id: String, user: User)(implicit s: Session) {
    val userToUpdate: User = user.copy(id)
    users.where(_.id === id).update(userToUpdate)
  }

  /**
   * Delete a computer
   * @param id
   */
  def delete(id: String)(implicit s: Session) {
    users.where(_.id === id).delete
  }

}