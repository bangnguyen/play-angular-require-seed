package controllers


import prosource.core.search.{Elastic, SearchApi}
import prosource.core.search.Elastic._
import secure.Secured
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import utils.JsonHelper._

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.Reads._
import utils.FuncResult._
import java.util.{UUID, Date}
import utils.Position
import utils.Position.Position
import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.ElasticDsl._
import utils.Constant._
import utils.CustomValidate._

/**
 * Created by Marco Chu on 5/30/14.
 */
object Profiles extends Controller with Secured {


  /** Retrieves the user for the given id as JSON */
  def get(id: String) = Action(parse.empty) {
    request =>
      println("get Profile id : "+ mapToJson( models.Profiles.get(id).getData()))
    // TODO Find user and convert to JSON

      Ok(mapToJson( models.Profiles.get(id).getData()))
  }

  /** Creates a user from the given JSON */
  def create() = Action(parse.json) {
    implicit request =>
      val userReads = (
        (__ \ 'email).read[String](profileEmailNotExisted) and
          (__ \ 'phone).readNullable[String](profilePhoneNotExisted).map[String](v => v.getOrElse("").trim) and
          (__ \ 'firstName).read[String](minLength[String](1)) and
          (__ \ 'lastName).read[String] and
          (__ \ 'address).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'position).readNullable[String].map[Position](s =>
            s.map(value => Position.withName(value)).getOrElse(Position.Student)) and
          (__ \ 'birthday).readNullable[Long].map[Date](s =>
            s.map(value => new Date(value)).getOrElse(new Date))
        ) tupled
       var result = Ok("")
         userReads.reads(request.body).fold(
        invalid = {
          errors =>
            result =  BadRequest(mapToJson(Map("status" -> "KO", "errors" -> errorsToJson(errors))))
        },
        valid = {
          data =>
            val profile = models.Profile.tupled(data)
            models.Profiles.put(profile)
            Elastic.esClient.sync.execute {
              ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData(forEs)
            }
            result =   Ok("")
        }
      )
      result
  }

  def update(id: String) = Action(parse.json) {
    implicit request =>
      val userReads = (
        (__ \ 'email).read[String] and
          (__ \ 'phone).readNullable[String].map[String](v => v.getOrElse("").trim) and
          (__ \ 'firstName).read[String](minLength[String](1)) and
          (__ \ 'lastName).read[String] and
          (__ \ 'address).readNullable[String].map[String](v => v.getOrElse("")) and
          (__ \ 'position).readNullable[String].map[Position](s =>
            s.map(value => Position.withName(value)).getOrElse(Position.Student)) and
          (__ \ 'birthday).readNullable[Long].map[Date](s =>
            s.map(value => new Date(value)).getOrElse(new Date))
        // (__ \ 'coursesId).readNullable[List[String]]
        ) tupled
      var result = Ok("")
      val callResult = userReads.reads(request.body).fold(
        invalid = {
          errors =>
            result = BadRequest(mapToJson(Map("status" -> "KO", "errors" -> errorsToJson(errors))))
        },
        valid = {
          data =>
            val profile = models.Profile.tupled(data)
            profile.id = id
            val coursesId =  request.body.\("coursesId").asInstanceOf[JsArray].value.foldLeft(List[String]())((r,u) =>
              r.::(u.asInstanceOf[JsString].value)
            )

            println( "1 " + coursesId )
            models.Profiles.put(profile)
            Elastic.esClient.sync.execute {
              ElasticDsl.index into(defaultIndex, profileType) id profile.id fields profile.getData(forEs)
            }
            result = Ok("")
        }
      )
      result
  }


  def deleteObject(id: String) = Action(parse.empty) {
    implicit request =>
      models.Profiles.delete(id)
      esClient.sync.execute {
        ElasticDsl.delete id id from url(profileType)
      }

      NoContent
  }


  def search(keyword: String, page: Int, pageSize: Int) = Action {
    implicit rs =>

      Ok(mapToJson(SearchApi.search(keyword, page, pageSize, profileType)))
  }


}