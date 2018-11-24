package com.dream.materials.api.part

import java.time.Instant
import java.util.UUID

import com.dream.inventory.common.{BaseObject, PartType}
import play.api.libs.json.{Format, Json}

case class Part(
  id: Option[UUID],
  partNr: String,
  description: String,
  partType: PartType,
  creator: UUID,
  modifiedBy: UUID,
  createdAt: Option[Instant],
  modifiedAt: Option[Instant]
) extends BaseObject {
  def safeId = id.getOrElse(UUID.randomUUID())
}

object Part {

  implicit val format: Format[Part] = Json.format

  def create(
    creator: UUID,
    partNr: String,
    description: String,
    partType: PartType,
  ) = Part(None, partNr, description, partType, creator,creator, None, None )
}