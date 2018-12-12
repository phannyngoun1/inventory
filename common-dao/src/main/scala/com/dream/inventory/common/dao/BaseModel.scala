package com.dream.inventory.common.dao

import java.time.Instant
import java.util.UUID
import play.api.libs.json.{Format, Json}


case class AuditData(
  creator: UUID,
  modifiedBy: Option[UUID] = None,
  createdAt: Instant = Instant.now(),
  modifiedAt: Option[Instant] = None
)

object AuditData {
  implicit val format: Format[AuditData] = Json.format
}

case class RecordStatus (
  isActive: Boolean = true,
  isDeleted: Boolean = false
)

object RecordStatus {
  implicit val format: Format[RecordStatus] = Json.format
}

trait BaseModel {

  def id: UUID

  def auditData: AuditData

  def recordStatus: RecordStatus

}


