package models

import play.api.libs.json.JsValue
import utils.JsonHelper._

/**
 * Created by Marco Chu on 5/24/14.
 */
trait BaseEntity {
  def getData : Map[String,Any]
  def toJson : JsValue =  anyToJson(getData)
}
