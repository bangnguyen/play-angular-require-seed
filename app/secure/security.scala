package secure

import play.api.mvc._

import models.User
import controllers.routes

/**
 * A request that adds the User for the current call
 */

case class SecuredRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

case class InfoRequest[A](user: Option[User], request: Request[A]) extends WrappedRequest(request)

trait Secured extends Controller {
  /**
   * A secured action.  If there is no user in the session the request is redirected
   * to the login page
   *
   * @param p the body parser to use
   * @param f the wrapped action to invoke
   * @tparam A
   * @return
   */
  def InfoAction[A](p: BodyParser[A])
                   (f: InfoRequest[A] => Result)
  = Action(p) {
    implicit request => {
      request.session.get("username").map { username =>
        val user =  User(username = username, password = "password")
        f(InfoRequest(Some(user), request))
      }.getOrElse{
        f(InfoRequest(None, request))
      }
    }
  }

  def InfoAction(f: InfoRequest[AnyContent] => Result): Action[AnyContent] = InfoAction(parse.anyContent)(f)
  /**
   * A secured action.  If there is no user in the session the request is redirected
   * to the login page
   *
   * @param p the body parser to use
   * @param f the wrapped action to invoke
   * @tparam A
   * @return
   */
  def SecuredAction[A](p: BodyParser[A])
                      (f: SecuredRequest[A] => Result)
                      = Action(p) {
    implicit request => {
      request.session.get("username").map { username =>
        val user = User(username = username, password = "password")

        f(SecuredRequest(user, request))
      }.getOrElse {
        val url = request.uri
        Redirect("/")
      }
    }
  }

  def SecuredAction(f: SecuredRequest[AnyContent] => Result): Action[AnyContent] = SecuredAction(parse.anyContent)(f)

  /**
   * A secured action.  If there is no user in the session the request is redirected
   * to the login page
   *
   * @param p the body parser to use
   * @param f the wrapped action to invoke
   * @tparam A
   * @return
   */
  def UnSecuredAction[A](p: BodyParser[A])
                      (f: Request[A] => Result)
  = Action(p) {
    implicit request => {
      val username = request.session.get("username")
      val url = request.uri
      if(!username.isEmpty) {
        Redirect("/")
      }
      else {
        f(request)
      }

    }
  }

  def UnSecuredAction(f: Request[AnyContent] => Result): Action[AnyContent] = UnSecuredAction(parse.anyContent)(f)
}