package controllers

import play.api.mvc.{Action, Controller}
import prosource.core.search.Elastic._
import prosource.core.search.SearchApi
import utils.JsonHelper._

/**
 * Created by Marco Chu on 6/9/14.
 */
object Search  extends Controller {


  def searchProfilesByKeyword(keyword: String, page: Int, pageSize: Int) = Action {
    implicit rs =>
      Ok(mapToJson(SearchApi.search(keyword, page, pageSize,profileType)))
  }

  def getAllTeachers() = Action {
    implicit rs =>
      Ok(mapToJson(SearchApi.getAllTeachers()))
  }
  def searchAllCourses() = Action {
    implicit rs =>
      Ok(mapToJson(SearchApi.searchAllCourses()))
  }

  def searchAllOpenCourses() = Action {
    implicit rs =>
      Ok(mapToJson(SearchApi.searchAllOpenCourses()))
  }

}
