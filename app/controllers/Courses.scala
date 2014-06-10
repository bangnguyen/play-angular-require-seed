package controllers

import play.api.mvc._
import play.api.libs.json._
import secure.Secured
import play.api.db.slick.DBAction
import play.api.libs.json.Reads._
import java.util.{UUID, Date}
import utils.JsonHelper._
import utils.FuncResult._
import play.api.libs.functional.syntax._
import utils.Level
import utils.Level.Level
import utils.Level


//import models.User

/** Example controller; see conf/routes for the the mapping to routes */
object Courses extends Controller with Secured {

  /** Retrieves the user for the given id as JSON */
  def get(id: String) = DBAction(parse.empty) {
    request =>
    // TODO Find user and convert to JSON
      Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = DBAction(parse.json) {
    request =>
      val userReads = (
        (__ \ 'id).read[String] and
          (__ \ 'code).read[String](minLength[String](1)) and
          (__ \ 'title).read[String](minLength[String](1)) and
          (__ \ 'level).read[String].map[Level](v => Level.withName(v)) and
          (__ \ 'isOpen).read[Boolean] and
          (__ \ 'teacher1).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'teacher2).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'comment).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'start).readNullable[String].map[Date](s =>
            s.map(value => simpleDateFormat.parse(value)).getOrElse(new Date))      and
          (__ \ 'finish).readNullable[String].map[Date](s =>
            s.map(value => simpleDateFormat.parse(value)).getOrElse(new Date)) and
          (__ \ 'days).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'hours).readNullable[String].map[String](v => v.getOrElse(""))  and
          (__ \ 'price).readNullable[Int].map[Int](v => v.getOrElse(0))  and
          (__ \ 'discount).readNullable[Double].map[Double](v => v.getOrElse(0.0))  and
          (__ \ 'room).readNullable[String].map[String](v => v.getOrElse(""))
        ) tupled

      println(request.body.as[JsObject] )
      val body = request.body.as[JsObject] + ("id" -> JsString(UUID.randomUUID().toString))
      val callResult = userReads.reads(body).fold(
        invalid = {
          errors =>
            CALL_FAIL
        },
        valid = {
          data =>
            println(data)
            models.Course.tupled(data)
            /*   models.Courses.put(models.Course(
                code = data._1, title = data._2,level = data._3, teacher1 = data._4,
                 comment = data._5, start = data._6, finish = data._7, days = data._8,
                   hourse = data._9))*/
            CALL_SUCCESS
        }
      )
      Ok
  }

  def update(id: String) = DBAction(parse.json) {
    request =>
      Ok
  }

  def delete(id: String) = DBAction(parse.empty) {
    request =>
      Ok
  }


  def list = DBAction {
    request =>
      Ok
  }

}
