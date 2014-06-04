package controllers


import play.api.libs.json._
import secure.Secured
import play.api.db.slick._
import play.api.mvc.Controller
import play.api.Play.current
import play.api.db.slick.DBAction
import utils.JsonHelper._
import search.Elastic._
import search.SearchApi

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
    request =>
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