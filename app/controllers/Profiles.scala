package controllers


import prosource.core.search.SearchApi
import prosource.core.search.Elastic._
import secure.Secured
import play.api.db.slick._
import play.api.mvc.Controller
import play.api.Play.current
import play.api.db.slick.DBAction
import utils.JsonHelper._

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.Reads._
import utils.FuncResult._
import java.util.Date

/**
 * Created by Marco Chu on 5/30/14.
 */
object Profiles extends Controller with Secured {


  /** Retrieves the user for the given id as JSON */
  def get(id: String) = DBAction(parse.empty) {
    request =>
    // TODO Find user and convert to JSON
      Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = DBAction(parse.json) {
    implicit request =>
      val userReads = (
        (__ \ 'firstName).read[String](minLength[String](1)) and
          (__ \ 'lastName).read[String](minLength[String](1)) and
          (__ \ 'phone).read[String] (minLength[String](1)) and
          (__ \ 'email).readNullable[String] and
          (__ \ 'address).readNullable[String] and
          (__ \ 'birthday).readNullable[String].map[Date](s =>
            s.map(value => simpleDateFormat.parse(value)).getOrElse(null))
        ) tupled

      val callResult = userReads.reads(request.body).fold(
        invalid = { errors =>
          CALL_FAIL
        },
        valid = { data =>
          println(data)
          models.Profiles.insert(models.Profile(firstName = data._1, lastName = data._2,
            phone = data._3, email = data._4.getOrElse(""), address = data._5.getOrElse(""),
           birthday = data._6))
          CALL_SUCCESS
        }
      )
      Ok
  }

  def update(id: String) = DBAction(parse.json) {
    request =>
      Ok
  }


  def deleteProfile(id: String) = DBAction(parse.empty) {
    implicit request =>
      models.Profiles.delete(id)

      NoContent
  }


  def search(keyword: String, page: Int, pageSize: Int) = DBAction {
    implicit rs =>

      Ok(mapToJson(SearchApi.search(keyword, page, pageSize,profileType)))
  }

}