package models


import java.util.UUID
import utils.Constant
import javax.persistence.{Entity, Id, Column}
import scala.annotation.meta.field

/**
 * Created by Marco Chu on 6/6/14.
 */

@Entity
case class CourseStudent(
                          @(Id@field) @(Column@field) (name="key")  id: String = UUID.randomUUID().toString,
                          @(Column@field)    courseId: String = "",
                          @(Column@field)   studentId: String = "",
                          @(Column@field)     isTuitionFeePaid: Boolean = false,
                          @(Column@field)    isBookFeePaid: Boolean = false,
                          @(Column@field)    comment: String = " ",
                          @(Column@field)    isReserved: Boolean = false,
                          @(Column@field)    tuitionFee: Integer = 0,
                          @(Column@field)    bookFee: Integer = 0,
                          @(Column@field)    salesStaffId: String = "",
                          @(Column@field)    consultantId: String = ""
                          ) extends BaseEntity {

  def this() = this(null)
  override def getId: String = id
  override def getData: Map[String, Any] = Map(
    "id" -> id,
    "courseId" -> courseId,
    "studentId" -> studentId,
    "isTuitionFeePaid" -> isTuitionFeePaid,
    "isBookFeePaid" -> isBookFeePaid,
    "comment" -> comment,
    "isReserved" -> isReserved,
    "tuitionFee" -> tuitionFee,
    "bookFee" -> bookFee,
    "salesStaffId" -> salesStaffId,
    "consultantId" -> consultantId
  )
}

object CourseStudents extends CassandraDAO[CourseStudent, String](classOf[CourseStudent], Constant.defaultEntityManager)


