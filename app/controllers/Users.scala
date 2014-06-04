package controllers

import play.api.mvc._
import play.api.libs.json._
import secure.Secured

//import models.User

/** Example controller; see conf/routes for the the mapping to routes */
object Users extends Controller with Secured {

  /** Retrieves the user for the given id as JSON */
  def get(id: String) = Action(parse.empty) { request =>
    // TODO Find user and convert to JSON
    Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = Action(parse.json) { request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    Ok
  }

  /** Updates the user for the given id from the JSON body */
  def update(id: String) = Action(parse.json) { request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    Ok
  }

  /** Deletes a user for the given id */
  def delete(id: String) = Action(parse.empty) { request =>
    // TODO Implement User creation, typically via request.body.validate[User]
    Ok
  }

}
