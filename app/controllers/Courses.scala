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
object Courses extends Controller with Secured {

  /** Retrieves the user for the given id as JSON */
  def get(id: String) = Action(parse.empty) {
    request =>
    // TODO Find user and convert to JSON
      Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = Action(parse.json) {
    request =>
      val userReads = (
          (__ \ 'id).read[String](minLength[String](1)) and
          (__ \ 'title).read[String](minLength[String](1)) and
          (__ \ 'level).read[String].map[Level](v => Level.withName(v)) and
          (__ \ 'isOpen).readNullable[Boolean].map[Boolean](v => v.getOrElse(true)) and
          (__ \ 'teacher1).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'teacher2).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'comment).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'start).readNullable[Long].map[Date](s =>
            s.map(value => new Date(value)).getOrElse(new Date)) and
          (__ \ 'finish).readNullable[Long].map[Date](s =>
            s.map(value => new Date(value)).getOrElse(new Date)) and
          (__ \ 'days).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'hours).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'price).read[Int]  and
          (__ \ 'discount).read[String].map[Double](v => v.toDouble )  and
          (__ \ 'room).readNullable[String].map[String](v => v.getOrElse(""))
        ) tupled

      val body = request.body.as[JsObject]
     var result = Ok("")
     println("create course "+ 1)
     userReads.reads(body).fold(
        invalid = {
          errors =>
            println(request.body.as[JsObject] )
            println("errors "+ errors)
            result =  BadRequest(mapToJson(Map("status" -> "KO", "errors" -> errorsToJson(errors))))
        },
        valid = {
          data =>
            val course =  models.Course.tupled(data)
            println("thanh cong roi " + course)
            models.Courses.put(course)
            Elastic.esClient.sync.execute {
              ElasticDsl.index into(defaultIndex, courseType) id course.getId fields course.getData(forEs)
            }
            result =   Ok("")
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
