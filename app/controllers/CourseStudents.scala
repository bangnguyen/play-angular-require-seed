package controllers

import play.api.mvc._
import play.api.libs.json._
import prosource.core.search.Elastic
import prosource.core.search.Elastic._
import secure.Secured
import play.api.libs.json.Reads._
import java.util.{UUID, Date}
import utils.JsonHelper._
import utils.FuncResult._
import play.api.libs.functional.syntax._
import utils.Level.Level
import utils.Level
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import com.sksamuel.elastic4s.ElasticDsl
import utils.Constant._
import utils.CustomValidate._


//import models.User

/** Example controller; see conf/routes for the the mapping to routes */
object CourseStudents extends Controller with Secured {

  /** Retrieves the user for the given id as JSON */
  def get(id: String) = Action(parse.empty) {
    request =>
    // TODO Find user and convert to JSON
      Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = Action(parse.json) {
    request =>
      val reads = (
        (__ \ 'courseId).read[String] and
          (__ \ 'studentId).read[String] and
          (__ \ 'isTuitionFeePaid).readNullable[Boolean].map[Boolean](v => v.getOrElse(false)) and
          (__ \ 'isBookFeePaid).readNullable[Boolean].map[Boolean](v => v.getOrElse(false)) and
          (__ \ 'isReserved).readNullable[Boolean].map[Boolean](v => v.getOrElse(false)) and
          (__ \ 'comment).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'comment).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'tuitionFee).readNullable[Long].map[Long](s => s.getOrElse(0)) and
          (__ \ 'bookFee).readNullable[Long].map[Long](s => s.getOrElse(0)) and
          (__ \ 'salesStaffId).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'consultantId).readNullable[String].map[String](v => v.getOrElse(""))
        ) tupled

      val body = request.body.as[JsObject] + ("id" -> JsString(UUID.randomUUID().toString))
      var result = Ok("")
      println("create course " + 1)
      reads.reads(body).fold(
        invalid = {
          errors =>
            println(request.body.as[JsObject])
            println("errors " + errors)
            result = BadRequest(mapToJson(Map("status" -> "KO", "errors" -> errorsToJson(errors))))
        },
        valid = {
          data =>
            //val courseId = Coudata._1

            result = Ok("")
        }
      )
      result
  }

  def update(id: String) = Action(parse.json) {
    request =>
      Ok
  }

  def deleteObject(id: String) = Action(parse.empty) {
    request =>
      models.Courses.delete(id)
      esClient.sync.execute {
        ElasticDsl.delete id id from url(courseType)
      }
      NoContent
  }


  def list = Action {
    request =>
      Ok
  }

}
