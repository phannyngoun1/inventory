package com.dream.materials.api.part

import java.time.Instant
import java.util.UUID

import com.dream.inventory.common.{BaseObject, PartTrackingType, PartTrackingValue, PartType}
import play.api.libs.json.{Format, Json}

case class Part(
  id: Option[UUID],
  partNr: String,
  description: String,
  partType: PartType,
  creator: UUID,
  modifiedBy: Option[UUID],
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
  ) = Part(None, partNr, description, partType, creator,None, None, None )
}


case class CreatePartRequest(
  partBasicInfo: PartBasicInfo,
  partTrackingMethod: Option[PartTrackingMethod] = None,
  initialInventory: Option[InitialInventory] = None,
  defaultVendor: Option[DefaultVendor]= None,
  defaultAccount: Option[DefaultAccount] = None
)

object CreatePartRequest{
  implicit val format: Format[CreatePartRequest] = Json.format
}

case class PartBasicInfo(
  partNr: String,
  description: String,
  partType: PartType,
  upc: Option[String] = None,
  uomId: Option[String] = None,
  creator: UUID
)

object PartBasicInfo {
  implicit val format: Format[PartBasicInfo] = Json.format
}

case class PartTrackingMethod(
  partTrackingType: PartTrackingType,
  nextValue: Option[String] = None,
  isPrimary: Boolean = false
)


object PartTrackingMethod {
  implicit val format: Format[PartTrackingMethod] = Json.format
}

case class InitialInventory(
  locationId: UUID,
  qty: Float,
  unitCost: Double,
  uomUd: UUID,
  date: Instant,
  partTrackingValue: List[PartTrackingValue] = List.empty
)

object InitialInventory {
  implicit val format: Format[InitialInventory] = Json.format
}


case class DefaultVendor(
  vendorId: UUID,
  vendorPartNr: String,
  vendorPartUomId: UUID,
  lastCost: Double
)

object DefaultVendor {
  implicit val format: Format[DefaultVendor] = Json.format
}

case class DefaultAccount(
  assetAcctId: Option[UUID],
  cogsAcctId: Option[UUID],
  adjustmentAcctId: Option[UUID],
  scrapAcctId: Option[UUID]
)


object DefaultAccount {
  implicit val format: Format[DefaultAccount] = Json.format
}