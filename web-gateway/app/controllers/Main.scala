package controllers

import com.dream.inventory.user.api.UserService
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

class Main(userService: UserService, controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext)
  extends AbstractAuctionController(controllerComponents) {

  def index = Action.async { implicit rh =>

    requireUser(userId => for {
      users <- userService.getUsers.invoke()
    } yield {
      Ok(users.toString())
    }).recover{
      case _ => BadRequest("Oops")
    }
  }
}
