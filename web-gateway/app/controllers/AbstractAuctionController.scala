package controllers

import java.util.UUID

import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractAuctionController(controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext)
  extends AbstractController(controllerComponents) with UserSession {

  override def isMockUser: Boolean = true

  protected def requireUser(block: UUID => Future[Result])(implicit rh: RequestHeader): Future[Result] = withUser {
    case Some(user) => block(user)
    case None => Future.successful(Redirect(routes.Main.index))
  }

}
