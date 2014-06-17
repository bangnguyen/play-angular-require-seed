package models
import java.util.{UUID, Date}
import utils.{Constant, Level}
import utils.Level.Level
import javax.persistence.{Entity, Column, Id}
import scala.annotation.meta.field
import utils.JsonHelper._
import models.Course
import utils.Constant._
import models.Course
import models.Course

/**
 * Created by Marco Chu on 5/21/14.
 */

@Entity
case class Course(@(Id@field) @(Column@field)(name = "key")   id: String ="",
                  @(Column@field)  title: String ="",
                  @(Column@field)  level: Level = Level.Beginner,
                  @(Column@field)  isOpen : Boolean = true,
                  @(Column@field)  teacher1 : String = "",
                  @(Column@field)  teacher2 :String = "",
                  @(Column@field)  comment: String = "",
                  @(Column@field)  start :  Date = new Date(),
                  @(Column@field)  finish:  Date = new Date(),
                  @(Column@field)  days : String = "",
                  @(Column@field)  hours : String = "" ,
                  @(Column@field)  price :  Int = 0  ,
                  @(Column@field)  discount :Double = 0.0,
                  @(Column@field)  room  :  String = ""
                   )
  extends BaseEntity {

  def this() = this(null)
  override def getId: String = id
  override def getData(option : Int = forView): Map[String, Any] = Map(
    "id" -> id,
    "title" -> title,
    "level" -> level.toString,
    "teacher1" -> teacher1 ,
    "teacher2" -> teacher2 ,
    "comment" -> comment,
    "start" ->(if (option == forView)  start.getTime else start) ,
    "finish" -> (if (option == forView)  finish.getTime else finish)  ,
    "isOpen" -> isOpen,
    "days" -> days,
    "hours" ->hours,
     "price" ->price,
    "discount" ->discount,
    "room" -> room
  )
}
object Courses extends CassandraDAO[Course, String](classOf[Course], Constant.defaultEntityManager)