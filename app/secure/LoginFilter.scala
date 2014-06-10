/*
package secure

import play.api.mvc.{Results, RequestHeader, EssentialAction, EssentialFilter}
import play.api.libs.iteratee.Iteratee
import play.api.db.slick._
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global



/**
 * Created by Marco Chu on 6/6/14.
 */


object LoginFilter extends EssentialFilter {


  def apply(next: EssentialAction) = new EssentialAction {
    import  play.api.db.slick.Config.driver.simple.Session
    def apply(request: RequestHeader) = {
      DB.withSession {
        implicit s: Session =>
        println(request.path+ " "+request.queryString)
        if(!request.path.equals("/")){
        val isConnected = request.session.get("username").map(username => !models.Users.findByUsername(username).equals(None)).getOrElse(false)
        if (!isConnected)
          Iteratee.ignore[Array[Byte]].map(_ => Results.Redirect("/#/login"))
        else
          next(request)
        }
          else next(request)
      }
    }
  }

}
*/
