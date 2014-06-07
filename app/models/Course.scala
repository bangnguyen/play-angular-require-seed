package models
import java.util.{UUID, Date}
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import utils.JsonHelper._
import utils.Level
import utils.Level.Level

/**
 * Created by Marco Chu on 5/21/14.
 */
case class Course(id: String = UUID.randomUUID().toString ,
                  code: String ="",
                  title: String ="",
                  level: Level = Level.Beginner,
                  teacher1 : String ="",
                  teacher2 :String ="",
                  comment: String = "",
                  start : Date = new Date(),
                  finish: Date = new Date(),
                  isOpen : Boolean = true,
                  days : String = "",
                  hours : String = "" ,
                  price :  Integer = 0 ,
                  discount : Double = 0.0,
                  room  : String =""

                   )
  extends BaseEntity {



  override def getData: Map[String, Any] = Map(
    "id" ->id ,
    "code" -> code,
    "title" -> title,
    "teacher1" -> teacher1 ,
    "teacher2" -> teacher2 ,
    "level" -> level.toString,
    "comment" -> comment,
    "start" ->start,
    "finish" -> finish ,
    "isOpen" -> isOpen,
    "days" -> days,
    "hours" ->hours
  )
}

 class Courses(tag: Tag)  extends Table[Course](tag, "COURSE") {

   implicit val clType1 = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))
   implicit val clType2 = MappedColumnType.base[Level, String](v1 => v1.toString, v1 => Level.withName(v1))
   def id = column[String]("id", O.PrimaryKey, O.NotNull)
   def code = column[String]("code", O.Nullable)
   def title = column[String]("title", O.Nullable)
   def level = column[Level]("level", O.Nullable)
   def teacher1 = column[String]("teacher1", O.Nullable)
   def teacher2 = column[String]("teacher2", O.Nullable)
   def comment = column[String]("comment", O.Nullable)
   def start = column[Date]("start" , O.Nullable)
   def finish = column[Date]("finish" , O.Nullable)
   def isOpen = column[Boolean]("isOpen" , O.Nullable)
   def days = column[String]("days" , O.Nullable)
   def hours = column[String]("hours" , O.Nullable)


   def * = (id, code, title, level, teacher1, teacher2,comment,start,finish,isOpen,days,hours) <>(Course.tupled, Course.unapply _)

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