package models


import java.util.UUID
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

/**
 * Created by Marco Chu on 6/6/14.
 */
case class CourseStudent(
                          id: String = UUID.randomUUID().toString,
                          courseId: String = "",
                          studentId: String = "",
                          isTuitionFeePaid: Boolean = false,
                          isBookFeePaid : Boolean =  false,
                          comment: String = " " ,
                          isReserved : Boolean = false,
                          tuitionFee : Integer = 0 ,
                          bookFee : Integer = 0 ,
                          salesStaffId : String ="",
                          consultantId : String = "",








                          ) extends BaseEntity {


  override def getData: Map[String, Any] = Map(
    "id" -> id,
    "courseId" -> courseId,
    "studentId" -> studentId,
    "isPaid" -> isPaid,
    "comment" -> comment

  )
}

class CourseStudents(tag: Tag) extends Table[CourseStudent](tag, "CourseStudent") {

  def id = column[String]("id", O.PrimaryKey, O.NotNull)

  def courseId = column[String]("courseId", O.NotNull)

  def studentId = column[String]("courseId", O.NotNull)

  def isPaid = column[Boolean]("isPaid", O.Nullable)

  def comment = column[String]("comment", O.Nullable)


  def * = (id, courseId, studentId, isPaid, comment) <>(CourseStudent.tupled, CourseStudent.unapply _)

}


object CourseStudents {

  val courseStudents = TableQuery[CourseStudents]

  def findById(id: String)(implicit s: Session): Option[CourseStudent] =
    courseStudents.where(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(courseStudents.length).first

  def insert(entity: CourseStudent)(implicit s: Session) {
    courseStudents.insert(entity)
  }

  def update(id: String, entity: CourseStudent)(implicit s: Session) {

  }
  def delete(id: String)(implicit s: Session) {
    courseStudents.where(_.id === id).delete
  }


}
