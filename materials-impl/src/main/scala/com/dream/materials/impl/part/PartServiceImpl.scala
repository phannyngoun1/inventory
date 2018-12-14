package com.dream.materials.impl.part

import java.util.UUID

import akka.NotUsed
import com.datastax.driver.core.utils.UUIDs
import com.dream.inventory.common.PartType
import com.dream.inventory.common.dao.AuditData
import com.dream.inventory.security.ServerSecurity._
import com.dream.materials._
import com.dream.materials.api.part.{CreatePartRequest, Part, PartBasicInfo, PartService}
import com.dream.materials.impl.MaterialServiceImpl
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{BadRequest, Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.dream.inventory.validate.Validation.Validator.ops._

import scala.concurrent.Future

trait PartServiceImpl extends PartService with PartValidation{
  this: MaterialServiceImpl =>


  override def createPart = authenticated(userId => ServerServiceCall { part =>

    if (userId != part.creator) {
      throw Forbidden("User " + userId + " can't created an item on behalf of " + part.creator)
    }


    val partId = UUIDs.timeBased()
    val pPart = PartDataModel(
      id = partId,
      partNr = part.partNr,
      description = part.description,
      partType = part.partType,
      auditData = AuditData(
        creator = part.creator,
        modifiedBy = Some(part.creator)
      )
    )

    val createPartRequest = CreatePartRequest(
      partBasicInfo = PartBasicInfo(
        partNr = "aa",
        description = "aa",
        partType = PartType.Inventory,
        creator = userId
      )
    )

    val result: Part = createPartRequest.validate.fold(errors => throw BadRequest(errors.map(_.message).toList.mkString(", ")), fa => part)

    Future.successful(result)
  })

  override def getPart(uUID: UUID): ServiceCall[NotUsed, api.part.Part] = authenticated(_ => ServerServiceCall { _ => {

    entityRef(uUID).ask(GetPart).map {
      case Some(part) => convertPart(part)
      case None => throw NotFound("Item " + uUID + " not found")
    }
  }
  })

  private def entityRef(partId: UUID) = entityRefString(partId.toString)

  private def entityRefString(partId: String) = registry.refFor[PartEntity](partId)

  private def convertPart(part: PartDataModel) =
    api.part.Part(
      id = Some(part.id),
      creator = part.auditData.creator,
      partNr = part.partNr,
      description = part.description,
      partType = part.partType,
      modifiedBy = part.auditData.modifiedBy,
      createdAt = Some(part.auditData.createdAt),
      modifiedAt = part.auditData.modifiedAt
    )
}
