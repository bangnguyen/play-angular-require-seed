package controllers
import secure.Secured
import play.api.mvc._
import scala.language.postfixOps
/**
 * Created by Marco Chu on 5/15/14.
 */
object Profile extends   Controller with Secured{


  def createProfile = SecuredAction {
    secureRequest =>

    Ok("no ok")
  }






}
