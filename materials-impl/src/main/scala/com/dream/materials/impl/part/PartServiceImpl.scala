package com.dream.materials.impl.part

import java.util.UUID

import akka.NotUsed
import com.datastax.driver.core.utils.UUIDs
import com.dream.inventory.security.ServerSecurity._
import com.dream.materials.api.part.PartService
import com.dream.materials._
import com.dream.materials.impl.MaterialServiceImpl
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{Forbidden, NotFound}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

trait PartServiceImpl extends PartService {
  this: MaterialServiceImpl =>

  override def createPart = authenticated(userId => ServerServiceCall { part =>

    if (userId != part.creator) {
      throw Forbidden("User " + userId + " can't created an item on behalf of " + part.creator)
    }

    val partId = UUIDs.timeBased()
    val pPart = PartDataModel(
      id = partId,
      creator = part.creator,
      partNr = part.partNr,
      description = part.description,
      partType = part.partType,
      modifiedBy = part.creator
    )
    entityRef(partId).ask(CreatePart(pPart)).map { _ =>
      part
    }
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
      creator = part.creator,
      partNr = part.partNr,
      description = part.description,
      partType = part.partType,
      modifiedBy = part.modifiedBy,
      createdAt = Some(part.createdAt),
      modifiedAt = part.modifiedAt
    )
}
