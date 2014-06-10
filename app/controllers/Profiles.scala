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
import java.util.{UUID, Date}
import utils.Position.Position
import utils.Position
import utils.Position.Position
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._

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
        (__ \ 'email).read[String](minLength[String](1)) and
          (__ \ 'phone).read[String](minLength[String](1)) and
          (__ \ 'firstName).read[String](minLength[String](1)) and
          (__ \ 'lastName).read[String] and
          (__ \ 'address).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'position).readNullable[String].map[Position](s =>
            s.map(value => Position.withName(value)).getOrElse(Position.Student)) and
          (__ \ 'birthday).readNullable[String].map[Date](s =>
            s.map(value => simpleDateFormat.parse(value)).getOrElse(new Date))
        ) tupled

      val callResult = userReads.reads(request.body).fold(
        invalid = {
          errors =>
            CALL_FAIL
        },
        valid = {
          data =>
            models.Profiles.put(models.Profile.tupled(data))
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
      esClient.sync.execute {
        ElasticDsl.delete id id from url(profileType)
      }

      NoContent
  }


  def search(keyword: String, page: Int, pageSize: Int) = DBAction {
    implicit rs =>

      Ok(mapToJson(SearchApi.search(keyword, page, pageSize, profileType)))
  }


}