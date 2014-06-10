package controllers

import play.api.libs.json._
import secure.Secured
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import com.github.tototoshi.csv.CSVReader
import play.api.mvc.{Action, Controller}
import play.api.Routes
import db.FakeData

/** Application controller, handles authentication */
object Application extends Controller with Secured {

  /** Serves the index page, see views/index.scala.html */
  def index = Action {
    implicit request =>
      Ok(views.html.index())
  }

  /**
   * Returns the JavaScript router that the client can use for "type-safe" routes.
   * @param varName The name of the global variable, defaults to `jsRoutes`
   */
  def jsRoutes(varName: String = "jsRoutes") = Action {
    implicit request =>
      Ok(
        Routes.javascriptRouter(varName)(
          routes.javascript.Application.login,
          routes.javascript.Application.logout,
          routes.javascript.Application.uploadFile,
          controllers.routes.javascript.Users.get,
          controllers.routes.javascript.Users.create,
          controllers.routes.javascript.Users.update,
          controllers.routes.javascript.Users.delete,
          controllers.routes.javascript.Profiles.get,
          controllers.routes.javascript.Profiles.create,
          controllers.routes.javascript.Profiles.update,
          controllers.routes.javascript.Profiles.deleteProfile,
        //  controllers.routes.javascript.Profiles.list,
          controllers.routes.javascript.Courses.get,
          controllers.routes.javascript.Courses.create,
          controllers.routes.javascript.Courses.update,
          controllers.routes.javascript.Courses.delete,
          controllers.routes.javascript.Courses.list,
          controllers.routes.javascript.Search.searchProfilesByKeyword,
          controllers.routes.javascript.Search.getAllTeacher
          // TODO Add your routes here
        )
      ).as(JAVASCRIPT)
  }


  /**
   * Log-in a user. Pass the credentials as JSON body.
   * @return The token needed for subsequent requests
   */
  /*def login() = Action(parse.json) { implicit request =>
    // TODO Check credentials, log user in, return correct token
    val token = java.util.UUID.randomUUID().toString
    Ok(Json.obj("token" -> token))
  }*/
  def login = Action(parse.json) {
    implicit request =>
      val userReads = (
        (__ \ 'username).read[String](minLength[String](4)) and
          (__ \ 'password).read[String](minLength[String](4))
        ) tupled

      userReads.reads(request.body).fold(
        invalid = {
          errors =>

          //println("errors")
            new Status(-1)
        },
        valid = {
          res =>
            Ok(Json.obj("username" -> res._1)).withSession("username" -> res._1)
        }
      )
  }

  /** Logs the user out, i.e. invalidated the token. */
  def logout() = Action {
    // TODO Invalidate token, remove cookie
    Ok
  }

  def uploadFile = Action(parse.multipartFormData) {
    request =>
      println("uploadFile")
      request.body.file("file").map {
        picture =>
          import java.io.File
          val filename = picture.filename
          val contentType = picture.contentType
          val fileStore = new File("/tmp/picture")
          picture.ref.moveTo(fileStore, true)
          //val newFile = new File("/home/bangnv/Desktop/data1.xlsx")
          val newFile = new File("/home/bangnv/workspace/aox/play/sampleData/free-zipcode-database.csv")
          println(newFile.exists())
          val reader = CSVReader.open(fileStore)
          reader.foreach(fields => {
            println(fields)

          })
          Ok("File uploaded")
      }.getOrElse {
        Redirect(routes.Application.index).flashing(
          "error" -> "Missing file"
        )
      }
  }


  def list = Action {
    implicit rs =>

        Ok
      //Ok(listToJson(query.list()))
  }

  def initData = Action {
    request =>
      FakeData.initData()
      Ok("data init")
  }






}
