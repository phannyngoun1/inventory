package com.dream.inventory.api.uom

import java.util.UUID

import julienrf.json.derived
import play.api.libs.json._


sealed trait UoMEvent {
  val uomId: UUID
}

object UoMEvent {
  implicit val format: Format[UoMEvent] =
    derived.flat.oformat((__ \ "type").format[String])
}

case class UoMCreated(override val uomId: UUID) extends UoMEvent

object UoMCreated {
  implicit val format: Format[UoMCreated] = Json.format
}
