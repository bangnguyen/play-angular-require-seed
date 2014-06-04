package controllers
import play.api.mvc._
import play.api.libs.json._
import secure.Secured
import play.api.db.slick.DBAction

//import models.User

/** Example controller; see conf/routes for the the mapping to routes */
object Courses extends Controller with Secured {

  /** Retrieves the user for the given id as JSON */
  def get(id: String) = DBAction(parse.empty) { request =>
  // TODO Find user and convert to JSON
    Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = DBAction(parse.json) { request =>
    Ok
  }

  def update(id: String) = DBAction(parse.json) { request =>
    Ok
  }

  def delete(id: String) = DBAction(parse.empty) { request =>
    Ok
  }


  def list = DBAction {
    request =>
      Ok
  }

}
