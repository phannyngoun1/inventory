package controllers

import java.util.UUID

import play.api.mvc._

trait UserSession {

  private val uUID = UUID.fromString("b0a6a6a0-ecbb-43fa-b4e0-6e7272202402")

  def isMockUser: Boolean = false

  protected def withUser[T](block: Option[UUID] => T)(implicit rh: RequestHeader): T = {
    block{
     if(isMockUser) Some(uUID) else rh.session.get("user").map(UUID.fromString)
    }
  }

}
