package controllers
import play.api.mvc._
import play.api.libs.json._
import secure.Secured
import play.api.db.slick.DBAction
import play.api.libs.json.Reads._
import java.util.Date
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
  def get(id: String) = DBAction(parse.empty) { request =>
  // TODO Find user and convert to JSON
    Ok(Json.obj("firstName" -> "John", "lastName" -> "Smith", "age" -> 42))
  }

  /** Creates a user from the given JSON */
  def create() = DBAction(parse.json) { request =>
    val userReads = (
      (__ \ 'code).read[String](minLength[String](1)) and
        (__ \ 'title).read[String](minLength[String](1)) and
        (__ \ 'level).readNullable[String].map[Level](v => v.map(s => Level.withName(s)).getOrElse(Level.Beginner))and
        (__ \ 'teacher1).readNullable[String] and
        (__ \ 'comment).readNullable[String] and
        (__ \ 'start).readNullable[String].map[Date](s =>
          s.map(value => simpleDateFormat.parse(value)).getOrElse(null))     and
        (__ \ 'finish).readNullable[String].map[Date](s =>
        s.map(value => simpleDateFormat.parse(value)).getOrElse(null))        and
        (__ \ 'days).readNullable[String] and
        (__ \ 'hours).readNullable[String]
      ) tupled

    val callResult = userReads.reads(request.body).fold(
      invalid = { errors =>
        CALL_FAIL
      },
      valid = { data =>
        println(data)
        models.Courses.insert(models.Course(
         code = data._1, title = data._2,level = data._3, teacher1 = data._4,
          comment = data._5, start = data._6, finish = data._7, days = data._8,
            hourse = data._9))
        CALL_SUCCESS
      }
    )
    Ok
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
