package com.dream.materials.impl.part

import java.time.Instant
import java.util.UUID

import akka.Done
import com.dream.inventory.common.dao.BaseModel
import com.dream.inventory.common.{PartTracking, AbstractPartTracking, PartTrackingType, PartType}
import com.dream.inventory.utils.JsonFormats.singletonFormat
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}

/** *************************Data *****************************/

case class PartDefaultLocationDataModel(
  groupLocationId: UUID,
  locationId: UUID
)

object PartDefaultLocationDataModel {
  implicit val format: Format[PartDefaultLocationDataModel] = Json.format
}

case class PartAcctDataModel(
  assetAccountId: Option[UUID],
  cosgsAccountId: Option[UUID],
  adjustmentAccountId: Option[UUID],
  scrapAccountId: Option[UUID]
)

object PartAcctDataModel {
  implicit val format: Format[PartAcctDataModel] = Json.format
}


case class PartSizeWeightDataModel(
  length: Option[Float],
  width: Option[Float],
  height: Option[Float],
  sizeUoM: Option[UUID],
  weight: Option[Float],
  weightUoM: Option[UUID]
)

object PartSizeWeightDataModel {
  implicit val format: Format[PartSizeWeightDataModel] = Json.format
}

case class PartTrackingMethodDataModel(
  partTrackingType: PartTrackingType,
  nextValue: String,
  isPrimary: Boolean
)


object PartTrackingMethodDataModel {
  implicit val format: Format[PartTrackingMethodDataModel] = Json.format
}

case class PartReorderLevelDataModel(
  groupLocationId: UUID,
  rop: Float, // Reorder Point
  oul: Option[Float] // Order Up to Level
)

object PartReorderLevelDataModel {
  implicit val format: Format[PartReorderLevelDataModel] = Json.format
}

case class InventoryDataModel(

  onHand: Float = 0,
  allocated: Float = 0,
  notAvailable: Float = 0,
  dropShip: Float = 0,
  committed: Float = 0 ,
  shot: Float = 0,
  onOrder: Float =0,
  availableToPick: Float =0,
  availableForSale: Float =0,

  avgCost: Double = 0,
  totalAvgCost: Double = 0,

  stdCost: Double = 0,
  totalStdCost: Double = 0,

  costingLayers: List[CostLayerDataModel] = List.empty,
  partTracking: List[PartTracking] = List.empty
)

object InventoryDataModel {
  implicit val format: Format[InventoryDataModel] = Json.format
}

case class PartTrackingDataModel(
  locationId: UUID,
  trackingTracking: PartTracking
)

object PartTrackingDataModel {
  implicit val format: Format[PartTrackingDataModel] = Json.format
}

case class CostLayerDataModel(
  date: Instant,
  qty: Float,
  unitCost: Double,
  totalCost: Double
)

object CostLayerDataModel {
  implicit val format: Format[CostLayerDataModel] = Json.format
}

case class PartDataModel (

  id: UUID,

  creator: UUID,

  partNr: String,

  description: String,

  partType: PartType,

  abcCode: Option[String] = None,

  stCost: Option[Float] = None,

  defaultBoMId: Option[UUID] = None,

  defaultProductId: Option[UUID] = None,

  details: Option[String] = None,

  pickInUoMPartOnly: Boolean = false,

  defaultLocations: List[PartDefaultLocationDataModel] = List.empty,

  accounts: Option[PartAcctDataModel] = None,

  sizeWeight: Option[PartSizeWeightDataModel] = None,

  trackingMethod: List[PartTrackingMethodDataModel] = List.empty,

  reorderLevels: List[PartReorderLevelDataModel] = List.empty,


  inventory: Option[InventoryDataModel]  = None,

  modifiedBy: UUID,

  createdAt: Instant = Instant.now(),

  modifiedAt: Option[Instant] = None,

  isActive: Boolean = true,

  isDeleted: Boolean = false

) extends  BaseModel {
  def disable: Either[PartError, PartDataModel] = Right(copy(
    isActive = false
  ))

  def enable: Either[PartError, PartDataModel] =  Right(copy(
    isActive = true
  ))
}

object PartDataModel {

  implicit val format: Format[PartDataModel] = Json.format


}

/** *************************Command *****************************/
sealed trait PartCommand

case object GetPart extends PartCommand with ReplyType[Option[PartDataModel]] {
  implicit val format: Format[GetPart.type] = singletonFormat(GetPart)
}

case class CreatePart(
  part: PartDataModel
) extends PartCommand with ReplyType[Done]

object CreatePart {
  implicit val format: Format[CreatePart] = Json.format
}

object DisablePart extends PartCommand with ReplyType[Done] {
  implicit val format: Format[DisablePart.type] = singletonFormat(DisablePart)
}

case object EnablePart extends PartCommand with ReplyType[Done] {
  implicit val format: Format[EnablePart.type] = singletonFormat(EnablePart)
}


/** *************************Event *****************************/

sealed trait PartEvent extends AggregateEvent[PartEvent] {
  override def aggregateTag: AggregateEventTagger[PartEvent] = PartEvent.Tag
}

object PartEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[PartEvent](NumShards)
}

case class PartCreated(part: PartDataModel) extends PartEvent

object PartCreated {
  implicit val format: Format[PartCreated] = Json.format
}

case object PartDisabled extends PartEvent {
  implicit val format: Format[PartDisabled.type] = singletonFormat(PartDisabled)
}

case object PartEnabled extends PartEvent {
  implicit val format: Format[PartEnabled.type] = singletonFormat(PartEnabled)
}


/** *************************Error *****************************/

sealed abstract class PartError(val message: String)

case class DefaultPartError(override val message: String) extends PartError(message)