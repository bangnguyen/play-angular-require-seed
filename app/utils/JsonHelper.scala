package utils

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.Reads._


import models._
import scala.collection.Seq
import play.api.libs.json.JsArray
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsString
import play.api.libs.json.JsBoolean
import play.api.data.validation.ValidationError
import play.api.libs.json.JsNumber
import java.text.SimpleDateFormat


trait JsonHelper {
  import scala.language.postfixOps

  val updateReads = (
    (JsPath \ "name").readNullable[String] and
      (JsPath \ "name_raw").readNullable[String] and
      (JsPath \ "employer").readNullable[String] and
      (JsPath \ "metro").readNullable[String] and
      (JsPath \ "uri").readNullable[String] and
      (JsPath \ "desc").readNullable[String] and
      (JsPath \ "alternate_titles").readNullable[String] and
      (JsPath \ "alternate_names").readNullable[String] and
      (JsPath \ "entity_status").readNullable[String] and
      (JsPath \ "curation_status").readNullable[String]
    ) tupled

  implicit val relatedReads = (
    (__ \ 'algo).readNullable[String] and
      (__ \ 'data).read[List[(String, Double)]]
    ) tupled

  val settingReads = (
    (__ \ 'cassandra \ 'storage).readNullable[String] and
      (__ \ 'cassandra \ 'local \ 'dir).readNullable[String] and
      (__ \ 'cassandra \ 'network \ 'hostname).readNullable[String] and
      (__ \ 'cassandra \ 'network \ 'keyspace).readNullable[String] and
      (__ \ 'es \ 'storage).readNullable[String] and
      (__ \ 'es \ "local" \ 'dir).readNullable[String] and
      (__ \ 'es \ "network" \ 'hostname).readNullable[String] and
      (__ \ 'es \ "network" \ 'cluster).readNullable[String] and
      (__ \ 'es \ "network" \ 'index_name).readNullable[String]
    ) tupled

  val paramsIndexReads = (
    (__ \ 'entity).read[String] and
      (__ \ 'ids).readNullable[List[String]]
    ) tupled

  implicit def listTupleReads : Reads[List[(String, Double)]] = new Reads[List[(String, Double)]] {
    def reads(json: JsValue) = json match {
      case JsArray(ts) => {
        var hasErrors = false

        val r = ts.zipWithIndex.map {
          case (item, idx) => {
            (Json.fromJson[String](item \ "id"), Json.fromJson[Double](item \ "weight")) match {
              case (JsSuccess(id, _), JsSuccess(weight, _)) => Right((id, weight))
              case (JsError(id), JsSuccess(weight, _)) =>
                hasErrors = true
                Left(id.map { case (p, valerr) => (JsPath(idx)) ++ p -> valerr })
              case (JsSuccess(id, _), JsError(weight)) =>
                hasErrors = true
                Left(weight.map { case (p, valerr) => (JsPath(idx)) ++ p -> valerr })
              case (JsError(id), JsError(weight)) =>
                hasErrors = true
                Left(id.map { case (p, valerr) => (JsPath(idx)) ++ p -> valerr } ++ weight.map { case (p, valerr) => (JsPath(idx)) ++ p -> valerr })
            }
          }
        }

        if (hasErrors) {
          val fulle = r.filter(_.isLeft).map(_.left.get)
            .foldLeft(List[(JsPath, Seq[ValidationError])]())((acc, v) => (acc ++ v))
          JsError(fulle)
        } // no error, rebuilds the map
        else {
          JsSuccess(r.map(_.right.get).toList)
        }
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsarray"))))
    }
  }
}

object JsonHelper extends JsonHelper {

  val simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy")

  def anyToJson(value: Any): JsValue = {
    type mapStringAny = Map[String, Any]
    var result: JsValue = null
    if (value == null) {
      result = JsNull
    } else {
      if (value.isInstanceOf[BaseEntity]) {
        result = mapToJson(value.asInstanceOf[BaseEntity].getData)
      } else if (value.isInstanceOf[JsValue]) {
        result = value.asInstanceOf[JsValue]
      }
      else {
        val className = value.getClass.getSimpleName
        className match {
          case "String" => {
            val temps = value.asInstanceOf[String]
            try {
              result = Json.parse(temps)
            } catch {
              case e: Exception => result = JsString(temps)
            }
          }
          case "Integer" => {
            result = JsNumber(value.asInstanceOf[Integer].toLong)
          }
          case "Short" => {
            result = JsNumber(value.asInstanceOf[Short].toLong)
          }
          case "Boolean" => {
            result = JsBoolean(value.asInstanceOf[Boolean])
          }
          case "Long" => {
            result = JsNumber(value.asInstanceOf[Long])
          }
          /*    case "JsArray" => {
                result = value.asInstanceOf[JsArray]
              }*/
          case _ => {
            if (value.isInstanceOf[mapStringAny]) {
              result = mapToJson(value.asInstanceOf[Map[String, Any]])
            } else if (value.isInstanceOf[List[Any]]) {
              result = listToJson(value.asInstanceOf[List[Any]])

            } else {
              result = JsString(value.toString)
            }
          }
        }
      }
    }
    result
  }

  def mapToJson(data: Map[String, Any]): JsValue = {
    if (data != null) {
      toJson(data.foldLeft(Map[String, JsValue]())((r, e) => {
        r.+(e._1 -> anyToJson(e._2))
      }))
    }
    else JsString("")
  }



  def listToJson(list: List[Any]): JsValue = {
    JsArray(list.foldLeft(List[JsValue]())((r,e)=>r.::(anyToJson((e)))))
  }



}
