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

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}
