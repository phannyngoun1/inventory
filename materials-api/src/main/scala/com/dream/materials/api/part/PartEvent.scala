package com.dream.materials.api.part

import java.time.Instant
import java.util.UUID

import julienrf.json.derived
import play.api.libs.json._

sealed trait PartEvent {
  val partId: UUID
}

object PartEvent {
  implicit val format: Format[PartEvent] =
    derived.flat.oformat((__ \ "type").format[String])
}

case class PartCreated(partId: UUID, creator: UUID, createdAt: Instant)

case class PartUpdated(partId: UUID, creatorId: UUID) extends PartEvent
object PartUpdated {
  implicit val format: Format[PartUpdated] = Json.format
}