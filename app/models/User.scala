package models

import java.util.{UUID, Date}
import utils.Constant
import javax.persistence.{Entity, Column, Id}
import scala.annotation.meta.field

import utils.Constant._
/**
 * Created by Marco Chu on 5/15/14.
 */
@Entity
case class User(
                 @(Id@field) @(Column@field)(name = "key") username: String,
                 @(Column@field) password: String,
                 @(Column@field) created: Date = new Date()
                 ) extends BaseEntity {
  override def getId: String = username

  def this() = this(null, null)

  override def getData(options : Int = forView): Map[String, Any] = ???
}


object Users extends CassandraDAO[User, String](classOf[User], Constant.defaultEntityManager)