package com.dream.inventory.api.uom

import java.time.Instant
import java.util.UUID

import com.dream.inventory.common.{BaseObject, MeasurementType}
import play.api.libs.json.{Format, Json}

case class UoM (
  id              : Option[UUID],
  code            : String,
  abbr            : String,
  measurementType : MeasurementType,
  description     : String,
  creator         : UUID,
  modifiedBy      : UUID,
  createdAt       : Option[Instant] = None,
  modifiedAt      : Option[Instant] = None
) extends BaseObject {
  def safeId = id.getOrElse(UUID.randomUUID())
}

object UoM {
  implicit val format: Format[UoM] = Json.format

  def create(
    code            : String,
    abbr            : String,
    measurementType : MeasurementType,
    description     : String,
    creator         : UUID,
    modifiedBy      : UUID
  ) = UoM(
    id              = None,
    code            = code,
    abbr            = abbr,
    measurementType = measurementType,
    description     = description,
    creator         = creator,
    modifiedBy      = modifiedBy,
    createdAt       = None,
    modifiedAt      = None
  )
}