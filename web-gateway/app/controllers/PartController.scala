package controllers

import java.util.UUID

import com.dream.inventory.common.PartType
import com.dream.inventory.security.ClientSecurity.authenticate
import com.dream.materials.api.MaterialService
import com.dream.materials.api.part._
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

class PartController(materialService: MaterialService, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractAuctionController(controllerComponents) {

  def createPart = Action.async { implicit rh =>

    requireUser(userId => {
      val part = Part.create(
        partNr = "Part 004",
        description = "Part 004",
        partType = PartType.InternalUsed,
        creator = userId
      )
      for {
        part <- materialService.createPart.handleRequestHeader(authenticate(userId)).invoke(part)
      } yield {
        Ok(s"Part ${part.id} created")
      }
    }).recover {
      case e => BadRequest(s"Oops ${e.getMessage}")
    }
  }

  def getPart(partId: UUID) = Action.async { implicit rh =>

    requireUser(userId => {
      for {
        part <- materialService.getPart(partId)
          .handleRequestHeader(authenticate(userId))
          .invoke()
      } yield {
        Ok(Json.toJson(part))
      }
    }).recover {
      case e => BadRequest(s"Oops ${e.getMessage}")
    }
  }


}
