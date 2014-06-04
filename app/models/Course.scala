package models
import java.util.{UUID, Date}
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
/**
 * Created by Marco Chu on 5/21/14.
 */
case class Course(  id: String = UUID.randomUUID().toString ,
                    code: String, title: String, level: String = "", comment: String = "")
  extends BaseEntity {

  override def getData: Map[String, Any] = Map(
    "id" ->id ,
    "code" -> code,
    "title" -> title,
    "level" -> level,
    "comment" -> comment
  )
}

 class Courses(tag: Tag)  extends Table[Course](tag, "COURSE") {

   def id = column[String]("id", O.PrimaryKey, O.NotNull)


   def code = column[String]("code", O.NotNull)

   def title = column[String]("title", O.NotNull)

   def level = column[String]("level", O.Nullable)

   def comment = column[String]("comment", O.Nullable)


   def * = (id, code, title, level, comment) <>(Course.tupled, Course.unapply _)

 }


object Courses {

  val courses = TableQuery[Courses]

  def findById(id: String)(implicit s: Session): Option[Course] =
    courses.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(courses.length).first

  def insert(course: Course)(implicit s: Session) {
    courses.insert(course)
  }

  def update(id: String, course: Course)(implicit s: Session) {
  /*  val courseUpdate: Course = course.copy(Some(id))
    courses.where(_.id === id).update(courseUpdate)*/
  }


  def delete(id: String)(implicit s: Session) {
    courses.where(_.id === id).delete
  }


}